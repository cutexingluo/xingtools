package top.cutexingluo.tools.designtools.juc.lock.extra;

/**
 * 锁类型
 */
public enum XTLockType {
    /**
     * 无锁
     */
    NonLock, // 无锁
    /**
     * 本地可重入锁
     */
    ReentrantLock, // 本地可重入锁
    /**
     * 本地可重入读锁
     */
    ReentrantReadLock, // 本地可重入读锁
    /**
     * 本地可重入写锁
     */
    ReentrantWriteLock, // 本地可重入写锁

    /**
     * 分布式普通锁
     * <p>需要导入Redisson 依赖</p>
     */
    RLock, //分布式普通锁
    /**
     * 分布式读锁
     * <p>需要导入Redisson 依赖</p>
     */
    RReadLock, //分布式读锁
    /**
     * 分布式写锁
     * <p>需要导入Redisson 依赖</p>
     */
    RWriteLock, //分布式写锁


//    ReentrantReadLock, //不支持注解
//    ReentrantWriteLock,
//    StaticLock,

}
