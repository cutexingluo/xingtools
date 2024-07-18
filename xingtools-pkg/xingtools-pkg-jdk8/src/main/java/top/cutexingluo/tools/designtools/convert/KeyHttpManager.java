package top.cutexingluo.tools.designtools.convert;

import cn.hutool.json.JSONUtil;
import top.cutexingluo.tools.utils.ee.web.ip.util.IPUtil;

/**
 * key - value 转化器
 * <p>http 版本</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/13 13:23
 * @since 1.0.4
 */
public class KeyHttpManager {

    /**
     * ip
     * <p> request ip</p>
     */
    public static final KeyHttpConvertor IP = new KeyHttpConvertor("ip", (method, request) -> request == null ? "" : IPUtil.getIpAddr(request));


    /**
     * real ip
     * <p> request real ip</p>
     */
    public static final KeyHttpConvertor REAL_IP = new KeyHttpConvertor("realIp", (method, request) -> request == null ? "" : IPUtil.getRealIpAddr(request));


    /**
     * host ip
     * <p> response ip</p>
     */
    public static final KeyHttpConvertor HOST_IP = new KeyHttpConvertor("hostIp", (method, request) -> request == null ? "" : IPUtil.getHostIp());

    /**
     * http_uri   uri
     */
    public static final KeyHttpConvertor HTTP_URI = new KeyHttpConvertor("uri", (method, request) -> request == null ? "" : request.getRequestURI());


    /**
     * http_uri   uri
     */
    public static final KeyHttpConvertor HEADER = new KeyHttpConvertor("header", (method, request) -> request == null ? "" : JSONUtil.toJsonStr(IPUtil.getHeader(request)));


    /**
     * http_method  uri
     * <p> GET, POST, PUT, DELETE </p>
     */
    public static final KeyHttpConvertor HTTP_METHOD = new KeyHttpConvertor("httpMethod", (method, request) -> request == null ? "" : request.getMethod());


    /**
     * method
     */
    public static final KeyHttpConvertor METHOD = new KeyHttpConvertor("method", (method, request) -> method == null ? "" : method.toString());


    /**
     * generic_method
     */
    public static final KeyHttpConvertor G_METHOD = new KeyHttpConvertor("gMethod", (method, request) -> method == null ? "" : method.toGenericString());

    /**
     * method
     */
    public static final KeyHttpConvertor TYPE = new KeyHttpConvertor("type", (method, request) -> method == null ? "" : method.getDeclaringClass().toString());


    /**
     * method-simple name
     */
    public static final KeyHttpConvertor METHOD_NAME = new KeyHttpConvertor("methodName", (method, request) -> method == null ? "" : method.getName());

}
