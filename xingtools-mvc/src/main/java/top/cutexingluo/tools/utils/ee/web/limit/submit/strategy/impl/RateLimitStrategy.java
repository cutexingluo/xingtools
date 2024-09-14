package top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.impl;

import com.google.common.util.concurrent.RateLimiter;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitData;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>本地 guava 令牌桶 RateLimiter 限制策略</b>
 * <p>timeout 值 必须为 1, 仅需要修改 maxCount值 限制QPS</p>
 * <p>此策略需要自行注册 Bean 到容器</p>
 * <p>需要导入 com.google:guava 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/6 20:04
 * @since 1.0.4
 */
@ConditionalOnClass(RateLimiter.class)
@Data
public class RateLimitStrategy implements LimitStrategy {

    protected Map<String, RateLimiter> limitMap;

    public RateLimitStrategy() {
        this(new ConcurrentHashMap<>());
    }

    @Autowired
    public RateLimitStrategy(Map<String, RateLimiter> limitMap) {
        this.limitMap = limitMap;
    }

    @Override
    public boolean checkRequestLimit(@NotNull RequestLimitData requestLimitData) throws IllegalArgumentException {
        boolean b = LimitStrategy.super.checkRequestLimit(requestLimitData);
        if (b) {
            if (requestLimitData.getTimeout() != 1) {
                throw new IllegalArgumentException("the RateLimitStrategy need timeout must be equal to 1");
            }
        }
        return b;
    }


    @Override
    public boolean interceptor(@NotNull RequestLimitData requestLimitData, @NotNull String key) {
        RateLimiter rateLimiter = null;
        if (!limitMap.containsKey(key)) {
            rateLimiter = RateLimiter.create(requestLimitData.getMaxCount());
            limitMap.put(key, rateLimiter);
        }
        rateLimiter = limitMap.get(key);
        // get one
        boolean acquire = rateLimiter.tryAcquire(requestLimitData.getWaitTime(), requestLimitData.getTimeUnit());
        if (!acquire) {
            return false;
        }
        return true;
    }
}


