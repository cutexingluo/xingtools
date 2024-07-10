package top.cutexingluo.tools.designtools.juc.lock;

import java.util.concurrent.atomic.AtomicReference;


/**
 * 自旋锁，不建议使用
 *
 * @author XingTian
 * @date 2023/10/16
 */
public class XTSpinLock {
    private AtomicReference<Thread> spinLock;

    XTSpinLock() {
        spinLock = new AtomicReference<>();
    }

    public void lock() {
        Thread thread = Thread.currentThread();
        while (!spinLock.compareAndSet(null, thread)) ;
    }

    public void unlock() {
        Thread thread = Thread.currentThread();
        spinLock.compareAndSet(thread, null);
    }

    public Runnable getLockRunnable(Runnable runnable) {
        return () -> {
            lock();
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        };
    }
}
