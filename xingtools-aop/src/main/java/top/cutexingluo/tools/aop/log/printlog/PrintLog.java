package top.cutexingluo.tools.aop.log.printlog;


import org.springframework.core.annotation.AliasFor;
import top.cutexingluo.tools.utils.log.LogType;

import java.lang.annotation.*;

/**
 * AOP 方式 输出字符串，可进行自定义
 * <p>默认开启，可通过配置手动关闭</p>
 * <p>MethodLog 简易版本</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2022/11/22 19:27
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrintLog {
    /**
     * 方法之前输出
     *
     * @return {@link String}
     */
    @AliasFor("value")
    String before() default "";

    @AliasFor("before")
    String value() default "";


    /**
     * 方法之后输出
     *
     * @return {@link String}
     */
    String after() default "";


    /**
     * key , 标识符，可用于Adapter 扩展
     *
     * @return {@link String}
     */
    String key() default "";

    /**
     * num , int类型标识符，可用于Adapter 扩展
     *
     * @return int
     */
    int num() default 0;

    LogType type() default LogType.System;
}
