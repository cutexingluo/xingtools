package top.cutexingluo.tools.designtools.json.serializer;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * json序列化器
 * <p>可继承</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/1 14:34
 * @since 1.0.4
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = StringJsonSerializer.class)
public @interface StrJson {
    /**
     * 自定义转化策略
     */
    @AliasFor("value")
    Class<? extends StrJsonStrategy> strategy() default DefaultStrJsonStrategy.class;


    /**
     * 自定义转化策略
     */
    @AliasFor("strategy")
    Class<? extends StrJsonStrategy> value() default DefaultStrJsonStrategy.class;


    /**
     * 策略名称
     * <p>如果为 "" , 则调用strategy 无参构造</p>
     * <p>如果不为 "", 则调用strategy 带一参构造，参数为该name</p>
     */
    String name() default "";

    /**
     * 过滤存在的字段
     * <p>true --> 如果为 null 则不执行策略</p>
     */
    boolean filterExists() default true;
}
