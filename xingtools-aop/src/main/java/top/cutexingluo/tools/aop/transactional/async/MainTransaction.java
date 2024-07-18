package top.cutexingluo.tools.aop.transactional.async;

import java.lang.annotation.*;

/**
 * 主线程
 *
 * <p>事务版本(旧版)，建议用 @MainThread </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/9/18 15:30
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MainTransaction {
    int value();//子线程数量
}