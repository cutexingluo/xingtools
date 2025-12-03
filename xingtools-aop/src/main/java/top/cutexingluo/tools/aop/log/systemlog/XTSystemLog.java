package top.cutexingluo.tools.aop.log.systemlog;

import org.springframework.core.annotation.AliasFor;
import top.cutexingluo.tools.utils.log.LogType;

import java.lang.annotation.*;

/**
 * 接口调用日志
 * <p>网络接口层面使用 @XTSystemLog -> {@link XTSystemLog} </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/27 9:06
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XTSystemLog {

    /**
     * 业务名称
     *
     * @return {@link String}
     */
    @AliasFor("value")
    String businessName() default "";

    @AliasFor("businessName")
    String value() default "";

    /**
     * 是否展示请求参数
     *
     * @return boolean
     */
    boolean showRequestArgs() default true;

    /**
     * 是否展示返回参数
     *
     * @return boolean
     */
    boolean showResponseArgs() default true;

    /**
     * 日志输出类型
     *
     * @return {@link LogType}
     */
    LogType type() default LogType.Info;


    /**
     * 是否打印分割线
     */
    boolean enableStartEndLine() default true;
}
