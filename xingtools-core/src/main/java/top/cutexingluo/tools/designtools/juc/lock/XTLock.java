package top.cutexingluo.tools.designtools.juc.lock;

import lombok.Data;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * <p>
 * 常用类 XTLock 锁类 ，<br>
 * 主要用于 运行加锁后的可执行接口，获得加锁的可执行接口Runnable和Callable
 * <p>
 *
 * @author XingTian
 * @version 1.0
 * @date 2022-11-22
 */
@Data
public class XTLock {
    protected Lock lock;
    protected Condition condition;
    protected volatile int productNumber = 0;

    //没有用处
//    protected volatile AtomicInteger count = new AtomicInteger(1);

    public XTLock() {
//new ReentrantLock() 默认非公平
        this.lock = new ReentrantLock();
    }

    public XTLock(boolean isFair) {
//new ReentrantLock() 默认非公平
        this.lock = new ReentrantLock(isFair);
    }

    public <L extends Lock> XTLock(L lock) {//new ReentrantLock()
        this.lock = lock;
    }

    public static XTLock getNew() { //获取实例
        return new XTLock();
    }

    //加锁工具
//    @XTAopLock(type = XTLockType.StaticLock)
    //运行加锁后的可执行接口
    public static void doStaticLock(Runnable runnable) { //静态
        XTLockUtils.doLock(runnable);
    }

    //加锁后的可执行接口
    public static Runnable getStaticLockRunnable(Runnable runnable) { //静态
        return XTLockUtils.runnableLock(runnable);
    }

    public static <T> Callable<T> getStaticLockRunnable(Callable<T> callable) { //静态
        return XTLockUtils.callableLock(callable);
    }

    //    @XTAopLock
    //运行加锁后的可执行接口
    public void doLock(Runnable runnable) {
        XTLockUtils.doLock(this.lock, runnable);
    }

    public void doLock(Runnable runnable, Runnable afterThis) {
        XTLockUtils.doLock(this.lock, runnable, afterThis);
    }

    public void doLock(Runnable runnable, Runnable beforeThis, Runnable afterThis) {
        XTLockUtils.doLock(this.lock, runnable, beforeThis, afterThis);
    }


    //获得加锁后的可执行接口
    //常用
    public Runnable getLockRunnable(Runnable runnable) {
        return XTLockUtils.runnableLock(this.lock, runnable);
    }

    //常用
    public <V> Callable<V> getLockCallable(Callable<V> runnable) {
        return XTLockUtils.callableLock(this.lock, runnable);
    }


}
