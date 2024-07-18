package top.cutexingluo.tools.aop.transactional.async;

import java.lang.annotation.*;

/**
 * 子线程
 * <p>子线程最后一个参数必须为当前线程</p>
 * <p>需要配合@Async @Transactional 注解使用</p>
 * <p>事务版本(旧版)，策略为Step，建议用 @SonThread </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/9/18 15:30
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SonTransaction {
    String value() default "";
}