package top.cutexingluo.tools.utils.ee.redisson;


import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import top.cutexingluo.tools.basepackage.baseimpl.XTRunCallUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Redisson工具类
 * <p>需要导入 org.redisson:redisson 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 22:56
 */
public class XTRedisson {

    /**
     * 常用4件套
     */
    // 看门狗

    /**
     * 执行锁里的东西，有返回值
     *
     * @param lock     锁
     * @param callTask 执行任务
     */
    public static <T> T call(RLock lock, Callable<T> callTask) throws Exception {
        return XTRunCallUtil.getTryCallable(callTask,
                lock::lock, lock::unlock).call();
    }

    /**
     * 执行锁里的东西，无返回值
     *
     * @param lock    锁
     * @param runTask 执行任务
     */
    public static <T> void run(RLock lock, Runnable runTask) throws Exception {
        XTRunCallUtil.getTryRunnable(runTask,
                lock::lock, lock::unlock).run();
    }

    // 带时间的更常用

    /**
     * 执行锁里的东西，有返回值
     *
     * @param lock     锁
     * @param timeout  加锁的时长, 过时自动释放
     * @param callTask 执行任务
     */
    public static <T> T call(RLock lock, int timeout, Callable<T> callTask) throws Exception {
        return XTRunCallUtil.getTryCallable(callTask,
                () -> lock.lock(timeout,
                        TimeUnit.SECONDS), lock::unlock).call();
    }

    /**
     * 执行锁里的东西，无返回值
     *
     * @param lock    锁
     * @param timeout 加锁的时长, 过时自动释放
     * @param runTask 执行任务
     */
    public static <T> void run(RLock lock, int timeout, Runnable runTask) throws Exception {
        XTRunCallUtil.getTryRunnable(runTask,
                () -> lock.lock(timeout,
                        TimeUnit.SECONDS), lock::unlock).run();
    }

    /**
     * 懒人工具锁类，普通锁，不常用
     */
    // 看门狗解锁

    /**
     * 利用 RedissonClient 锁执行任务
     *
     * @param redisson redisson锁
     * @param name     锁的名称
     * @param runTask  执行任务
     */
    public static void run(RedissonClient redisson, String name, Runnable runTask) {
        RLock lock = redisson.getLock(name);
        XTRunCallUtil.getTryRunnable(runTask, lock::lock, lock::unlock).run();
    }

    /**
     * 利用 RedissonClient 锁执行任务
     *
     * @param redisson redisson锁
     * @param name     锁的名称
     * @param callTask 执行任务
     */
    public static <T> T call(RedissonClient redisson, String name, Callable<T> callTask) throws Exception {
        RLock lock = redisson.getLock(name);
        return XTRunCallUtil.getTryCallable(callTask, lock::lock, lock::unlock).call();
    }

    // 设定时间

    /**
     * 利用 RedissonClient 锁执行任务
     *
     * @param redisson redisson锁
     * @param name     锁的名称
     * @param timeout  加锁的时长, 过时自动释放
     * @param runTask  执行任务
     */
    public static void run(RedissonClient redisson, String name, int timeout, Runnable runTask) {
        RLock lock = redisson.getLock(name);
        XTRunCallUtil.getTryRunnable(runTask,
                () -> lock.lock(timeout, TimeUnit.SECONDS),
                lock::unlock).run();
    }

    /**
     * 利用 RedissonClient 锁执行任务
     *
     * @param redisson redisson锁
     * @param name     锁的名称
     * @param timeout  加锁的时长, 过时自动释放
     * @param callTask 执行任务
     */
    public static <T> T call(RedissonClient redisson, String name, int timeout, Callable<T> callTask) throws Exception {
        RLock lock = redisson.getLock(name);
        return XTRunCallUtil.getTryCallable(callTask,
                () -> lock.lock(timeout,
                        TimeUnit.SECONDS), lock::unlock).call();
    }

    // 锁30s

    /**
     * 利用 RedissonClient 锁执行任务, 设置时长30s
     *
     * @param redisson redisson锁
     * @param name     锁的名称
     * @param runTask  执行任务
     */
    public static void run30s(RedissonClient redisson, String name, Runnable runTask) {
        run(redisson, name, 30, runTask);
    }

    /**
     * 利用 RedissonClient 锁执行任务, 设置时长30s
     *
     * @param redisson redisson锁
     * @param name     锁的名称
     * @param callTask 执行任务
     */
    public static <T> T call30s(RedissonClient redisson, String name, Callable<T> callTask) throws Exception {
        return call(redisson, name, 30, callTask);
    }

    // 设定时间

    /**
     * 利用 RedissonClient 锁执行任务, 自行进行diy锁
     *
     * @param redisson RedissonClient锁
     * @param name     锁的名称
     * @param timeout  加锁的时长, 过时自动释放
     * @param takeLock 自行操作锁，手动加锁，执行完任务自动释放（所以在此方法里加锁返回true）
     * @param runTask  执行的任务
     */
    public static void runDiy(RedissonClient redisson, String name, int timeout, Function<RLock, Boolean> takeLock, Runnable runTask) {
        RLock lock = redisson.getLock(name);
        Boolean isLock = takeLock.apply(lock);
        if (isLock) XTRunCallUtil.getTryRunnable(runTask,
                null, lock::unlock).run();
    }

    /**
     * 利用 RedissonClient 锁执行任务, 自行进行diy锁
     *
     * @param redisson RedissonClient锁
     * @param name     锁的名称
     * @param timeout  加锁的时长, 过时自动释放
     * @param takeLock 自行操作锁，手动加锁，执行完任务自动释放（所以在此方法里加锁返回true）
     * @param callTask 执行的任务
     */
    public static <T> T callDiy(RedissonClient redisson, String name, int timeout, Function<RLock, Boolean> takeLock, Callable<T> callTask) throws Exception {
        RLock lock = redisson.getLock(name);
        Boolean isLock = takeLock.apply(lock);
        T ret = null;
        if (isLock) {
            ret = XTRunCallUtil.getTryCallable(callTask,
                    null, lock::unlock).call();
        }
        return ret;
    }

    /**
     * 利用 RedissonClient 锁执行任务, 尝试加锁（在等待时间内），锁上了一段时间后解锁
     *
     * @param redisson   RedissonClient锁
     * @param name       锁的名称
     * @param waitTime   等待时间，尝试加锁
     * @param timeUnLock 加锁成功后解锁时长
     * @param callTask   执行任务
     */
    public static <T> T tryLockCall(RedissonClient redisson, String name, int waitTime, int timeUnLock, Callable<T> callTask) throws Exception {
        RLock lock = redisson.getLock(name);
        boolean isLock = lock.tryLock(waitTime, timeUnLock, TimeUnit.SECONDS);
        T ret = null;
        if (isLock) ret = XTRunCallUtil.getTryCallable(callTask,
                null, lock::unlock).call();
        return ret;
    }

    /**
     * 利用 RedissonClient 锁执行任务, 尝试加锁（在等待时间内），锁上了一段时间后解锁
     *
     * @param redisson   RedissonClient锁
     * @param name       锁的名称
     * @param waitTime   等待时间，尝试加锁
     * @param timeUnLock 加锁成功后解锁时长
     * @param runTask    执行任务
     */
    public static <T> void tryLockRun(RedissonClient redisson, String name, int waitTime, int timeUnLock, Runnable runTask) throws Exception {
        RLock lock = redisson.getLock(name);
        boolean isLock = lock.tryLock(waitTime, timeUnLock, TimeUnit.SECONDS);
        if (isLock) XTRunCallUtil.getTryRunnable(runTask,
                null, lock::unlock).run();
    }

}
