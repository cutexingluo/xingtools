package top.cutexingluo.tools.designtools.juc.lock;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 13:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class XTRWLock extends XTLock {
    //    private volatile Map<String, Object> lockMap;
    private ReentrantReadWriteLock rwLock; //重写

    //只支持这个
    public XTRWLock() {
        rwLock = new ReentrantReadWriteLock();
    }

    public XTRWLock(ReentrantReadWriteLock lock) {
        this.rwLock = lock;
    }

    public static XTRWLock newInstance() {
        return new XTRWLock();
    }

    //加读锁
    public Runnable getRLockRunnable(Runnable runnable) {
        ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
        return XTLockUtils.runnableLock(readLock, runnable, readLock::lock, readLock::unlock);
    }

    //加写锁
    public Runnable getWLockRunnable(Runnable runnable) {
        ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
        return XTLockUtils.runnableLock(writeLock, runnable, writeLock::lock, writeLock::unlock);
    }
}
