package top.cutexingluo.tools.utils.ee.web.limit.submit.pkg;

import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.Assert;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitConfig;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitProcessor;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy;
import top.cutexingluo.tools.utils.se.string.XTPickUtil;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 * RequestLimit 通用拦截器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/6 21:09
 * @since 1.0.4
 */
public class RequestLimitInterceptor {

    /**
     * RequestLimit 拦截主方法
     *
     * @param requestLimitConfig 请求限制配置
     * @param bundle             web方法切面配置
     *                           <p>-method 方法</p>
     *                           <p>-httpServletRequest 请求对象</p>
     *                           <p>-joinPoint 执行的程序 , 注解模式不为null</p>
     * @return 是否通过
     * @throws Exception 返回拒绝异常
     */
    public static boolean intercept(@NotNull RequestLimitConfig requestLimitConfig, @NotNull AspectBundle bundle) throws Exception {
        LimitStrategy limitStrategy = requestLimitConfig.getStrategy();
        Assert.notNull(limitStrategy, "LimitStrategy is null. Please check your strategy.");
        if (!limitStrategy.checkRequestLimit(requestLimitConfig)) { // check the requestLimit
            return false;
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        if (StrUtil.isNotBlank(requestLimitConfig.getPrefix())) {
            map.put("prefix", requestLimitConfig.getPrefix());
        }
        if (StrUtil.isNotBlank(requestLimitConfig.getKey())) { // not blank ==> override the key
            map.put("key", requestLimitConfig.getKey());
        }
        getKeyMap(map, requestLimitConfig, bundle.getRequest(), bundle.getMethod());
        String limitKey = limitStrategy.getLimitKey(map, requestLimitConfig, bundle);
        if (limitKey == null) { // 默认生成策略
            limitKey = generateKey(map, requestLimitConfig.getDelimiter());
        }
        requestLimitConfig.setLimitKey(limitKey);
        boolean pass = limitStrategy.interceptor(requestLimitConfig, limitKey);
        if (!pass) {
            throw limitStrategy.refuseException(requestLimitConfig.getMsg());
        }
        return true;
    }

    /**
     * 组装 key
     */
    public static String generateKey(@NotNull LinkedHashMap<String, String> keyMap, String delimiter) {
        StringBuilder sb = new StringBuilder();
        if (keyMap.size() == 0) {
            throw new IllegalStateException("keyMap is empty. the key is empty !");
        }
        String prefix = keyMap.get("prefix");
        if (StrUtil.isNotBlank(prefix)) { // prefix
            sb.append(prefix);
        }
        String key = keyMap.get("key");
        if (StrUtil.isNotBlank(key)) { // key
            if (sb.length() != 0) {
                sb.append(delimiter);
            }
            String tmp = sb.append(key).toString();
            return XTPickUtil.takeAllValueFromBraces(tmp, s -> {
                String value = keyMap.get(s);
                if (StrUtil.isNotBlank(value)) {
                    return value;
                }
                return s;
            });
        }
        keyMap.forEach((k, v) -> { // other
            if (sb.length() != 0) {
                sb.append(delimiter);
            }
            sb.append(v);
        });
        return sb.toString();
    }

    /**
     * 生成 key 放进map
     */
    public static void getKeyMap(@NotNull LinkedHashMap<String, String> keyMap, @NotNull RequestLimitConfig requestLimitConfig, @Nullable HttpServletRequest httpServletRequest, @Nullable Method method) {
        if ((requestLimitConfig.getKeyStrategy() & KeyStrategy.IP) != 0) {// IP-127.0.0.1
            if (httpServletRequest != null) {
                String ip = RequestLimitProcessor.getIp(httpServletRequest);
                if (StrUtil.isNotBlank(ip)) {
                    keyMap.put("ip", "IP-" + ip);
                }
            }
        }
        if ((requestLimitConfig.getKeyStrategy() & KeyStrategy.HTTP_METHOD) != 0) {
            if (httpServletRequest != null) {
                String httpMethod = RequestLimitProcessor.getHttpMethod(httpServletRequest);
                if (StrUtil.isNotBlank(httpMethod)) {
                    keyMap.put("httpMethod", httpMethod);
                }
            }
        }
        if ((requestLimitConfig.getKeyStrategy() & KeyStrategy.HTTP_URI) != 0) {
            if (httpServletRequest != null) {
                String httpUri = RequestLimitProcessor.getHttpUri(httpServletRequest);
                if (StrUtil.isNotBlank(httpUri)) {
                    keyMap.put("httpUri", httpUri);
                }
            }
        }
        if ((requestLimitConfig.getKeyStrategy() & KeyStrategy.METHOD) != 0) {
            if (method != null) {
                if (StrUtil.isNotBlank(method.toString())) {
                    keyMap.put("method", method.toString());
                }
            }
        }
        if ((requestLimitConfig.getKeyStrategy() & KeyStrategy.KEY_ANNO) != 0) {
            if (method != null) {
                StringBuilder sb = new StringBuilder();
                String delimiter = requestLimitConfig.getDelimiter(); // 分割符
                String requestLimitKey = RequestLimitProcessor.getRequestLimitKey(method, sb, delimiter).toString();
                if (StrUtil.isNotBlank(requestLimitKey)) {
                    keyMap.put("requestLimitKey", requestLimitKey);
                }
            }
        }
    }

}
