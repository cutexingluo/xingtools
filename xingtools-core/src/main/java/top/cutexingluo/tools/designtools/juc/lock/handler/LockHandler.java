package top.cutexingluo.tools.designtools.juc.lock.handler;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.base.Clearable;
import top.cutexingluo.tools.basepackage.base.Initializable;
import top.cutexingluo.tools.basepackage.basehandler.CallableHandler;
import top.cutexingluo.tools.common.data.Entry;
import top.cutexingluo.tools.designtools.juc.lock.extra.XTLockMeta;
import top.cutexingluo.tools.designtools.juc.lock.extra.XTLockType;
import top.cutexingluo.tools.designtools.juc.lock.extra.XTLockTypeUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;

/**
 * 锁处理器
 * <p> 原 XTLockHandler 和 XTAopLockHandler 合并版本后提出 RedissonClient 的原始版本</p>
 * <p>v1.0.2 -> v1.1.1 翻新</p>
 * <p>推荐使用</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 10:04
 * @since v1.0.1
 */
@Data
@Accessors(chain = true)
public class LockHandler implements CallableHandler, Initializable, Clearable {

    /**
     * 锁参数
     */
    @NotNull
    protected XTLockMeta lockMeta;

    /**
     * 锁对象
     */
    protected Lock lock;

    /**
     * 读写锁对象
     */
    protected ReadWriteLock readWriteLock;

    /**
     * 是否初始化
     */
    protected boolean initialized = false;

    /**
     * 默认本地锁
     */
    public LockHandler(@NotNull XTLockMeta lockMeta) {
        this.lockMeta = lockMeta;
    }

    @Override
    public <T> Callable<T> decorate(Callable<T> callable) {
        init();
        return () -> lock(callable);
    }

    /**
     * 直接校验转化类型
     */
    @Override
    public void init() {
        if (!initialized) {
            XTLockType lockType = XTLockTypeUtil.toReentrant(lockMeta.getLockType());
            lockMeta.setLockType(lockType);
            initLock(); // 初始化锁
            initialized = true;
        }
    }

    /**
     * 清除里面的锁，并重置初始化
     */
    @Override
    public void clear() {
        lock = null;
        readWriteLock = null;
        initialized = false;
    }

    /**
     * 单独提出初始化方法
     * <p>不建议直接使用该方法，建议调用 init 方法</p>
     *
     * @since 1.1.4
     */
    protected void initLock() {
//        Lock lock;
//        ReadWriteLock readWriteLock;
//        String lockName = lockMeta.getName();
        switch (lockMeta.getLockType()) {
            case NonLock:
                break;
            case ReentrantReadLock:
                readWriteLock = new ReentrantReadWriteLock(lockMeta.isFair());
                lock = readWriteLock.readLock();
                break;
            case ReentrantWriteLock:
                readWriteLock = new ReentrantReadWriteLock(lockMeta.isFair());
                lock = readWriteLock.writeLock();
                break;
            default: //ReentrantLock
                lock = new ReentrantLock(lockMeta.isFair());
                break;
        }
    }

    /**
     * 加锁方法
     *
     * @param task 任务
     * @return 任务执行返回值
     * @throws Exception InterruptedException 或 task 抛出的 Exception
     */
    public <T> T lock(Callable<T> task) throws Exception {
        return lockTask(task).getValue();
    }

    /**
     * 加锁方法
     *
     * @param task 任务
     * @return 任务执行返回值
     */
    public <T> T lock(Callable<T> task, BiConsumer<Boolean, Exception> inCatchHandler) {
        Entry<Boolean, T> ret = null;
        try {
            ret = lockTask(task);
            if (inCatchHandler != null) inCatchHandler.accept(ret.getKey(), null);
        } catch (Exception e) {
            if (inCatchHandler != null) inCatchHandler.accept(false, e);
        }
        return ret == null ? null : ret.getValue();
    }

    /**
     * 核心加锁任务方法
     *
     * @param task 任务
     * @return 任务执行返回值
     * @throws Exception InterruptedException 或 task 抛出的 Exception
     */
    @NotNull
    public <T> Entry<Boolean, T> lockTask(Callable<T> task) throws Exception {
        if (lock == null) {
            return new Entry<>(false, task.call());
        }
        // 是否获取了锁
        boolean tryLock;
        T result = null;
        if (lockMeta.getTryTimeout() != -1) {
            try {
                tryLock = lock.tryLock(lockMeta.getTryTimeout(), TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw e; // throw exception
            }
        } else {
            lock.lock();
            tryLock = true;
        }
        try { // 进行到这里一定获取到了锁
            if (task != null) result = task.call();
//            result=joinPoint.proceed();
        } finally {
            if (tryLock) lock.unlock();
        }
        return new Entry<>(tryLock, result);
    }


    /**
     * 核心加锁任务方法
     *
     * <p>直接加锁方法</p>
     *
     * @param task 任务
     * @return 是否进行了加锁, 任务执行返回值, 二元组对象
     * @throws Exception InterruptedException 或 task 抛出的 Exception
     */
    @NotNull
    public <T> Entry<Boolean, T> directLockTask(Callable<T> task) throws Exception {
        // 是否加锁
        if (lock == null) {
            return new Entry<>(false, task.call());
        }

        T result = null;
        lock.lock();
        try { // 进行到这里一定获取到了锁
            if (task != null) result = task.call();
        } catch (Exception e) {
            throw e; // throw exception
        } finally {
            lock.unlock();
        }
        return new Entry<>(true, result);
    }

    /**
     * 核心加锁任务方法
     *
     * <p>try 加锁方法</p>
     *
     * @param task 任务
     * @return 是否获取了锁, 任务执行返回值, 二元组对象
     * @throws Exception InterruptedException 或 task 抛出的 Exception
     */
    @NotNull
    public <T> Entry<Boolean, T> tryLockTask(Callable<T> task) throws Exception {
        if (lock == null) {
            return new Entry<>(false, task.call());
        }
        // 是否获取了锁
        boolean tryLock = false;
        T result = null;
        if (lockMeta.getTryTimeout() != -1) {
            tryLock = lock.tryLock(lockMeta.getTryTimeout(), TimeUnit.SECONDS);
            try { // 进行到这里一定获取到了锁
                if (task != null) result = task.call();
//            result=joinPoint.proceed();
            } finally {
                if (tryLock) lock.unlock();
            }
        }
        return new Entry<>(tryLock, result);
    }


}
