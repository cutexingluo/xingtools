package top.cutexingluo.tools.utils.ee.web.limit.submit.base;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 用在参数或者字段属性上，供其他RequestLimit配置解释
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/5 16:19
 * @since 1.0.4
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestLimitKey {


    /**
     * 是否添加进key
     */
    boolean add() default true;

    /**
     * 该字段的 key 名称,
     * <p>如果不设置, 则默认使用该对象</p>
     */
    @AliasFor("value")
    String key() default "";

    /**
     * 该字段的 key 名称,
     * <p>如果不设置, 则默认使用该对象</p>
     */
    @AliasFor("key")
    String value() default "";

    /**
     * 是否进入参数获取其内部字段注解
     * <p>进入该类获取其字段上的该注解</p>
     * <p>为防止死循环, 仅支持一级字段</p>
     */
    boolean inner() default false;
}
