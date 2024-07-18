package top.cutexingluo.tools.utils.ee.feign.retry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * feign 延时
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 15:47
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Backoff {
    /**
     * ‘延时
     */
    long delay() default 1000L;

    /**
     * 最大延时
     */
    long maxDelay() default 0L;

    /**
     * 次数
     */
    double multiplier() default 0.0D;
}
