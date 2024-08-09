package top.cutexingluo.tools.utils.ee.web.limit.easylimit;

import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimit;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.RequestLimitHandler;

import java.lang.annotation.*;

/**
 * Redis 限流注解
 * <p>1.如果在拦截器使用工具类方法，则使用AccessLimit</p>
 * <p>若选择这种方式(编程式)，则调用AccessLimitUtil的方法</p>
 * <p>2.如果想自动拦截Aop, 则使用Limit注解 或者 ruoyi 的RateLimiter注解</p>
 * <p>若开启该aop则需要开启EnableXingToolsCloudServer，并开启相应配置</p>
 *
 * <p>1.0.4 及以后版本推荐使用 {@link RequestLimit} 注解 或 {@link RequestLimitHandler} 编程式工具</p>
 *
 * @author ican
 * @author XingTian
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {

    /**
     * 限制周期(秒)
     */
    int seconds();

    /**
     * 规定周期内限制次数
     */
    int maxCount();

    /**
     * 触发限制时的消息提示
     */
    String msg() default "操作过于频繁请稍后再试";

}
