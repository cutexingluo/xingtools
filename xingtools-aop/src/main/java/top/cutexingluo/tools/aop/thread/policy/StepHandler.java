package top.cutexingluo.tools.aop.thread.policy;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.transaction.PlatformTransactionManager;
import top.cutexingluo.core.designtools.juc.lock.extra.XTLockType;
import top.cutexingluo.tools.aop.thread.MainThread;
import top.cutexingluo.tools.aop.thread.SonThread;
import top.cutexingluo.tools.aop.thread.run.RollbackPolicy;
import top.cutexingluo.tools.aop.thread.run.ThreadAopHandler;
import top.cutexingluo.tools.aop.thread.run.ThreadPolicy;
import top.cutexingluo.tools.aop.transactional.TransactionMeta;

import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * <p>CountDownLatch 放行 异步转同步策略</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/2 14:02
 * @since 1.0.2
 */
@Data
@AllArgsConstructor
public class StepHandler implements ThreadAopHandler {

    public static final ThreadPolicy policy = ThreadPolicy.Step;

    private ConcurrentHashMap<String, Object> map;
    private PlatformTransactionManager transactionManager;

    @Override
    public boolean needAutowired() {
        return true;
    }

    @Override
    public Object runInMainAop(ProceedingJoinPoint joinPoint, MainThread mainThread) throws Exception {
        String threadName = getMainThreadNameInMain();
        //-----------begin------------
        AtomicInteger count = newInteger(mainThread.sonThreadNum());
        setSonCount(threadName + ":count", count); // 设置数目
        //初始化计数器
        CountDownLatch mainDownLatch = newCountDownLatch(1); //主线程放行
        CountDownLatch sonDownLatch = newCountDownLatch(mainThread.value());//子线程的数量
        // 用来记录子线程的运行状态，只要有一个失败就变为true
        AtomicBoolean rollBackFlag = newBoolean(false);
        // 用来存每个子线程的异常，把每个线程的自定义异常向vector的首位置插入，其余异常向末位置插入，避免线程不安全，所以使用vector代替list
        Vector<Exception> exceptionVector = newExceptionVector();


//        map.put(threadName + ":startTime", mainThread.startTime()); //开始时间。全程执行，无时间。
        map.put(threadName + ":rollbackPolicy", mainThread.rollbackPolicy()); //回滚策略
        map.put(threadName + ":mainDownLatch", mainDownLatch);
        map.put(threadName + ":sonDownLatch", sonDownLatch);
        map.put(threadName + ":exceptionVector", exceptionVector);
        map.put(threadName + ":rollBackFlag", rollBackFlag);
        boolean isGroup = mainThread.rollbackPolicy() == RollbackPolicy.Group; //是否集体回滚


        //-----------run------------
        Object result = getTask(joinPoint, (e) -> { //此时主线程和子线程已分离
            exceptionVector.add(0, e);
            rollBackFlag.set(true);//子线程回滚
            mainDownLatch.countDown();//放行所有子线程
        }).call(); //开始执行

        if (!rollBackFlag.get()) {
            try {
                // sonDownLatch等待，直到所有子线程执行完操作，但此时还没有提交事务
                sonDownLatch.await(); //等子线程执行完成
                if (mainDownLatch.getCount() > 0)
                    mainDownLatch.countDown();// 根据rollBackFlag状态放行子线程的await处，告知是回滚还是提交
            } catch (Exception e) {
                rollBackFlag.set(true);
                exceptionVector.add(0, e);
            }
        }
        //-----------end------------
//        map.remove(threadName + ":startTime");
        map.remove(threadName + ":rollbackPolicy"); // 回滚策略
        map.remove(threadName + ":mainDownLatch");
        map.remove(threadName + ":sonDownLatch");
        map.remove(threadName + ":rollBackFlag");
        map.remove(threadName + ":exceptionVector");
        removeSonCount(threadName + ":count"); // 移除核心
        if (CollUtil.isNotEmpty(exceptionVector)) {
            throw exceptionVector.get(0);
        }
        return result;
    }

