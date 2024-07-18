package top.cutexingluo.tools.designtools.juc.lock;

import org.springframework.core.annotation.AliasFor;
import top.cutexingluo.tools.designtools.juc.lock.extra.XTLockType;

import java.lang.annotation.*;

/**
 * aop 注解锁
 *
 * @author XingTian
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface XTAopLock {
    XTLockType type() default XTLockType.ReentrantLock;

    /**
     * 锁名称，仅分布式锁
     */
    @AliasFor("value")
    String name() default "";

    /**
     * 锁名称，仅分布式锁
     */
    @AliasFor("name")
    String value() default "";

    /**
     * 是否公平，仅本地锁
     */
    boolean isFair() default false;

    /**
     * 锁类型
     */
    XTLockType lockType() default XTLockType.ReentrantLock;

    // 由于Lock接口没有带 锁多长时间，所以这个只能适用于尝试获取锁。大于0则尝试获取，-1则直接锁
    int tryTimeout() default -1;
}
