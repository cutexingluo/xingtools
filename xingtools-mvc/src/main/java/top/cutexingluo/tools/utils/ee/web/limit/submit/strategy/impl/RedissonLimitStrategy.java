package top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.impl;

import org.jetbrains.annotations.NotNull;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitConfig;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy;

import java.util.Map;

/**
 * Redisson限流策略
 * <p>需要导入 org.redisson:redisson 包</p>
 *
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/16 21:36
 */
public class RedissonLimitStrategy implements LimitStrategy {

    protected RedissonClient redissonClient;

    @Autowired
    public RedissonLimitStrategy(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public static final String LOCK_KEY = "lock";
    public static final String IS_LOCK_KEY = "isLock";

    @Override
    public boolean interceptor(@NotNull RequestLimitConfig requestLimitConfig, @NotNull String key) {
        RLock lock = redissonClient.getLock(key);
        boolean isLocked = false;
        try {
            //尝试抢占锁
            isLocked = lock.tryLock();
            //没有拿到锁说明已经有了请求了
            if (!isLocked) {
                return false;
            }
            //拿到锁后设置过期时间
            lock.lock(requestLimitConfig.getTimeout(), requestLimitConfig.getTimeUnit());
        } catch (Exception e) {
            return false;
        } finally {
            requestLimitConfig.getContext().put(IS_LOCK_KEY, isLocked);
            requestLimitConfig.getContext().put(LOCK_KEY, lock);
        }
        return true;
    }

    @Override
    public void afterDone(@NotNull RequestLimitConfig requestLimitConfig, @NotNull AspectBundle bundle) {
        Map<String, Object> context = requestLimitConfig.getContext();
        if (context.containsKey(IS_LOCK_KEY) && context.containsKey(LOCK_KEY)) {
            boolean isLocked = (boolean) context.get(IS_LOCK_KEY);
            RLock lock = (RLock) context.get(LOCK_KEY);
            //释放锁
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
