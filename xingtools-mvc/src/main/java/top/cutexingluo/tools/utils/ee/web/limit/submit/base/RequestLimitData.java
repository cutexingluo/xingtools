package top.cutexingluo.tools.utils.ee.web.limit.submit.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 请求限制配置数据
 *
 * <p><b>这里提供两种使用方式</b></p>
 * <p>1. {@link top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy} 实现类 进行编程式使用</p>
 * <p>2. 详见子类 {@link RequestLimitConfig}  介绍</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/10 11:21
 * @see RequestLimitConfig
 * @since 1.1.4
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class RequestLimitData {

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
     * 限制的key
     * <p>可手动设置</p>
     * <p>会在执行 RequestLimitInterceptor.intercept 方法后自动填充</p>
     */
    private String limitKey;

    /**
     * 上下文对象，用于扩展
     */
    private Map<String, Object> context;

    public RequestLimitData() {
        this.context = new HashMap<>();
    }

    public RequestLimitData(int cap) {
        this.context = new HashMap<>(cap);
    }

    public RequestLimitData(Map<String, Object> context) {
        this.context = context;
    }
}
