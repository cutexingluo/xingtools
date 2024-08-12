package top.cutexingluo.tools.bridge.servlet.adapter;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * HttpServletRequest 适配器
 *
 * <p>由于 jdk 只识别签名，故1.1.2 版本 在支持jdk17 版本的众多方法会报错 , NoSuchMethodError 找不到方法</p>
 * <p>故在 xingtools 里面 不对外暴露方法, 面向接口</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/12 10:49
 * @since 1.1.3
 */
public interface HttpServletRequestAdapter {

    /**
     * 获取请求头
     */
    String getHeader(String name);

    /**
     * 获取请求头
     */
    Enumeration<String> getHeaders(String name);

    /**
     * 获取请求头名称
     */
    Enumeration<String> getHeaderNames();

    /**
     * 获取请求路径
     */
    String getRequestURI();

    /**
     * 获取请求方法
     */
    String getMethod();

    /**
     * 获取客户端IP
     */
    String getRemoteAddr();


    /**
     * cookie 获取value
     * <p>适配方法</p>
     *
     * @return value cookie.getValue()
     */
    String getCookieValue(String name);

    /**
     * cookie 过滤器
     * <p>适配方法</p>
     *
     * @param namePredicate  名称过滤
     * @param cookieConsumer cookie 消费器
     */
    void cookieFilter(Predicate<String> namePredicate, BiConsumer<String, String> cookieConsumer);


    /**
     * cookie 过滤器
     * <p>适配方法</p>
     *
     * @param namePredicate 名称过滤
     * @return 符合条件的 cookie 对
     */
    default Map<String, String> cookieFilter(Predicate<String> namePredicate) {
        HashMap<String, String> map = new HashMap<>();
        this.cookieFilter(namePredicate, map::put);
        return map;
    }


}
