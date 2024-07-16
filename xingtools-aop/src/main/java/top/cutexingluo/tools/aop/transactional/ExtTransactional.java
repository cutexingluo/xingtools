package top.cutexingluo.tools.aop.transactional;

import java.lang.annotation.*;

/**
 * 注意动态代理
 * 事务
 * <p>建议直接使用 @Transactional 注解</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2022/11/22 19:27
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtTransactional {

}
