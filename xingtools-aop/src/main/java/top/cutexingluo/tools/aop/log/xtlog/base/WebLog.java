package top.cutexingluo.tools.aop.log.xtlog.base;

import org.springframework.core.annotation.AliasFor;
import top.cutexingluo.tools.aop.log.systemlog.XTSystemLog;
import top.cutexingluo.tools.aop.log.xtlog.pkg.WebLogHandler;
import top.cutexingluo.tools.aop.log.xtlog.pkg.WebLogKeyMap;
import top.cutexingluo.tools.aop.log.xtlog.strategy.WebLogFactory;
import top.cutexingluo.tools.aop.log.xtlog.strategy.WebLogStrategy;
import top.cutexingluo.tools.aop.log.xtlog.strategy.impl.DefaultWebLogStrategy;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.utils.log.LogLevel;
import top.cutexingluo.tools.utils.log.LogPkg;
import top.cutexingluo.tools.utils.log.pkg.ILogProvider;
import top.cutexingluo.tools.utils.log.pkg.LogSlf4j;

import java.lang.annotation.*;

/**
 * <p><b>WebLog 注解</b> 提供了策略型打印功能</p>
 * <p>提供自定义功能，能够提高灵活性</p>
 * <p>推荐使用该注解或者 {@link WebLogHandler}</p>
 * <p>默认方法上注解会被类上的注解覆盖</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/8 11:51
 * @since 1.0.4
 */
@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebLog {

    /**
     * <p>是否不使用类上注解的配置</p>
     * <p>false，不设置的项 (即默认值)  会被类上注解的配置覆盖</p>
     * <p>true,   始终使用当前配置</p>
     */
    boolean useSelf() default false;

    /**
     * 标识符，用于strategy, 可自定义操作
     */
    int index() default 0;

    /**
     * 标识符，用于strategy, 可自定义操作
     */
    String key() default "";

    /**
     * 日志级别
     * <p>默认无日志级别</p>
     * <p>优先级比 {@code levelStr} 高</p>
     * <p>使用方式: {@code LogLevel.DEBUG | LogLevel.INFO} , 按低位到高位顺序依次打印 </p>
     * <p>如需按从左到右顺序打印使用 levelStr , 并且需要把该项设为0 </p>
     *
     * @see LogLevel
     */
    int level() default LogLevel.NONE;

    /**
     * 日志级别
     * <p>1. 优先级比 {@code level} 低, 在 level 打印之后打印</p>
     * <p>2. 使用方式: {@code "error|debug|INFO"} , 不区分大小写，从左往右依次打印，比 level 灵活</p>
     */
    String levelStr() default "";


    /**
     * 匹配打印模式
     * <p>使用方式 {@code "ip+httpMethod:args"} (+组装key , : 换行)</p>
     * <p>可以实现 strategy 增加修改 key-value 对</p>
     * <p>会被 value 或 msg 覆盖</p>
     *
     * @see WebLogKeyMap
     * @see top.cutexingluo.tools.aop.log.xtlog.LogKey
     * @see top.cutexingluo.tools.aop.log.xtlog.LogVarKey
     */
    String match() default "";

    /**
     * 详见 {@code msg}
     */
    @AliasFor("msg")
    String value() default "";

    /**
     * 精确打印模式
     * <p>使用方式 {@code "IP地址:    ${ip}, 参数:  ${args}"} (常规模式串 ${key}进行替换) 也可直接使用常量 {@code LogKey.ALL}</p>
     * <p>可以实现 strategy 增加修改 key-value 对</p>
     * <p>会覆盖 match</p>
     *
     * @see WebLogKeyMap
     * @see top.cutexingluo.tools.aop.log.xtlog.LogKey
     * @see top.cutexingluo.tools.aop.log.xtlog.LogVarKey
     */
    @AliasFor("value")
    String msg() default "";

    /**
     * SpEL打印模式
     * <p>SpEL 表达式打印，方法返回值必须为 String.class </p>
     * <p>1. 该项会覆盖msg, 并且 keyMap 设置的值会失效</p>
     * <p>2. 使用bean调用, 例如 {@code  "@testService.print( #msgMap, 'hello world')"}   其中
     * msgMap 为 {@link WebLogKeyMap}对象</p>
     * <p>3. rootObject 为 切点对象(可以直接调用当前类所在的方法)，若为空则为 strategy() 对象</p>
     * <p>4. 提供三种基本变量 {@link WebLogKeyMap} #msgMap,  {@link WebLogConfig} #config , {@link AspectBundle} #bundle  </p>
     *
     * @see top.cutexingluo.tools.utils.se.obj.spel.SpELObject
     */
    String spEL() default "";

    /**
     * 业务名称
     * <p>keyMap 的 key = bName, 匹配字段为 {@code {bName} }</p>
     * <p>兼容 {@link XTSystemLog}</p>
     */
    String businessName() default "";

    /**
     * <b>引用名称, 用于获取 {@link WebLogStrategy}</b>
     * <p>1. 该字段作为 Spring {@link WebLogFactory} 中获取 {@link WebLogStrategy} 的依据</p>
     * <p>2. 该字段作为 Spring beans 中获取 {@link WebLogStrategy} 的依据</p>
     *
     * <p>详细顺序请见 {@link WebLogProcessor} 的 getWebLogStrategy 方法</p>
     *
     * @see WebLogProcessor
     */
    String referer() default "";

    /**
     * 策略实例类型
     * <p>如果 referer 存在 ，则会被 {@code referer} 覆盖</p>
     * <p>详细顺序请见 {@link WebLogProcessor} 的 getWebLogStrategy 方法</p>
     *
     * @see WebLogProcessor
     */
    Class<? extends WebLogStrategy> strategy() default DefaultWebLogStrategy.class;


    /**
     * 日志种类
     * <p>日志框架包 例如 slf4j , log4j , log4j2</p>
     * <p>如果为0, 则会查找spring bean</p>
     *
     * @see LogPkg
     */
    int logPkg() default LogPkg.SLF4J;

    /**
     * 日志种类 class
     * <p>如果 logPkg 项等于0</p>
     * <p>needSpring  == true ，先从Spring 查找，如果没有则会反射获取实例</p>
     * <p>needSpring  == false,  反射获取实例 </p>
     * <p>默认使用 {@link LogSlf4j}</p>
     */
    Class<? extends ILogProvider<?>> logProviderClass() default LogSlf4j.class;


    /**
     * <p>1.是否需要 Spring 容器中获取 WebLogStrategy 实例</p>
     * <p>2.是否需要 Spring 容器中获取 ILogProvider 实例</p>
     * <p>默认开启</p>
     *
     * @see WebLogProcessor
     */
    boolean needSpring() default true;

    /**
     * 是否在方法之前执行
     * <p>默认 true</p>
     * <p>false -> 在方法之后执行</p>
     */
    boolean before() default true;

    /**
     * 展示返回数据
     * <p>不使用=> ""  , 开启 => "${ret}"</p>
     * <p>使用的是精确匹配 ${ret}</p>
     * <p>before = true    方法结束  将结果赋给 {@code "ret"} , 然后该项通过覆盖 msg ,然后再次调用 strategy 打印新的 log 数据 </p>
     * <p>before = false   方法结束 将结果赋给 {@code "ret"} ,   ret 字段进入 msg 或者 match 打印 log 数据</p>
     */
    String ret() default "";
}
