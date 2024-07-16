package top.cutexingluo.tools.utils.ee.web.limit.submit.strategy;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.common.Constants;
import top.cutexingluo.tools.exception.ServiceException;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitConfig;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.RequestLimitInterceptor;

import java.util.LinkedHashMap;

/**
 * RequestLimit 策略接口
 * <p>通过实现并配置，可以自定义限流策略 (推荐注册到 Spring 容器)</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/5 17:52
 * @since 1.0.4
 */
public interface LimitStrategy {


    /**
     * 校验配置参数
     *
     * @param requestLimitConfig 请求限制配置
     */
    default boolean checkRequestLimit(@NotNull RequestLimitConfig requestLimitConfig) throws IllegalArgumentException {
        if (requestLimitConfig.getTimeout() < 0) {
            throw new IllegalArgumentException("timeout must be greater than or equal to 0.");
        }
        if (requestLimitConfig.getMaxCount() < 0) {
            throw new IllegalArgumentException("maxCount must be greater than or equal to 0.");
        }
        if (requestLimitConfig.getWaitTime() < 0) {
            throw new IllegalArgumentException("waitTime must be greater than or equal to 0.");
        }
        return true;
    }


    /**
     * 获取 key 值
     *
     * @param generatedKey       根据 keyStrategy 生成的 key
     * @param requestLimitConfig 请求限制配置
     * @param bundle             web方法切面配置
     * @return key 最终的 key 值，若为 null 则最终为 generatedKey 默认生成策略
     */
    default String getLimitKey(@NotNull LinkedHashMap<String, String> generatedKey, @NotNull RequestLimitConfig requestLimitConfig, @NotNull AspectBundle bundle) {
        return RequestLimitInterceptor.generateKey(generatedKey, requestLimitConfig.getDelimiter());
    }


    /**
     * <p><b>拦截具体实现方法</b></p>
     * <p>是否放行</p>
     * <p>返回 true 则放行，返回 false 则拦截</p>
     * <p>若返回false, 则调用 refuseException 抛出异常</p>
     *
     * @param requestLimitConfig 请求限制配置
     * @param key                组装后的key
     */
    boolean interceptor(@NotNull RequestLimitConfig requestLimitConfig, @NotNull String key);


    /**
     * 执行完方法后的操作
     *
     * @param requestLimitConfig 请求限制配置
     * @param bundle             web方法切面配置
     */
    default void afterDone(@NotNull RequestLimitConfig requestLimitConfig, @NotNull AspectBundle bundle) {

    }


    /**
     * 拦截后返回的自定义异常
     *
     * @param msg 注解或配置上的异常信息
     */
    default Exception refuseException(@NotNull String msg) {
        return new ServiceException(Constants.CODE_403, msg);
    }
}
