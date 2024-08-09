package top.cutexingluo.tools.utils.ee.web.limit.submit.base;

import top.cutexingluo.tools.utils.ee.web.limit.LimitKey;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.KeyStrategy;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.RequestLimitHandler;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.impl.RateLimitStrategy;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.impl.RedisLimitStrategy;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 接口防抖注解
 * <p>1.防止重复提交</p>
 * <p>2.基于任意策略，可自行实现策略 {@link LimitStrategy}</p>
 * <p><b>这里提供两种使用方式</b></p>
 * <p>1. {@link RequestLimitConfig} 配合处理器 {@link RequestLimitHandler} 进行编程式使用</p>
 * <p>2. 提供注解 {@link RequestLimit} 并开启配置 {@code xingtools.enabled.request-limit=true} 进行声明式使用</p>
 * <p>3. 为了稳定性不提供类上注解，请使用拦截器。如有需要，请反馈</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/5 15:57
 * @since 1.0.4
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestLimit {

    /**
     * <p>是否不使用类上注解的配置</p>
     * <p>false，不设置的项 (即默认值)  会被类上注解的配置覆盖</p>
     * <p>true,   始终使用当前配置</p>
     */
    boolean useSelf() default false;

    /**
     * key 前缀
     * <p>key 最终结果是 prefix + keyStrategy </p>
     *
     * @return 默认为空
     */
    String prefix() default "";

    /**
     * key 字符串添加策略
     *
     * @return 注解默认四种策略组合
     * @see KeyStrategy
     */
    int keyStrategy() default KeyStrategy.IP | KeyStrategy.HTTP_METHOD | KeyStrategy.HTTP_URI | KeyStrategy.KEY_ANNO;


    /**
     * key 值
     * <p>默认为空</p>
     * <p>若不为空，则会覆盖keyStrategy生成的key , 最终结果是 prefix + key</p>
     * <p>使用精确匹配 ${} 符号进行匹配，例如 "${ip}" , 同时需要设置 keyStrategy 必须拥有 ip 策略</p>
     *
     * @return key 值
     * @see KeyStrategy
     */
    String key() default "";

    /**
     * timeout 时间内允许的次数
     *
     * @return 默认1次 / timeout
     */
    long maxCount() default 1;

    /**
     * <p>时间间隔</p>
     * <p>这里有两种说法</p>
     * <p>1. key 过期时间</p>
     * <p>2. key 刷新的间隔</p>
     * <p>如果使用的是提供的 rate 令牌桶策略, 是按照QPS 计算的，所以此项必须为1</p>
     *
     * @return 默认1 timeUnit
     */
    long timeout() default 1;


    /**
     * 获取允许权限过程等待时间
     * <p>提供的 redis 策略无效果，所以此项必须为0</p>
     * <p>提供的 rate 策略为令牌桶获取时间间隔</p>
     *
     * <p> the maximum time to wait for the permit </p>
     *
     * @return 默认 0 timeUnit
     */
    long waitTime() default 0;


    /**
     * timeout 和 waitTime 时间单位
     *
     * @return 默认单位为秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;


    /**
     * 是否开启防抖动
     * <p>开启则为防抖模式，如果在timeout内，则会刷新 timeout 重新开始计时</p>
     * <p>关闭则为节流模式</p>
     *
     * @return 默认关闭
     */
    boolean debounce() default false;

    /**
     * aop 切面策略
     * <p>提供 redis 策略 {@link RedisLimitStrategy}, 需打开 {@code xingtools.enabled.request-limit-redis=true} , 或者自行注册容器</p>
     * <p>提供 rate 策略 {@link RateLimitStrategy}, 需自行注册容器</p>
     *
     * @return 默认为Redis 策略
     */
    Class<? extends LimitStrategy> strategy() default RedisLimitStrategy.class;


    /**
     * strategy 是否通过 spring 容器 注入
     * <p>默认 true</p>
     * <p>true ==> 先从 spring 容器获取 strategy 指定的类对象，如果没有则从反射获取实例</p>
     * <p>false ==> 直接反射获取实例</p>
     */
    boolean needSpring() default true;

    /**
     * key 分隔符
     *
     * @return 分隔符
     */
    String delimiter() default "&";

    /**
     * 方法未执行返回提示信息
     * <p>未通过提示信息</p>
     */
    String msg() default LimitKey.QUICK_MSG;

}
