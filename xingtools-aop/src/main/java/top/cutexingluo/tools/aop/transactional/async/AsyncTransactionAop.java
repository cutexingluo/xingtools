package top.cutexingluo.tools.aop.transactional.async;


import cn.hutool.core.collection.CollUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>需自行注入该bean</p>
 *
 * SpringBoot 中的多线程事务处理太繁琐？一个自定义注解直接搞定！
 *
 * @author snail , XingTian
 * @version 1.0.0
 * @date 2023/9/18 15:33
 */
@EnableAsync
@Aspect
//@Component
public class AsyncTransactionAop {

    //用来存储各线程计数器数据(每次执行后会从map中删除)
    private static final Map<String, Object> map = new ConcurrentHashMap<>();


    private PlatformTransactionManager transactionManager;

    @Autowired
    public AsyncTransactionAop(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }


    @Around("@annotation(mainTransaction)")
    public Object mainIntercept(ProceedingJoinPoint joinPoint, MainTransaction mainTransaction) throws Throwable {
        //当前线程名称
        Thread thread = Thread.currentThread();
        String threadName = thread.getName();
        //初始化计数器
        CountDownLatch mainDownLatch = new CountDownLatch(1);
        CountDownLatch sonDownLatch = new CountDownLatch(mainTransaction.value());//@MainTransaction注解中的参数, 为子线程的数量
        // 用来记录子线程的运行状态，只要有一个失败就变为true
        AtomicBoolean rollBackFlag = new AtomicBoolean(false);
        // 用来存每个子线程的异常，把每个线程的自定义异常向vector的首位置插入，其余异常向末位置插入，避免线程不安全，所以使用vector代替list
        Vector<Throwable> exceptionVector = new Vector<>();

        map.put(threadName + "mainDownLatch", mainDownLatch);
        map.put(threadName + "sonDownLatch", sonDownLatch);
        map.put(threadName + "rollBackFlag", rollBackFlag);
        map.put(threadName + "exceptionVector", exceptionVector);

        Object result = null;
        try {
            result = joinPoint.proceed();//执行方法
        } catch (Throwable e) {
            exceptionVector.add(0, e);
            rollBackFlag.set(true);//子线程回滚
            mainDownLatch.countDown();//放行所有子线程
        }

        if (!rollBackFlag.get()) {
            try {
                // sonDownLatch等待，直到所有子线程执行完插入操作，但此时还没有提交事务
                sonDownLatch.await();
                mainDownLatch.countDown();// 根据rollBackFlag状态放行子线程的await处，告知是回滚还是提交
            } catch (Exception e) {
                rollBackFlag.set(true);
                exceptionVector.add(0, e);
            }
        }
        if (CollUtil.isNotEmpty(exceptionVector)) {
            map.remove(threadName + "mainDownLatch");
            map.remove(threadName + "sonDownLatch");
            map.remove(threadName + "rollBackFlag");
            map.remove(threadName + "exceptionVector");
            throw exceptionVector.get(0);
        }
        return result;
    }

    @Around("@annotation(SonTransaction)")
    public Object sonIntercept(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Thread thread = Thread.currentThread();
        if (args[args.length - 1] instanceof Thread) {
            thread = (Thread) args[args.length - 1];
        }
        String threadName = thread.getName();
        CountDownLatch mainDownLatch = (CountDownLatch) map.get(threadName + "mainDownLatch");
        if (mainDownLatch == null) {
            //主事务未加注解时, 直接执行子事务
            return joinPoint.proceed();
        }
        CountDownLatch sonDownLatch = (CountDownLatch) map.get(threadName + "sonDownLatch");
        AtomicBoolean rollBackFlag = (AtomicBoolean) map.get(threadName + "rollBackFlag");
        Vector<Throwable> exceptionVector = (Vector<Throwable>) map.get(threadName + "exceptionVector");

        //如果这时有一个子线程已经出错，那当前线程不需要执行
        if (rollBackFlag.get()) {
            sonDownLatch.countDown();
            return null;
        }

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();// 开启事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 设置事务隔离级别
        TransactionStatus status = transactionManager.getTransaction(def);
        Object result = null;
        try {
            result = joinPoint.proceed();//执行方法

            sonDownLatch.countDown();// 对sonDownLatch-1
            mainDownLatch.await();// 如果mainDownLatch不是0，线程会在此阻塞，直到mainDownLatch变为0
            // 如果能执行到这一步说明所有子线程都已经执行完毕判断如果atomicBoolean是true就回滚false就提交
            if (rollBackFlag.get()) {
                transactionManager.rollback(status);
            } else {
                transactionManager.commit(status);
            }
        } catch (Throwable e) {
            exceptionVector.add(0, e);
            // 回滚
            transactionManager.rollback(status);
            // 并把状态设置为true
            rollBackFlag.set(true);
            mainDownLatch.countDown();
            sonDownLatch.countDown();
        }
        return result;
    }
}
