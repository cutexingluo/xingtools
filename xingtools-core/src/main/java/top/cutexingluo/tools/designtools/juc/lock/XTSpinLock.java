package top.cutexingluo.tools.designtools.juc.lock;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;


/**
 * 自旋锁，不建议使用
 *
 * @author XingTian
 * @date 2023/10/16
 */
@Data
@AllArgsConstructor
public class XTSpinLock {
    private AtomicReference<Thread> spinLock;

    public XTSpinLock(AtomicReference<Thread> spinLock) {
        this.spinLock = spinLock;
    }

    private boolean printTrace = true;
    private Consumer<Exception> exceptionHandler = null;

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
                if (exceptionHandler != null) exceptionHandler.accept(e);
                else if (printTrace) e.printStackTrace();
            } finally {
                unlock();
            }
        };
    }
}
