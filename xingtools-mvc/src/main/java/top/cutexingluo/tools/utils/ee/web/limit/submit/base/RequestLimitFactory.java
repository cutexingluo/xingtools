package top.cutexingluo.tools.utils.ee.web.limit.submit.base;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.utils.se.obj.ChooseUtil;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.KeyStrategy;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.impl.RedisLimitStrategy;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * RequestLimitConfig 配置工厂
 *
 * <p>由于RequestLimitConfig 类过于重, 故移植在该类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/10 10:39
 * @since 1.1.4
 */
@Data
public class RequestLimitFactory {

    protected RequestLimit requestLimit;

    public RequestLimitFactory(RequestLimit requestLimit) {
        this.requestLimit = requestLimit;
    }


    /**
     * 从注解获取RequestLimitConfig
     */
    public RequestLimitConfig getRequestLimitConfig() {
        return getRequestLimitConfig(RequestLimitProcessor.getLimitStrategy(requestLimit));
    }

    /**
     * 从注解和 LimitStrategy 获取RequestLimitConfig
     */
    public RequestLimitConfig getRequestLimitConfig(@NotNull LimitStrategy strategy) {
        RequestLimitConfig config = new RequestLimitConfig(strategy);
        config.setPrefix(requestLimit.prefix());
        config.setKeyStrategy(requestLimit.keyStrategy());
        config.setKey(requestLimit.key());
        config.setMaxCount(requestLimit.maxCount());
        config.setTimeout(requestLimit.timeout());
        config.setWaitTime(requestLimit.waitTime());
        config.setTimeUnit(requestLimit.timeUnit());
        config.setDebounce(requestLimit.debounce());
        config.setNeedSpring(requestLimit.needSpring());
        config.setDelimiter(requestLimit.delimiter());
        config.setMsg(requestLimit.msg());
        config.setContext(new HashMap<>());
        return config;
    }

    /**
     * 从注解和 RequestLimitSetting 获取RequestLimitConfig
     */
    public RequestLimitConfig getRequestLimitConfig(@NotNull RequestLimitSetting requestLimitSetting) {
        RequestLimitConfig other = requestLimitSetting.getRequestLimitConfig();
        RequestLimitConfig config = new RequestLimitConfig(takeLimitStrategy(other.getStrategy()));
        config.setPrefix(ChooseUtil.checkBlankOverride(requestLimit.prefix(), other.getPrefix()));
        config.setKeyStrategy(ChooseUtil.checkOverride(requestLimit.keyStrategy(), other.getKeyStrategy(), KeyStrategy.IP | KeyStrategy.HTTP_METHOD | KeyStrategy.HTTP_URI));
        config.setKey(ChooseUtil.checkBlankOverride(requestLimit.key(), other.getKey()));
        config.setMaxCount(ChooseUtil.checkOverride(requestLimit.maxCount(), other.getMaxCount(), 1L));
        config.setTimeout(ChooseUtil.checkOverride(requestLimit.timeout(), other.getTimeout(), 1L));
        config.setWaitTime(ChooseUtil.checkOverride(requestLimit.waitTime(), other.getWaitTime(), 0L));
        config.setTimeUnit(ChooseUtil.checkAddrOverride(requestLimit.timeUnit(), other.getTimeUnit(), TimeUnit.SECONDS));
        config.setDebounce(ChooseUtil.checkOverride(requestLimit.debounce(), other.isDebounce(), false));
        config.setNeedSpring(ChooseUtil.checkOverride(requestLimit.needSpring(), other.isNeedSpring(), true));
        config.setDelimiter(ChooseUtil.checkBlankOverride(requestLimit.delimiter(), other.getDelimiter()));
        config.setMsg(ChooseUtil.checkBlankOverride(requestLimit.msg(), other.getMsg()));
        config.setContext(new HashMap<>());
        return config;
    }


    protected LimitStrategy takeLimitStrategy(LimitStrategy strategy) {
        if (strategy != null && requestLimit.strategy() == RedisLimitStrategy.class) {
            return strategy;
        }
        return RequestLimitProcessor.getLimitStrategy(requestLimit);
    }


}
