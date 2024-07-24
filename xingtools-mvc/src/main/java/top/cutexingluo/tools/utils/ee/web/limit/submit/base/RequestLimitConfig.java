package top.cutexingluo.tools.utils.ee.web.limit.submit.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.ee.web.limit.LimitKey;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.KeyStrategy;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.RequestLimitHandler;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.impl.RateLimitStrategy;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.impl.RedisLimitStrategy;
import top.cutexingluo.tools.utils.se.obj.ChooseUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 接口防抖配置
 * <p>1.防止重复提交</p>
 * <p>2.基于任意策略</p>
 *
 * <p><b>这里提供两种使用方式</b></p>
 * <p>1. {@link RequestLimitConfig} 配合处理器 {@link RequestLimitHandler} 进行编程式使用</p>
 * <p>2. 提供注解 {@link RequestLimit} 并开启配置 {@code xingtools.enabled.request-limit=true} 进行声明式使用</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/6 20:24
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class RequestLimitConfig {
    /**
     * key 前缀
     * <p>key 最终结果是 prefix + keyStrategy </p>
     *
     * <p>默认为 ""</p>
     */
    private String prefix = "";

    /**
     * key 字符串添加策略
     *
     * <p>配置默认三种策略组合</p>
     */
    private int keyStrategy = KeyStrategy.IP | KeyStrategy.HTTP_METHOD | KeyStrategy.HTTP_URI;


    /**
     * key 值
     * <p>默认为空</p>
     * <p>若不为空，则会覆盖keyStrategy生成的key , 最终结果是 prefix + key</p>
     *
     * <p>key 值</p>
     */
    private String key = "";

    /**
     * timeout 时间内允许的次数
     *
     * <p>默认1次 / timeout</p>
     */
    private long maxCount = 1;

    /**
     * <p>时间间隔</p>
     * <p>这里有两种说法</p>
     * <p>1. key 过期时间</p>
     * <p>2. key 刷新的间隔</p>
     *
     * <p>默认 1 timeUnit</p>
     */
    private long timeout = 1;


    /**
     * 获取允许权限过程等待时间
     * <p>提供的 redis 策略无效果</p>
     * <p>提供的 rate 策略为令牌桶获取时间间隔</p>
     *
     * <p> the maximum time to wait for the permit </p>
     *
     * <p> 默认 0 timeUnit</p>
     */
    private long waitTime = 0;


    /**
     * timeout 和 waitTime 时间单位
     *
     * <p>默认单位为秒</p>
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;


    /**
     * 是否开启防抖动
     * <p>开启则为防抖模式，如果在 timeout 内发起请求，则会刷新 timeout 重新开始计时</p>，直到到达 maxCount 会结束刷新
     * <p>关闭则为节流模式</p>
     *
     * <p>默认关闭</p>
     */
    private boolean debounce = false;

    /**
     * aop 切面策略
     * <p>提供 redis 策略 {@link RedisLimitStrategy}</p>
     * <p>提供 rate 策略 {@link RateLimitStrategy}}</p>
     *
     * <p>默认 redis 策略</p>
     */
    private LimitStrategy strategy;


    /**
     * strategy 是否通过 spring 容器 注入
     * <p>默认 true</p>
     * <p>true ==> 先从 spring 容器获取 strategy 指定的类对象，如果没有则从反射获取实例</p>
     * <p>false ==> 直接反射获取实例</p>
     */
    private boolean needSpring = true;

    /**
     * key 分隔符
     *
     * @return 分隔符
     */
    private String delimiter = "&";

    /**
     * 方法未执行返回提示信息
     */
    private String msg = LimitKey.QUICK_MSG;

    /**
     * 限制的key，会在执 intercept 方法后填充
     */
    private String limitKey;

    /**
     * 上下文对象，用于扩展
     */
    private Map<String, Object> context;

    public RequestLimitConfig() {
        this.strategy = new RedisLimitStrategy();
    }

    public RequestLimitConfig(LimitStrategy strategy) {
        this.strategy = strategy;
    }

    public RequestLimitConfig(@NotNull RequestLimit requestLimit) {
        this(requestLimit, RequestLimitProcessor.getLimitStrategy(requestLimit));
    }

    public RequestLimitConfig(@NotNull RequestLimit requestLimit, @NotNull LimitStrategy strategy) {
        this.prefix = requestLimit.prefix();
        this.keyStrategy = requestLimit.keyStrategy();
        this.key = requestLimit.key();
        this.maxCount = requestLimit.maxCount();
        this.timeout = requestLimit.timeout();
        this.waitTime = requestLimit.waitTime();
        this.timeUnit = requestLimit.timeUnit();
        this.debounce = requestLimit.debounce();
        this.strategy = strategy;
        this.needSpring = requestLimit.needSpring();
        this.delimiter = requestLimit.delimiter();
        this.msg = requestLimit.msg();
        this.context = new HashMap<>();
    }

    public RequestLimitConfig(@NotNull RequestLimit requestLimit, @NotNull RequestLimitSetting requestLimitSetting) {
        RequestLimitConfig other = requestLimitSetting.getRequestLimitConfig();
        this.prefix = ChooseUtil.checkBlankOverride(requestLimit.prefix(), other.prefix);
        this.keyStrategy = ChooseUtil.checkOverride(requestLimit.keyStrategy(), other.keyStrategy, KeyStrategy.IP | KeyStrategy.HTTP_METHOD | KeyStrategy.HTTP_URI);
        this.key = ChooseUtil.checkBlankOverride(requestLimit.key(), other.key);
        this.maxCount = ChooseUtil.checkOverride(requestLimit.maxCount(), other.maxCount, 1L);
        this.timeout = ChooseUtil.checkOverride(requestLimit.timeout(), other.timeout, 1L);
        this.waitTime = ChooseUtil.checkOverride(requestLimit.waitTime(), other.waitTime, 0L);
        this.timeUnit = ChooseUtil.checkAddrOverride(requestLimit.timeUnit(), other.timeUnit, TimeUnit.SECONDS);
        this.debounce = ChooseUtil.checkOverride(requestLimit.debounce(), other.debounce, false);
        this.strategy = takeLimitStrategy(requestLimit, other.strategy);
        this.needSpring = ChooseUtil.checkOverride(requestLimit.needSpring(), other.needSpring, true);
        this.delimiter = ChooseUtil.checkOverride(requestLimit.delimiter(), other.delimiter, "&");
        this.msg = ChooseUtil.checkOverride(requestLimit.msg(), other.msg, LimitKey.QUICK_MSG);
        this.context = new HashMap<>();
    }


    protected LimitStrategy takeLimitStrategy(@NotNull RequestLimit requestLimit, LimitStrategy strategy) {
        if (strategy != null && requestLimit.strategy() == RedisLimitStrategy.class) {
            return strategy;
        }
        return RequestLimitProcessor.getLimitStrategy(requestLimit);
    }


}
