package top.cutexingluo.tools.designtools.juc.lock;


import top.cutexingluo.tools.basepackage.baseimpl.XTRunCallUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * <p>XTLock 锁工具类 </p>
 * <p>1. 主要用于 运行加锁后的可执行接口，获得加锁的可执行接口</p>
 * <p>2. 此类为本地锁，推荐使用扩展包 new  XTLockHandler 拥有更多自动操作</p>
 * <p>3. 被XTLock使用，可以用XTLock加锁 ( 手动加lock )  </p>
 * <p>4. 可以开启AOP 使用扩展包注解 XTAopLock</p>
 *
 * @author XingTian
 * @version 1.0
 * @date 2022-11-22
 */
public class XTLockUtils {
    //----------------------------------------------------------------
    //常用锁 直接运行
    public static boolean doLock(Runnable runnable) {
        return doLock(new ReentrantLock(), false, runnable, null, null);
    }

    //常用锁2 直接运行
    public static boolean doLock(Lock lock, Runnable runnable) {
        return doLock(lock, false, runnable, null, null);
    }

    //常用锁3 直接运行
    public static boolean doLock(Lock lock, Runnable runnable, Runnable afterThis) {
        return doLock(lock, false, runnable, null, afterThis);
    }

    //常用锁4 直接运行
    public static boolean doLock(Lock lock, Runnable runnable, Runnable beforeThis, Runnable afterThis) {
        return doLock(lock, false, runnable, beforeThis, afterThis);
    }

    //基本锁结构 直接运行
    public static boolean doLock(Lock lock, boolean isFair, Runnable runnable, Runnable beforeThis, Runnable afterThis) {
        try {
            runnableLock(lock, isFair, runnable, beforeThis, afterThis).run();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //----------------------------------------------------------------
    public static Runnable runnableLock(Runnable runnableTask) {
        return runnableLock(new ReentrantLock(), false, runnableTask, null, null);
    }

    public static Runnable runnableLock(Lock lock, Runnable runnableTask) {
        return runnableLock(lock, false, runnableTask, null, null);
    }

    public static Runnable runnableLock(Lock lock, Runnable runnableTask, Runnable afterThis) {
        return runnableLock(lock, false, runnableTask, null, afterThis);
    }

    public static Runnable runnableLock(Lock lock, Runnable runnableTask, Runnable beforeThis, Runnable afterThis) {
        return runnableLock(lock, false, runnableTask, beforeThis, afterThis);
    }

    //基本锁结构模板
    public static Runnable runnableLock(Lock lock, boolean isFair, Runnable runnableTask, Runnable beforeThis, Runnable afterThis) {
        if (lock == null) lock = new ReentrantLock(isFair);
        Lock finalLock = lock;
        return () -> {
            finalLock.lock();
            try {
                if (beforeThis != null) beforeThis.run();
                if (runnableTask != null) runnableTask.run();
                if (afterThis != null) afterThis.run();
            } finally {
                finalLock.unlock();
            }
        };
    }


    public static <T> Callable<T> callableLock(Callable<T> callableTask) {
        return callableLock(new ReentrantLock(), false, callableTask, null, null);
    }

    public static <T> Callable<T> callableLock(Lock lock, Callable<T> callableTask) {
        return callableLock(new ReentrantLock(), false, callableTask, null, null);
    }

    public static <T> Callable<T> callableLock(Lock lock, Callable<T> callableTask, Runnable afterThis) {
        return callableLock(new ReentrantLock(), false, callableTask, null, afterThis);
    }

    public static <T> Callable<T> callableLock(Lock lock, Callable<T> callableTask, Runnable beforeThis, Runnable afterThis) {
        return callableLock(lock, false, callableTask, beforeThis, afterThis);
    }

    //基本锁Callable模板
    public static <T> Callable<T> callableLock(Lock lock, boolean isFair, Callable<T> callableTask, Runnable beforeThis, Runnable afterThis) {
        if (lock == null) lock = new ReentrantLock(isFair);
        Callable<T> lockCallable = XTRunCallUtil.getTryCallable(callableTask, lock::lock, lock::unlock);
        return XTRunCallUtil.getTryCallable(lockCallable, beforeThis, afterThis);
    }
}