    @Override
    public Object runInSonAop(ProceedingJoinPoint joinPoint, SonThread sonThread) throws Exception {
        String threadName = getMainThreadNameInSon(joinPoint, true);
        //-----------begin------------
        AtomicInteger count = getSonCount(threadName + ":count", AtomicInteger.class);
        if (count == null) {
            //主事务未加注解时, 直接执行子事务
            return getTask(joinPoint).call();
        }
//        ThreadTimePolicy timePolicy = (ThreadTimePolicy) map.get(threadName + ":startTime"); //开始时间
        RollbackPolicy rollbackPolicy = (RollbackPolicy) map.get(threadName + ":rollbackPolicy"); // 回滚策略
        CountDownLatch mainDownLatch = (CountDownLatch) map.get(threadName + ":mainDownLatch");
        CountDownLatch sonDownLatch = (CountDownLatch) map.get(threadName + ":sonDownLatch");
        Vector<Exception> exceptionVector = (Vector<Exception>) map.get(threadName + ":exceptionVector");
        AtomicBoolean rollBackFlag = (AtomicBoolean) map.get(threadName + ":rollBackFlag");
        boolean isGroup = sonThread.transaction() && rollbackPolicy == RollbackPolicy.Group; //是否集体回滚

        //-----------run------------
        //如果这时有一个子线程已经出错，那当前线程不需要执行
        if (rollBackFlag.get()) {
            sonDownLatch.countDown();
            return null;
        }
        // 获取任务
        Callable<Object> task = getTask(joinPoint);

        // 加锁，包裹 //内部进行加锁
        if (sonThread.lockType() != XTLockType.NonLock) {
            task = addLock(task, sonThread);
        }

        // 异常处理
        BiConsumer<Exception, TransactionMeta> exceptionHandler = (e, meta) -> {
            exceptionVector.add(0, e);
            if (isGroup) { //回滚设置
                rollBackFlag.set(true);
            }
            meta.rollback(); //单独回滚
            if (mainDownLatch.getCount() > 0)
                mainDownLatch.countDown(); //有异常就释放前面执行过的所有线程
            sonDownLatch.countDown();
        };

        // 开启事务,包裹
        if (sonThread.transaction()) {
            if (transactionManager == null) {
                throw new Exception("transactionManager is null ! 需要自行注入事务管理器");
            }
            task = addTransaction(task, (r, meta) -> {
                try { //这里不能被双重捕获
                    sonDownLatch.countDown();
                    mainDownLatch.await();// 如果mainDownLatch不是0，线程会在此阻塞，直到mainDownLatch变为0
                    // 如果能执行到这一步说明所有子线程都已经执行完毕判断如果atomicBoolean是true就回滚false就提交
                    if (rollBackFlag.get()) {
                        meta.rollback();
                    } else {
                        meta.commit();
                    }
                } catch (Exception e) {
                    exceptionVector.add(0, e);
                    if (isGroup) { //回滚设置
                        rollBackFlag.set(true);
                    }
                    if (mainDownLatch.getCount() > 0)
                        mainDownLatch.countDown(); //有异常就释放前面执行过的所有线程
                }
            }, exceptionHandler);
        } else { // 没有添加事务
            Callable<Object> finalTask = task;
            task = () -> { //包裹任务
                Object res = null;
                try {
                    res = finalTask.call();
//                    mainDownLatch.await();// 其实这里不加也行，反正没事务关系
                } catch (Exception e) {
                    exceptionVector.add(0, e);
//                    mainDownLatch.countDown(); //有异常就释放前面执行过的所有线程
                } finally {
                    sonDownLatch.countDown();
                }
                return res;
            };
        }

        //-----------end------------
        Object result = task.call();
        addResult(threadName, result); //记录返回值
        return result;
    }
}
