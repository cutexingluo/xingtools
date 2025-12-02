package top.cutexingluo.tools.designtools.juc.lock.handler;

import org.jetbrains.annotations.NotNull;
import org.redisson.api.RedissonClient;
import top.cutexingluo.core.basepackage.struct.ExtInitializable;
import top.cutexingluo.core.designtools.juc.lock.extra.XTLockMeta;
import top.cutexingluo.tools.utils.spring.SpringUtils;

/**
 * LockHandler 扩展类
 * <p>继承 {@link XTLockHandler}</p>
 * <p>需要导入  org.redisson.api.RedissonClient 包</p>
 * <p>扩展：基于Spring 可以自动从容器获取 RedissonClient</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 9:38
 * @since 1.1.1
 */

public class XTExtLockHandler extends XTLockHandler implements ExtInitializable<XTExtLockHandler> {


    public XTExtLockHandler(XTLockMeta lockMeta) {
        super(lockMeta);
    }

    public XTExtLockHandler(XTLockMeta lockMeta, RedissonClient redissonClient) {
        super(lockMeta, redissonClient);
    }

    public XTExtLockHandler(XTLockMeta lockMeta, RedissonClient redissonClient, boolean useRedissonClient) {
        super(lockMeta, redissonClient, useRedissonClient);
    }

    public XTExtLockHandler(@NotNull XTLockHandlerMeta handlerMeta) {
        super(handlerMeta);
    }

    public XTExtLockHandler(XTLockMeta lockMeta, boolean openRedissonClient) {
        super(lockMeta);
        this.useRedissonClient = openRedissonClient;
    }

    /**
     * 自动获取RedissonClient 对象
     * <p>可以在执行 decorate 或 lock 方法前需手动调用此方法</p>
     */
    @Override
    public XTExtLockHandler initSelf() {
        if (useRedissonClient && redissonClient == null) {
            redissonClient = SpringUtils.getBean(RedissonClient.class);
//            if (redissonClient == null) { // auto check
//                throw new IllegalStateException("RedissonClient is null !!!");
//            }
        }
        return this;
    }
}
