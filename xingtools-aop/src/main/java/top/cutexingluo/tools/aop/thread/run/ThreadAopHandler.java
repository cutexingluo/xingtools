package top.cutexingluo.tools.aop.thread.run;

import cn.hutool.extra.spring.SpringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import top.cutexingluo.core.designtools.juc.lock.extra.XTLockMeta;
import top.cutexingluo.core.designtools.juc.thread.XTThreadPool;
import top.cutexingluo.tools.aop.thread.MainThread;
import top.cutexingluo.tools.aop.thread.SonThread;
import top.cutexingluo.tools.aop.thread.policy.ThreadAopFactory;
import top.cutexingluo.tools.aop.transactional.TransactionHandler;
import top.cutexingluo.tools.aop.transactional.TransactionMeta;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseJoinPointTaskHandler;
import top.cutexingluo.tools.designtools.juc.lock.handler.XTExtLockHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * Thread Aop处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/2 13:28
 * @since 1.0.2
 */
public interface ThreadAopHandler extends BaseJoinPointTaskHandler {

    default Vector<Exception> newExceptionVector() {
        return new Vector<>();
    }

    default AtomicInteger newInteger(int value) {
        return new AtomicInteger(value);
    }

    default AtomicBoolean newBoolean(boolean value) {
        return new AtomicBoolean(value);
    }

    /**
     * 域
     */
    default CountDownLatch newCountDownLatch(int count) {
        return new CountDownLatch(count);
    }

    default String getMainThreadNameInMain() {
        return ThreadAopFactory.getMainThreadNameInMain();
    }

    default String getMainThreadNameInSon(ProceedingJoinPoint joinPoint, boolean lastArgThread) {
        return ThreadAopFactory.getMainThreadNameInSon(joinPoint, lastArgThread);
    }

    /**
     * 是否需要外部的map和transactionManager
     */
    boolean needAutowired();

    /**
     * 事务
     */
    void setTransactionManager(@NotNull PlatformTransactionManager transactionManager);

    @NotNull
    PlatformTransactionManager getTransactionManager();

    /**
     * 数据存储
     */
    void setMap(@NotNull ConcurrentHashMap<String, Object> map);

    /**
     * 数据存储
     */
    @NotNull
    ConcurrentHashMap<String, Object> getMap();

    /**
     * 子线程数量
     */
    default <T extends Number> void setSonCount(String key, T sonCount) {
        getMap().put(key, sonCount);
    }

    default <T extends Number> T getSonCount(String key, Class<T> clazz) {
        return (T) getMap().get(key);
    }

    default <T extends Number> T removeSonCount(String key) {
        return (T) getMap().remove(key);
    }

    default ThreadTimePolicy getThreadTimePolicy(MainThread mainThread) {
        return ThreadAopFactory.getRightTimePolicy(mainThread);
    }

    /**
     * 获得结果
     */
    default List<Object> getResultList(String threadName) {
        return (List<Object>) getMap().get(threadName + ":results");
    }

    /**
     * 设置结果
     */
    default void setResultList(String threadName, List<Object> list) {
        getMap().put(threadName + ":results", list);
    }

    /**
     * 添加结果
     */
    default void addResult(String threadName, Object result) {
        if (getResultList(threadName) == null) {
            setResultList(threadName, new ArrayList<>());
        }
        getResultList(threadName).add(result);
    }

    /**
     * 移除结果
     */
    default void removeResultList(String threadName) {
        getMap().remove(threadName + ":results");
    }


    default ExecutorService getExecutorService(SonThread sonThread) {
        ExecutorService bean = SpringUtil.getBean(sonThread.threadPoolName());
        if (bean == null) {
            bean = SpringUtil.getBean(ExecutorService.class);
            if (bean == null) {
                bean = XTThreadPool.getInstance().getThreadPool();
            }
        }
        return bean;
    }

    /**
     * 新建任务列表
     */
    default <V> void newTaskList(String key) {
        ConcurrentHashMap<String, Object> map = getMap();
        map.put(key, new ArrayList<Callable<V>>());
    }

    /**
     * 删除任务列表
     */
    default <V> void removeTaskList(String key) {
        ConcurrentHashMap<String, Object> map = getMap();
        map.remove(key);
    }

    /**
     * 添加任务
     */
    default <V> void addTask(String key, Callable<V> task) {
        ConcurrentHashMap<String, Object> map = getMap();
        if (map.containsKey(key)) {
            ArrayList<Callable<V>> tasks = (ArrayList<Callable<V>>) map.get(key);
            tasks.add(task);
        } else {
            ArrayList<Callable<V>> tasks = new ArrayList<>();
            tasks.add(task);
            getMap().put(key, tasks);
        }
    }

    /**
     * 获得任务列表
     */
    default <V> ArrayList<Callable<V>> getTaskList(String key) {
        ConcurrentHashMap<String, Object> map = getMap();
        if (map.containsKey(key)) {
            return (ArrayList<Callable<V>>) getMap().get(key);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 2.任务加事务
     */
    default <T> Callable<T> addTransaction(Callable<T> task) {
        TransactionHandler transactionHandler = new TransactionHandler(true,
                getTransactionManager(), TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionHandler.decorate(task);
    }

    /**
     * 2.任务加事务
     */
    default <T> Callable<T> addTransaction(Callable<T> task, @Nullable BiConsumer<T, TransactionMeta> inTry, @Nullable BiConsumer<Exception, TransactionMeta> inCatch) {
        TransactionHandler transactionHandler = new TransactionHandler(true,
                getTransactionManager(), TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionHandler.decorate(task, inTry, inCatch);
    }

    /**
     * 任务加锁
     */
    default <T> Callable<T> addLock(Callable<T> task, SonThread sonThread) {
        XTLockMeta meta = new XTLockMeta(sonThread.lockName(), sonThread.lockType(), sonThread.isFair(), sonThread.tryTimeout());
        return addLock(task, meta, sonThread.redisson());
    }

    /**
     * 任务加锁
     */
    default <T> Callable<T> addLock(Callable<T> task, XTLockMeta meta, boolean openRedissonClient) {
        XTExtLockHandler lockHandler = new XTExtLockHandler(meta, openRedissonClient);
        return lockHandler.decorate(task);
    }


    /**
     * 在Main 执行的方法
     */
    Object runInMainAop(ProceedingJoinPoint joinPoint, MainThread mainThread) throws Exception;

    /**
     * 在Son 执行的方法
     */
    Object runInSonAop(ProceedingJoinPoint joinPoint, SonThread sonThread) throws Exception;
}
