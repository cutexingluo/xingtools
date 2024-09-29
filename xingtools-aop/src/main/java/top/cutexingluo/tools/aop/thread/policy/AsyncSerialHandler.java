package top.cutexingluo.tools.aop.thread.policy;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;
import top.cutexingluo.tools.aop.thread.MainThread;
import top.cutexingluo.tools.aop.thread.SonThread;
import top.cutexingluo.tools.aop.thread.run.RollbackPolicy;
import top.cutexingluo.tools.aop.thread.run.ThreadAopHandler;
import top.cutexingluo.tools.aop.thread.run.ThreadPolicy;
import top.cutexingluo.tools.aop.thread.run.ThreadTimePolicy;
import top.cutexingluo.tools.aop.transactional.TransactionMeta;
import top.cutexingluo.tools.designtools.juc.async.XTAsync;
import top.cutexingluo.tools.designtools.juc.lock.extra.XTLockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自动异步 + 串行策略
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/2 16:38
 * @since 1.0.2
 */
@Data
@AllArgsConstructor
public class AsyncSerialHandler implements ThreadAopHandler {
    public static final ThreadPolicy policy = ThreadPolicy.AsyncSerial;

    private ConcurrentHashMap<String, Object> map;
    private PlatformTransactionManager transactionManager;

    @Override
    public boolean needAutowired() {
        return true;
    }

    @Override
    public Object runInMainAop(ProceedingJoinPoint joinPoint, MainThread mainThread) throws Exception {
        String name = getMainThreadNameInMain();
        //-----------begin------------
        ThreadTimePolicy startTime = getThreadTimePolicy(mainThread);
        map.put(name + ":startTime", startTime); //开始时间
        map.put(name + ":rollbackPolicy", mainThread.rollbackPolicy()); //回滚策略
        // 子线程数量
        AtomicInteger count = newInteger(mainThread.sonThreadNum());
        setSonCount(name + ":count", count); // 设置数目
        // 异常信息
        Vector<Exception> exceptionVector = newExceptionVector();
        map.put(name + ":exceptionVector", exceptionVector);
        AtomicBoolean rollBackFlag = newBoolean(false);//是否回滚
        map.put(name + ":rollbackFlag", rollBackFlag); //是否回滚
        boolean isGroup = mainThread.rollbackPolicy() == RollbackPolicy.Group; //是否集体回滚

        newTaskList(name + ":task"); //新建

        //-----------run------------
        Callable<Object> task = getTask(joinPoint);
        Object result = null;
        try {
            result = task.call();
            if (mainThread.startTime() == ThreadTimePolicy.AsyncAfterMain) {
                CompletableFuture<Object> allOfTask = (CompletableFuture<Object>) map.get(name + ":allTasks");
                Object o = allOfTask.get();// 运行完后执行
                addResult(name, o);
                if (isGroup) {
                    List<TransactionMeta> transactionMetas = (List<TransactionMeta>) map.get(name + ":transactions");
                    if (transactionMetas != null) {
                        if (rollBackFlag.get()) { //全部回滚
                            transactionMetas.forEach(TransactionMeta::rollback);
                        } else {
                            transactionMetas.forEach(TransactionMeta::commit);
                        }
                    }
                }
            }
        } catch (Exception e) {
            exceptionVector.add(0, e);
        }
        //-----------end------------
        removeTaskList(name + ":task"); //删除
        map.remove(name + ":allTasks"); //future删除
        map.remove(name + ":startTime");
        map.remove(name + ":exceptionVector");
        map.remove(name + ":rollbackPolicy"); // 回滚策略
        map.remove(name + ":rollbackFlag"); //是否回滚
        map.remove(name + ":transactions"); //回滚列表
        removeSonCount(name + ":count"); // 移除核心
        removeResultList(name);
        if (CollUtil.isNotEmpty(exceptionVector)) {
            throw exceptionVector.get(0);
        }
        return result;
    }

    @Override
    public Object runInSonAop(ProceedingJoinPoint joinPoint, SonThread sonThread) throws Exception {
        String name = getMainThreadNameInSon(joinPoint, false);
        //-----------begin------------
        AtomicInteger count = getSonCount(name + ":count", AtomicInteger.class);
        if (count == null) {
            //主事务未加注解时, 直接执行子任务
            return getTask(joinPoint).call();
        }
        Vector<Exception> exceptionVector = (Vector<Exception>) map.get(name + ":exceptionVector");
        AtomicBoolean rollBackFlag = (AtomicBoolean) map.get(name + ":rollbackFlag");//是否回滚
        RollbackPolicy rollbackPolicy = (RollbackPolicy) map.get(name + ":rollbackPolicy"); // 回滚策略
        boolean isGroup = sonThread.transaction() && rollbackPolicy == RollbackPolicy.Group; //是否集体回滚

        Callable<Object> task = getTask(joinPoint);
        List<TransactionMeta> transactionMetas = isGroup ? new ArrayList<>() : null;

        // 加锁，包裹
        if (sonThread.lockType() != XTLockType.NonLock) {
            task = addLock(task, sonThread);
        }

        // 开启事务,包裹
        if (sonThread.transaction()) {
            Assert.notNull(transactionManager, "transactionManager is null ! 需要自行注入事务管理器");
            task = addTransaction(task, (r, meta) -> {
                if (isGroup) {
                    transactionMetas.add(meta); //添加事务
                } else {
                    meta.commit();
                }
            }, (e, meta) -> {
                exceptionVector.add(0, e);
                if (isGroup) { //回滚设置
                    rollBackFlag.set(true);
                }
                meta.rollback();
            });
        }

        //-----------run------------
        addTask(name + ":task", task); //添加
        ArrayList<Callable<Object>> taskList = getTaskList(name + ":task");
        Object result = null;
        try {
            if (taskList.size() >= count.get()) { //执行，这就要求 @MainThread设置的线程数必须小于等于实际调用线程数
                // 编排
                ExecutorService service = getExecutorService(sonThread);
                XTAsync.FutureResult<Object> futureResult = XTAsync.serialFutureJoinByCallable(taskList, (e) -> {
                    exceptionVector.add(0, new Exception(e));
                    return e;
                }, service);
                CompletableFuture<Object> allOfTask = futureResult.getFuture();
                setResultList(name, futureResult.getResultList());
                // 执行
                ThreadTimePolicy timePolicy = (ThreadTimePolicy) map.get(name + ":startTime");
                if (timePolicy == ThreadTimePolicy.AsyncAfterMain) {
                    map.put(name + ":allTasks", allOfTask);
                    if (isGroup) {
                        map.put(name + ":transactions", transactionMetas);
                    }
                } else {
                    Object o = allOfTask.get();//直接执行
                    addResult(name, o);
                    if (isGroup && rollBackFlag.get()) { //全部回滚
                        transactionMetas.forEach(TransactionMeta::rollback);
                    }
                }
                taskList.clear();
            }
        } catch (Exception e) {
            exceptionVector.add(0, e);
        }

        //-----------end------------
        return result; //串行留最后一个值
    }
}
