package top.cutexingluo.tools.designtools.juc.lock.extra;

/**
 * 锁类型转换工具
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 18:11
 */
public class XTLockTypeUtil {
    public static XTLockType toR(XTLockType lockType) {
        if (lockType == XTLockType.ReentrantLock) return XTLockType.RLock;
        else if (lockType == XTLockType.ReentrantReadLock) return XTLockType.RReadLock;
        else if (lockType == XTLockType.ReentrantWriteLock) return XTLockType.RWriteLock;
        return lockType;
    }

    public static XTLockType toReentrant(XTLockType lockType) {
        if (lockType == XTLockType.RLock) return XTLockType.ReentrantLock;
        else if (lockType == XTLockType.RReadLock) return XTLockType.ReentrantReadLock;
        else if (lockType == XTLockType.RWriteLock) return XTLockType.ReentrantWriteLock;
        return lockType;
    }
}
