package top.cutexingluo.tools.designtools.juc.lock.handler;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.base.Initializable;
import top.cutexingluo.tools.basepackage.basehandler.CallableHandler;
import top.cutexingluo.tools.designtools.juc.lock.extra.XTLockMeta;
import top.cutexingluo.tools.designtools.juc.lock.extra.XTLockType;
import top.cutexingluo.tools.designtools.juc.lock.extra.XTLockTypeUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 锁处理器
 * <p> 原 XTLockHandler 和 XTAopLockHandler 合并版本后提出 RedissonClient 的原始版本</p>
 * <p>v1.0.2 -> v1.1.1 翻新</p>
 * <p>推荐使用</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 10:04
 * @since  v1.0.1
 */
@Data
@Accessors(chain = true)
public class LockHandler  implements CallableHandler , Initializable {

    /**
     * 锁参数
     */
    @NotNull
    protected XTLockMeta lockMeta;

    /**
     * 默认本地锁
     */
    public LockHandler(@NotNull XTLockMeta lockMeta) {
        this.lockMeta = lockMeta;
    }

    @Override
    public <T> Callable<T> decorate(Callable<T> callable) {
        init();
        return ()->lock(callable);
    }

    /**
     * 直接校验转化类型
     */
    @Override
    public void init() {
        XTLockType lockType = XTLockTypeUtil.toReentrant(lockMeta.getLockType());
        lockMeta.setLockType(lockType);
    }

    /**
     * 核心加锁任务方法
     *
     * @param task 任务
     * @return  任务执行返回值
     * @throws Exception InterruptedException 或 task 抛出的 Exception
     */
    public <T> T lock(Callable<T> task) throws Exception {
        if (lockMeta.getLockType() == XTLockType.NonLock) {
            return task.call();
        }
        Lock lock;
        ReadWriteLock readWriteLock;
//        String lockName = lockMeta.getName();
        switch (lockMeta.getLockType()) {
            case ReentrantReadLock:
                readWriteLock = new ReentrantReadWriteLock(lockMeta.isFair());
                lock = readWriteLock.readLock();
            case ReentrantWriteLock:
                readWriteLock = new ReentrantReadWriteLock(lockMeta.isFair());
                lock = readWriteLock.writeLock();
            default: //ReentrantLock
                lock = new ReentrantLock(lockMeta.isFair());
        }
        T result = null;
        if (lockMeta.getTryTimeout() != -1) {
            try {
                boolean b = lock.tryLock(lockMeta.getTryTimeout(), TimeUnit.SECONDS);
                if (!b) return null;
            } catch (InterruptedException e) {
                throw  e; // throw exception
            }
        } else lock.lock();
        try { // 进行到这里一定获取到了锁
            if (task != null) result = task.call();
//            result=joinPoint.proceed();
        } finally {
            lock.unlock();
        }
        return result;
    }

}
