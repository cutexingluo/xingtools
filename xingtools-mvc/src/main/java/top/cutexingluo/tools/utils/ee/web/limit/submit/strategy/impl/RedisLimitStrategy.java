package top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import top.cutexingluo.tools.utils.ee.redis.RYRedisCache;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitConfig;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy;
import top.cutexingluo.tools.utils.spring.SpringUtils;

/**
 * <b>Redis 限制策略</b>
 * <p>waitTime 必须为0</p>
 * <p>1. 可自行注入到 Spring 容器，也可以开启 request-limit-redis 配置</p>
 * <p>2. 需要开启 redis 相关配置或自行导入 RedisTemplate </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/5 22:30
 * @since 1.0.4
 */
public class RedisLimitStrategy implements LimitStrategy {


    protected RYRedisCache redisCache;
    protected RedisTemplate<String, Object> redisTemplate;


    public RedisLimitStrategy() {
        this(SpringUtils.getBeanNoExc(RYRedisCache.class), SpringUtils.getBeanNoExc("redisTemplate", RedisTemplate.class));
    }

    @Autowired
    public RedisLimitStrategy(RYRedisCache redisCache, RedisTemplate<String, Object> redisTemplate) {
        this.redisCache = redisCache;
        this.redisTemplate = redisTemplate;
        if (this.redisCache == null && this.redisTemplate == null) {
            throw new IllegalArgumentException("RYRedisCache and RedisTemplate cannot be null at the same time ! please check the configuration of the redis ! 请检查 Redis 配置");
        }
        if (this.redisCache == null) {
            this.redisCache = new RYRedisCache(this.redisTemplate);
        }
    }

    @Override
    public boolean checkRequestLimit(@NotNull RequestLimitConfig requestLimitConfig) throws IllegalArgumentException {
        boolean checkRequestLimit = LimitStrategy.super.checkRequestLimit(requestLimitConfig);
        if (!checkRequestLimit) {
            return false;
        }
        if (requestLimitConfig.getWaitTime() > 0) {
            throw new IllegalArgumentException("the RedisLimitStrategy need waitTime must be equal to 0.");
        }
        return true;
    }


    @Override
    public boolean interceptor(@NotNull RequestLimitConfig requestLimitConfig, @NotNull String key) {
        if (!redisCache.isExpire(key)) {
            long count = redisCache.getCacheLong(key);
            if (count >= requestLimitConfig.getMaxCount()) {
                return false;
            }
            if (requestLimitConfig.isDebounce()) { // debounce
                redisCache.setCacheObjectIfPresent(key, count + 1L, requestLimitConfig.getTimeout(), requestLimitConfig.getTimeUnit());
            } else {
                redisCache.incrementCacheValue(key, 1L);
            }
        } else {
            redisCache.setCacheObjectIfAbsent(key, 1L, requestLimitConfig.getTimeout(), requestLimitConfig.getTimeUnit());
        }
        return true;
    }
}
