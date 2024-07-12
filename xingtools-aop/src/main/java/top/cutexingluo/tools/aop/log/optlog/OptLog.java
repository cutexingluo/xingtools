package top.cutexingluo.tools.aop.log.optlog;


import org.springframework.core.annotation.AliasFor;
import top.cutexingluo.tools.utils.log.LogType;

import java.lang.annotation.*;

/**
 * AOP 方式 对方法进行拦截操作
 * <p>
 * 使用方法：
 *     <ul>
 *         <li>开启配置 xingtools.enabled  </li>
 *         <li>方法上使用注解 @OptLog(key="")</li>
 *         <li>然后实现 OptLogAdapter 并注入容器，即可启动</li>
 *     </ul>
 * </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2022/11/22 19:27
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptLog {
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
     * key , 标识符，可用于 OptLogAdapter 扩展
     *
     * @return {@link String}
     */
    String key() default "";

    /**
     * num , int类型标识符，可用于 OptLogAdapter 扩展
     *
     * @return int
     */
    int num() default 0;

    LogType type() default LogType.System;
}
