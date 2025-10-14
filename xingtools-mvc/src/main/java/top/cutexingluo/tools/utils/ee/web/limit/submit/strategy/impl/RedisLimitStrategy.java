package top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.impl;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import top.cutexingluo.core.designtools.juc.lock.handler.LockHandler;
import top.cutexingluo.tools.utils.ee.redis.RYRedisCache;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitData;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy;
import top.cutexingluo.tools.utils.spring.SpringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <b>Redis 限制策略</b>
 * <p>waitTime 必须为0</p>
 * <p>1. 可自行注入到 Spring 容器，也可以开启 request-limit-redis 配置</p>
 * <p>2. 需要开启 redis 相关配置或自行导入 RedisTemplate </p>
 *
 * <p>于1.1.4 版本新增lua 脚本方式</p>
 * <p>使用方式</p>
 * <pre>
 *     DefaultRedisScript &lt Long &gt script = XTRedisScript.initLocalRedisLimitScript(); // 加载脚本
 *     RedisLimitStrategy redisLimitStrategy = new RedisLimitStrategy(redisCache);
 *     redisLimitStrategy.setRedisScript(redisScript); // 设置脚本
 *     RequestLimitData limitData = new RequestLimitData();
 *     limitData.setMaxCount(2);
 *     limitData.setTimeout(10L);
 *     redisLimitStrategy.interceptor(requestLimitData, key);
 * </pre>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/5 22:30
 * @since 1.0.4
 */
@Data
public class RedisLimitStrategy implements LimitStrategy {


    protected RYRedisCache redisCache;
    protected RedisTemplate<String, Object> redisTemplate;

    /**
     * redis 脚本
     * <p>执行 interceptor , null  则lockLimit (执行默认lockHandler若为空则accessLimit)  , 非null 执行luaLimit</p>
     */
    protected RedisScript<Long> redisScript;

    /**
     * 锁工具类
     */
    protected LockHandler lockHandler;


    public RedisLimitStrategy() {
        this(SpringUtils.getBeanNoExc(RYRedisCache.class), SpringUtils.getBeanNoExc("redisTemplate", RedisTemplate.class));
    }

    /**
     * 推荐使用
     */
    public RedisLimitStrategy(RYRedisCache redisCache) {
        this.redisCache = redisCache;
        if (this.redisCache == null) {
            throw new IllegalArgumentException("RYRedisCache is null !");
        }
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
    public boolean checkRequestLimit(@NotNull RequestLimitData requestLimitData) throws IllegalArgumentException {
        boolean checkRequestLimit = LimitStrategy.super.checkRequestLimit(requestLimitData);
        if (!checkRequestLimit) {
            return false;
        }
        if (requestLimitData.getWaitTime() > 0) {
            throw new IllegalArgumentException("the RedisLimitStrategy need waitTime must be equal to 0.");
        }
        return true;
    }


    @Override
    public boolean interceptor(@NotNull RequestLimitData requestLimitData, @NotNull String key) throws Exception {
        if (redisScript == null) {
            if (lockHandler == null) {
                return accessLimit(requestLimitData, key);
            } else {
                return lockLimit(requestLimitData, key, lockHandler);
            }
        } else {
            return luaLimit(requestLimitData, Collections.singletonList(key), redisScript);
        }
    }

    /**
     * 直接限制
     * <p>无任何措施</p>
     * <p>1.1.4 之前的版本默认方法</p>
     *
     * @deprecated 操作过多，已废弃，未来移除
     */
    @Deprecated
    public boolean directLimit(@NotNull RequestLimitData requestLimitData, @NotNull String key) {
        if (!redisCache.isExpire(key)) {
            long count = redisCache.getCacheLong(key);
            if (count >= requestLimitData.getMaxCount()) {
                return false;
            }
            if (requestLimitData.isDebounce()) { // debounce
                redisCache.setCacheObjectIfPresent(key, count + 1L, requestLimitData.getTimeout(), requestLimitData.getTimeUnit());
            } else {
                redisCache.incrementCacheValue(key, 1L);
            }
        } else {
            redisCache.setCacheObjectIfAbsent(key, 1L, requestLimitData.getTimeout(), requestLimitData.getTimeUnit());
        }
        return true;
    }

    /**
     * AccessLimitUtil 的 直接限制
     * <p>无任何措施</p>
     * <p>1.1.4 之前的版本 AccessLimitUtil 默认方法</p>
     */
    public boolean accessLimit(@NotNull RequestLimitData requestLimitData, @NotNull String key) {
        boolean result = true;
        Long count = redisCache.incrementCacheValue(key, 1L);
        if (Objects.nonNull(count)) {
            if (requestLimitData.isDebounce() || count == 1) { // 防抖 或者 第一次节流
                redisCache.expire(key, requestLimitData.getTimeout(), TimeUnit.SECONDS);
            }
            if (count > requestLimitData.getMaxCount()) {
                result = false;
            }
        }
        return result;
    }

    /**
     * 加锁限制
     * <p>保证并发安全</p>
     *
     * @param lockHandler 锁工具类, 可继承该类重写方法
     */
    public boolean lockLimit(@NotNull RequestLimitData requestLimitData, @NotNull String key, @NotNull LockHandler lockHandler) throws Exception {
        return lockHandler.lock(() -> accessLimit(requestLimitData, key));
    }

    /**
     * 执行lua 脚本
     *
     * @return 是否通过
     */
    public boolean luaLimit(@NotNull RequestLimitData requestLimitData, @NotNull List<String> keys, @NotNull RedisScript<Long> redisScript) {
        TimeUnit timeUnit = requestLimitData.getTimeUnit();
        long timeout = timeUnit.toSeconds(requestLimitData.getTimeout());
        RedisTemplate<String, Object> redisTemplate = redisCache.get();
        Long count = redisTemplate.execute(redisScript, keys,
                requestLimitData.getMaxCount(), timeout, requestLimitData.isDebounce() ? 1 : 0);
        return count != null && count != 0;
    }
}
