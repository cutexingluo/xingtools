package top.cutexingluo.tools.utils.ee.web.limit.submit.pkg;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitConfig;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 提供编程式限流的功能
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/6 20:21
 * @since 1.0.4
 */
public class RequestLimitHandler {

    /**
     * RequestLimitConfig 请求限制配置
     */
    protected RequestLimitConfig requestLimitConfig;

    public RequestLimitHandler(RequestLimitConfig requestLimitConfig) {
        this.requestLimitConfig = requestLimitConfig;
    }


    /**
     * 编程式 limit
     * <p>可用于拦截器等任意地方</p>
     *
     * @param httpServletRequest 请求对象
     * @param method             方法对象
     * @return 是否通过
     * @throws Exception 返回拒绝异常
     */
    public boolean limit(@Nullable Method method, @Nullable HttpServletRequest httpServletRequest) throws Exception {
        return RequestLimitInterceptor.intercept(requestLimitConfig, new AspectBundle(method, httpServletRequest));
    }

    /**
     * 编程式 limit
     * <p>可用于AOP 注解</p>
     *
     * @param bundle web方法切面配置
     * @return 是否通过
     * @throws Exception 返回拒绝异常
     */
    public boolean limit(@NotNull AspectBundle bundle) throws Exception {
        return RequestLimitInterceptor.intercept(requestLimitConfig, bundle);
    }

    /**
     * 执行完的操作
     */
    public void afterDone(@NotNull AspectBundle bundle) {
        LimitStrategy limitStrategy = requestLimitConfig.getStrategy();
        Assert.notNull(limitStrategy, "In AfterDone, LimitStrategy is null. Please check your strategy.");
        limitStrategy.afterDone(requestLimitConfig, bundle);
    }
}
