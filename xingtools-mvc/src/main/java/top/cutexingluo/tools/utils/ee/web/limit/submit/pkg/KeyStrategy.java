package top.cutexingluo.tools.utils.ee.web.limit.submit.pkg;

import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitKey;

import javax.servlet.http.HttpServletRequest;

/**
 * key string 组装策略
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/5 16:45
 * @since 1.0.4
 */
public class KeyStrategy {

    /**
     * <p>使用 IP 限定 </p>
     * <p>如果不加则会全局限定</p>
     * <p>例如 127.0.0.1</p>
     * <p> key = "ip"</p>
     *
     * <p>HttpServletRequest:  {@link HttpServletRequest}</p>
     */
    public final static int IP = 0b10000;

    /**
     * <p>依据 HTTP_METHOD 进行限定 </p>
     * <p>HttpServletRequest.getMethod()</p>
     * <p>例如 GET POST PUT DELETE 等</p>
     * <p> key = "httpMethod"</p>
     *
     * <p>HttpServletRequest:  {@link HttpServletRequest}</p>
     */
    public final static int HTTP_METHOD = 0b01000;

    /**
     * <p>依据 HTTP_URI 进行限定 </p>
     * <p>对请求 API 接口进行限定</p>
     * <p>例如 /xxx/xxx/xxx</p>
     * <p> key = "httpUri"</p>
     *
     * <p>HttpServletRequest:  {@link HttpServletRequest}</p>
     */
    public final static int HTTP_URI = 0b00100;

    /**
     * <p>依据 Method 签名(全限定) 进行限定 </p>
     * <p>仅注解模式有效</p>
     * <p>例如 public xxx(xxx)</p>
     * <p> key = "method"</p>
     *
     * <p>Method:  {@link java.lang.reflect.Method}</p>
     */
    public final static int METHOD = 0b00010;

    /**
     * <p>使用 key 注解进行组装 </p>
     * <p>对方法参数进行自定义限定</p>
     * <p>仅注解模式有效</p>
     * <p>key = "requestLimitKey"</p>
     *
     * <p>key 注解:  {@link RequestLimitKey}</p>
     */
    public final static int KEY_ANNO = 0b00001;


}
