package top.cutexingluo.tools.bridge.servlet.adapter;


import java.util.Collections;
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
 * <p>于 1.1.6  新增大量 servlet 方法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/12 10:49
 * @since 1.1.3
 */
public interface HttpServletRequestAdapter extends ServletRequestAdapter {


    /**
     * 获取认证类型
     */
    String getAuthType();

//    /**
//     * 获取cookies
//     */
//    Cookie[] getCookies();

    /**
     * 获取请求头时间戳
     */
    long getDateHeader(String name);

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
     * 获取请求头的值
     */
    int getIntHeader(String name);


    /**
     * 获取请求方法
     */
    String getMethod();

    /**
     * 获取路径信息
     */
    String getPathInfo();

    /**
     * 获取实际指定路径
     */
    String getPathTranslated();

//    /**
//     * 获取客户端IP
//     */
//    String getRemoteAddr();

    /**
     * 获取上下文路径
     */
    String getContextPath();

    /**
     * 获取请求参数
     */
    String getQueryString();

    /**
     * 这次请求的登录名
     */
    String getRemoteUser();


    /**
     * 判断是否在角色中
     */
    boolean isUserInRole(String role);

    /**
     * 包含用户名称
     */
    java.security.Principal getUserPrincipal();


    /**
     * 获取session id
     */
    String getRequestedSessionId();

    /**
     * 获取请求路径
     */
    String getRequestURI();


    /**
     * 获取请求URL
     */
    StringBuffer getRequestURL();

    /**
     * 获取 servlet path
     */
    String getServletPath();

    /**
     * 修改session id
     */
    String changeSessionId();


    /**
     * 确定 是否为有效会话
     */
    boolean isRequestedSessionIdValid();

    /**
     * 确定是否来自cookie
     */
    boolean isRequestedSessionIdFromCookie();

    /**
     * 确定是否来自URL
     */
    boolean isRequestedSessionIdFromURL();


    /**
     * 获取 trailer 字段
     */
    default Map<String, String> getTrailerFields() {
        return Collections.emptyMap();
    }

    /**
     * 是否准备就绪
     */
    default boolean isTrailerFieldsReady() {
        return true;
    }


    // ext

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
