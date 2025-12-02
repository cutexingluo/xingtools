package top.cutexingluo.tools.designtools.juc.lock.handler;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RedissonClient;
import top.cutexingluo.core.basepackage.basehandler.CallableHandler;
import top.cutexingluo.core.common.result.Constants;
import top.cutexingluo.core.designtools.juc.lock.extra.XTLockMeta;
import top.cutexingluo.core.designtools.juc.lock.handler.LockHandler;
import top.cutexingluo.core.exception.ConfigNullPointerException;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 锁处理器
 * <p> 原 XTLockHandler 和 XTAopLockHandler 合并版本</p>
 * <p>v1.0.2 -> v1.1.1 翻新</p>
 * <p>推荐使用</p>
 * <p>需要导入 org.redisson.api.RedissonClient 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/12 9:53
 * @since 1.1.1
 */
@Accessors(chain = true)
public class XTLockHandler extends LockHandler implements CallableHandler {


    /**
     * RedissonClient 对象
     */
    protected RedissonClient redissonClient;
    /**
     * 是否启用 RedissonClient
     */
    protected boolean useRedissonClient = false;

    /**
     * 默认本地锁
     */
    public XTLockHandler(@NotNull XTLockMeta lockMeta) {
        super(lockMeta);
    }

    /**
     * 使用 RedissonClient
     *
     * <p>useRedissonClient = true, 使用 RedissonClient , 如果不存在会自动从 Spring容器获取</p>
     */
    public XTLockHandler(@NotNull XTLockMeta lockMeta, RedissonClient redissonClient) {
        this(lockMeta, redissonClient, true);
    }

    /**
     * 使用 RedissonClient
     *
     * @param useRedissonClient 使用 RedissonClient , 如果不存在会自动从 Spring容器获取
     */
    public XTLockHandler(@NotNull XTLockMeta lockMeta, RedissonClient redissonClient, boolean useRedissonClient) {
        super(lockMeta);
        this.redissonClient = redissonClient;
        this.useRedissonClient = useRedissonClient;
    }

    /**
     * @param handlerMeta 使用XTLockHandlerMeta包装的数据
     */
    public XTLockHandler(@NotNull XTLockHandlerMeta handlerMeta) {
        this(handlerMeta.getLockMeta(), handlerMeta.getRedissonClient(), handlerMeta.isUseRedissonClient());
    }

    @Override
    public void init() {
        if (!useRedissonClient || redissonClient == null) {// 转化类型
            super.init();
        }
    }


    /**
     * 对加锁任务进行封装
     * <p>自动判断获取和转化 RedissonClient</p>
     */
    @Override
    public <T> Callable<T> decorate(Callable<T> callable) {
        init();
        return () -> lock(callable);
    }

    @Override
    protected void initLock() {
//        Lock lock;
//        ReadWriteLock readWriteLock;
        String lockName = lockMeta.getName();
        switch (lockMeta.getLockType()) {
            case NonLock:
                break;
            case RLock:
                if (redissonClient == null)
                    throw new ConfigNullPointerException(Constants.CODE_500.getCode(), "No  RedissonClient");
                if (StrUtil.isBlank(lockName)) throw new IllegalArgumentException("XTLockMeta don't have name string");
                lock = redissonClient.getLock(lockName);
                break;
            case RReadLock:
                if (redissonClient == null)
                    throw new ConfigNullPointerException(Constants.CODE_500.getCode(), "No  RedissonClient");
                if (StrUtil.isBlank(lockName)) throw new IllegalArgumentException("XTLockMeta don't have name string");
                readWriteLock = redissonClient.getReadWriteLock(lockName);
                lock = readWriteLock.readLock();
                break;
            case RWriteLock:
                if (redissonClient == null)
                    throw new ConfigNullPointerException(Constants.CODE_500.getCode(), "No  RedissonClient");
                if (StrUtil.isBlank(lockName)) throw new IllegalArgumentException("XTLockMeta don't have name string");
                readWriteLock = redissonClient.getReadWriteLock(lockName);
                lock = readWriteLock.writeLock();
                break;
            case ReentrantReadLock:
                readWriteLock = new ReentrantReadWriteLock(lockMeta.isFair());
                lock = readWriteLock.readLock();
            case ReentrantWriteLock:
                readWriteLock = new ReentrantReadWriteLock(lockMeta.isFair());
                lock = readWriteLock.writeLock();
            default: //ReentrantLock
                lock = new ReentrantLock(lockMeta.isFair());
        }
    }


    public RedissonClient getRedissonClient() {
        return redissonClient;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public boolean isUseRedissonClient() {
        return useRedissonClient;
    }

    public void setUseRedissonClient(boolean useRedissonClient) {
        this.useRedissonClient = useRedissonClient;
    }
}
