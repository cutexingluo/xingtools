package top.cutexingluo.tools.utils.ee.web.limit.guava;

import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimit;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.RequestLimitHandler;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解 current limiting
 * <p>1.如果在拦截器使用工具类方法，则使用AccessLimit</p>
 * <p>若选择这种方式(编程式)，则调用AccessLimitUtil方法</p>
 * <p>2.如果想自动拦截Aop, 则使用Limit注解 或者 ruoyi 的RateLimiter注解</p>
 * <p>若开启该aop则需要开启EnableXingToolsCloudServer，并开启相应配置</p>
 *
 * <p>1.0.4 及以后版本推荐使用 {@link RequestLimit} 注解 或 {@link RequestLimitHandler} 编程式工具</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 16:29
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Limit {
    /**
     * 资源的key,唯一
     * 作用：不同的接口，不同的流量控制
     */
    String key() default "";

    /**
     * 最多的访问限制次数
     */
    double permitsPerSecond();

    /**
     * 获取令牌最大等待时间
     */
    long timeout();

    /**
     * 获取令牌最大等待时间,单位(例:分钟/秒/毫秒) 默认:毫秒
     */
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;

    /**
     * 得不到令牌的提示语
     */
    String msg() default "系统繁忙,请稍后再试.";
}
