package top.cutexingluo.tools.utils.ee.feign.retry;

import top.cutexingluo.tools.utils.log.LogType;

import java.lang.annotation.*;

/**
 * feign 重试注解
 * <p>
 *      使用方法：<br>
 *  <code>@FeignRetry(maxAttempt = 6, backoff = @Backoff(delay = 500L, maxDelay = 20000L, multiplier = 4))</code>
 * </p>
 * <p>需要导入 spring-data-redis 包</p>
 * <p>需要导入 spring-retry 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 15:45
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FeignRetry {

    /**
     * 延时策略
     */
    Backoff backoff() default @Backoff();

    /**
     *最大尝试
     */
    int maxAttempt() default 3;

    /**
     *
     * @return 包含的异常
     */
    Class<? extends Throwable>[] include() default {};


    /**
     * 日志级别
     */
    LogType logType() default LogType.Info;
}
