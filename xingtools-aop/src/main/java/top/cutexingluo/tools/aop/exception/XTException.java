package top.cutexingluo.tools.aop.exception;


import org.springframework.core.annotation.AliasFor;
import top.cutexingluo.tools.utils.log.LogType;

import java.lang.annotation.*;

/**
 * 异常，非业务型异常
 *
 * @author XingTian
 * 版本v1.0.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XTException {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    @AliasFor("desc")
    String des() default "error";

    @AliasFor("des")
    String desc() default "error";

    //是否输出系统错误信息
    @AliasFor("printStackTrace")
    boolean wrong() default false;

    @AliasFor("wrong")
    boolean printStackTrace() default false;

    /**
     * 日志输出类型
     *
     * @return {@link LogType}
     */
    LogType logType() default LogType.Error;

}
