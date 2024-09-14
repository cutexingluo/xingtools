package top.cutexingluo.tools.utils.ee.web.limit.submit.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.cutexingluo.tools.utils.ee.web.limit.LimitKey;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.KeyStrategy;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.RequestLimitHandler;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.impl.RateLimitStrategy;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.impl.RedisLimitStrategy;

/**
 * 接口防抖节流限制 扩展配置
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
 * @since 1.0.4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class RequestLimitConfig extends RequestLimitData {
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
     */
    private String delimiter = "&";

    /**
     * 方法未执行返回提示信息
     */
    private String msg = LimitKey.QUICK_MSG;


    public RequestLimitConfig() {
        this.strategy = new RedisLimitStrategy();
    }


    /**
     * 推荐使用的构造方法
     */
    public RequestLimitConfig(LimitStrategy strategy) {
        this.strategy = strategy;
    }


}
