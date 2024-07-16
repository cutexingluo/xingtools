package top.cutexingluo.tools.aop.thread.policy;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.transaction.PlatformTransactionManager;
import top.cutexingluo.tools.aop.thread.MainThread;
import top.cutexingluo.tools.aop.thread.SonThread;
import top.cutexingluo.tools.aop.thread.run.RollbackPolicy;
import top.cutexingluo.tools.aop.thread.run.ThreadAopHandler;
import top.cutexingluo.tools.aop.thread.run.ThreadPolicy;
import top.cutexingluo.tools.aop.thread.run.ThreadTimePolicy;
import top.cutexingluo.tools.aop.transactional.TransactionMeta;
import top.cutexingluo.tools.designtools.juc.async.XTCompletionService;
import top.cutexingluo.tools.designtools.juc.lock.extra.XTLockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 自动异步+并发+ 获得结果 策略
 * <p>提交 / 获取结果是异步的</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/5 16:43
 * @since 1.0.2
 */
@Data
@AllArgsConstructor
public class FutureHandler implements ThreadAopHandler {
    public static final ThreadPolicy policy = ThreadPolicy.Future;

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
        RollbackPolicy rollbackPolicy = mainThread.rollbackPolicy();
        // 子线程数量
        AtomicInteger count = newInteger(mainThread.sonThreadNum());
        // 用来记录子线程的运行状态，只要有一个失败就变为true
        AtomicBoolean rollBackFlag = newBoolean(false);
        // 用来存每个子线程的异常，把每个线程的自定义异常向vector的首位置插入，其余异常向末位置插入，避免线程不安全，所以使用vector代替list
        Vector<Exception> exceptionVector = newExceptionVector();
        List<Future<Object>> futureList = new ArrayList<>();
        boolean isGroup = rollbackPolicy == RollbackPolicy.Group; //是否集体回滚
        List<TransactionMeta> transactionMetas = new ArrayList<>(); //事务列表


        setSonCount(name + ":count", count); // 设置数目
        map.put(name + ":startTime", startTime); //开始时间策略
        map.put(name + ":rollbackPolicy", rollbackPolicy); //回滚策略
        map.put(name + ":exceptionVector", exceptionVector);
        map.put(name + ":rollbackFlag", rollBackFlag);
        map.put(name + ":futures", futureList); // future 结果
        map.put(name + ":transaction", transactionMetas); // 事务
        //-----------run------------
        Callable<Object> mainTask = getTask(joinPoint, (e) -> {
            exceptionVector.add(0, e);
        });
        try {
            mainTask.call();
            if (startTime == ThreadTimePolicy.GetFuture) { //主线程完了，子线程没完，强制阻塞。
                // 获取所有future的结果
                List<Object> results = futureList.stream().map(f -> {
                    if (f == null) return null;
                    try {
                        return f.get();
                    } catch (Exception e) {
                        exceptionVector.add(0, e);
                        if (isGroup) rollBackFlag.set(true);
                        return null;
                    }
                }).collect(Collectors.toList());
                setResultList(name, results);
                if (isGroup) {
                    if (rollBackFlag.get()) transactionMetas.forEach(TransactionMeta::rollback);
                    else transactionMetas.forEach(TransactionMeta::commit);
                }
            }
        } catch (Exception e) {
            exceptionVector.add(0, e);
        }

        //-----------end------------

        removeSonCount(name + ":count");
        map.remove(name + ":startTime");
        map.remove(name + ":exceptionVector");
        map.remove(name + ":rollbackPolicy"); // 回滚策略
        map.remove(name + ":rollbackFlag"); //是否回滚
        futureList.clear();
        map.remove(name + ":futures"); // future 结果
        map.remove(name + ":transaction"); // 事务
        map.remove(name + ":completionService"); //线程执行器
        removeResultList(name); // 结果列表
        if (CollUtil.isNotEmpty(exceptionVector)) {
            throw exceptionVector.get(0);
        }
        return null;
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
        AtomicBoolean rollBackFlag = (AtomicBoolean) map.get(name + ":rollbackFlag");//是否回滚
        RollbackPolicy rollbackPolicy = (RollbackPolicy) map.get(name + ":rollbackPolicy"); // 回滚策略
        List<TransactionMeta> transactionMetas = (List<TransactionMeta>) map.get(name + ":transaction");
        boolean isGroup = sonThread.transaction() && rollbackPolicy == RollbackPolicy.Group; //是否集体回滚

        //如果这时有一个子线程已经出错，那当前线程不需要执行
        if (isGroup && rollBackFlag.get()) { //出错不执行
            return null;
        }

        ThreadTimePolicy startTime = (ThreadTimePolicy) map.get(name + ":startTime"); // 时间策略
        Vector<Exception> exceptionVector = (Vector<Exception>) map.get(name + ":exceptionVector"); //异常信息
        XTCompletionService<Object> service = (XTCompletionService<Object>) map.get(name + ":completionService"); //运行线程
        if (service == null) { //新建线程处理器
            ExecutorService executorService = getExecutorService(sonThread);
            service = XTCompletionService.newInstance(executorService);
            map.put(name + ":completionService", service);
        }


        Callable<Object> task = getTask(joinPoint, () -> !isGroup || !rollBackFlag.get()); //z如果前面有异常就不执行

        // 加锁，包裹
        if (sonThread.lockType() != XTLockType.NonLock) {
            task = addLock(task, sonThread);
        }
        // 开启事务,包裹
        if (sonThread.transaction()) {
            if (transactionManager == null) {
                throw new Exception("transactionManager is null ! 需要自行注入事务管理器");
            }
            task = addTransaction(task, (r, meta) -> {
                if (isGroup) {
//                    transactionMetas.add(meta); //添加事务
                    if (rollBackFlag.get()) { //前面有出错全部回滚
                        meta.rollback();
                    } else {
                        transactionMetas.add(meta); //先暂时添加事务
                    }
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
        List<Future<Object>> futureList = (List<Future<Object>>) map.get(name + ":futures");
        Future<Object> future = service.submit(task);
        futureList.add(future);
        if (futureList.size() >= count.get()) { //全部开始执行
            // 最后一个子线程全部获取
            if (startTime == ThreadTimePolicy.GetResultAfterLastSon) {
                List<Object> results = null;
                try {
                    results = futureList.stream().map(f -> {
                        if (f == null) return null;
                        try {
                            return f.get(); //阻塞获取最先的
                        } catch (InterruptedException | ExecutionException e) {
                            exceptionVector.add(0, e);
                            if (isGroup) rollBackFlag.set(true);
                        }
                        return null;
                    }).collect(Collectors.toList());
                } catch (Exception e) {
                    exceptionVector.add(0, e);
                }
                setResultList(name, results);
            } else if (startTime == ThreadTimePolicy.GetQuickResult) {
                List<Object> results = new ArrayList<>();
                try {
                    for (int i = 0; i < count.get(); i++) {
                        Object o = service.take().get(); //阻塞获取最快的
                        results.add(o);
                    }
                } catch (Exception e) {
                    exceptionVector.add(0, e);
                    if (isGroup) rollBackFlag.set(true);
                }
                setResultList(name, results);
            }
            if (isGroup) {
                if (rollBackFlag.get()) transactionMetas.forEach(TransactionMeta::rollback);
                else transactionMetas.forEach(TransactionMeta::commit);
            }
        }

        //-----------end------------
        return null; //直接无返回值
    }
}
