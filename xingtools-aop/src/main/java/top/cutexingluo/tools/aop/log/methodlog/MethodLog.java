package top.cutexingluo.tools.aop.log.methodlog;

import org.springframework.core.annotation.AliasFor;
import top.cutexingluo.tools.aop.log.systemlog.XTSystemLog;
import top.cutexingluo.tools.aop.log.xtlog.base.WebLog;
import top.cutexingluo.tools.utils.log.LogType;

import java.lang.annotation.*;

/**
 * 方法调用日志
 * <p>网络接口层面使用 @XTSystemLog -> {@link XTSystemLog} </p>
 * <p>方法调用层面使用 @MethodLog ->  {@link MethodLog}</p>
 * <p><b>1.0.4 版本后推荐使用综合型注解 {@link WebLog}</b></p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/16 14:47
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodLog {

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
     * Key 值, String类型标识符，可用作 MethodLogAdapter 扩展使用
     *
     * @return {@link String}
     */
    String key() default "";

    /**
     * num , int类型标识符，可用于 MethodLogAdapter 扩展使用
     *
     * @return int
     */
    int num() default 0;


    /**
     * 方法执行前 msg
     *
     * @return {@link String}
     */
    String beforeMsg() default "";


    /**
     * 方法执行后 msg
     *
     * @return {@link String}
     */
    String afterMsg() default "";

    /**
     * 是否显示方法名称
     *
     * @return {@link NameType}
     */
    NameType showName() default NameType.Simple;

    /**
     * 是否展示参数
     *
     * @return boolean
     */
    boolean showArgs() default true;


    /**
     * 是否展示返回数据
     *
     * @return boolean
     */
    boolean showReturn() default true;


    /**
     * 日志输出类型
     *
     * @return {@link LogType}
     */
    LogType type() default LogType.Info;

}
