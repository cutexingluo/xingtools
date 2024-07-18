package top.cutexingluo.tools.aop.log.xtlog;


import top.cutexingluo.tools.designtools.convert.KeyManager;

/**
 * WebLog 基本字符串常量
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/13 13:29
 * @since 1.0.4
 */
public class LogKey extends KeyManager {

    public static final String RET_KEY = "ret";
    public static final String WRAP_VALUE = "${wrap}";
    public static final String COMMA = ",";
    public static final String COMMAS = ", ";


    public static final String IP_STR = "IP: ${ip}";
    public static final String F_IP_STR = "IP              : ${ip}";

    public static final String REAL_IP = "IP: ${realIp}";
    public static final String F_REAL_IP_STR = "IP              : ${realIp}";

    public static final String HOST_IP = "HOST_IP: ${hostIp}";
    public static final String F_HOST_IP = "HOST_IP         : ${hostIp}";

    public static final String B_NAME = "BusinessName: ${bName}";
    public static final String F_B_NAME = "BusinessName    : ${bName}";

    public static final String URI = "URI: ${uri}";
    public static final String F_URI = "URI             : ${uri}";

    public static final String HTTP_METHOD = "HTTP Method: ${httpMethod}";
    public static final String F_HTTP_METHOD = "HTTP Method     : ${httpMethod}";

    public static final String S_METHOD = "Signature Method: ${sName}";
    public static final String F_S_METHOD = "Signature Method: ${sName}";

    public static final String CLASS_METHOD = "Class Method: ${classMethod}";
    public static final String F_CLASS_METHOD = "Class Method    : ${classMethod}";

    public static final String HEADER = "Header: ${header}";
    public static final String F_HEADER = "Header          : ${header}";

    public static final String ARGS = "Args: ${args}";
    public static final String F_ARGS = "Args            : ${args}";

    public static final String RET = "Return: ${ret}";
    public static final String F_RET = "Return          : ${ret}";

    /**
     * 本地 IP 为真实 ip
     */
    public static final String ALL_REAL = REAL_IP + ", " + HOST_IP + ", " + B_NAME + ", " + URI + ", " + HTTP_METHOD + ", " + CLASS_METHOD + ", " + HEADER + ", " + ARGS;
    /**
     * 本地 IP 为真实 ip
     */
    public static final String F_ALL_REAL = REAL_IP + "\n" + F_HOST_IP + "\n" + F_B_NAME + "\n" + F_URI + "\n" + F_HTTP_METHOD + "\n" + F_CLASS_METHOD + "\n" + F_HEADER + "\n" + F_ARGS;

    public static final String ALL = IP_STR + ", " + HOST_IP + ", " + B_NAME + ", " + URI + ", " + HTTP_METHOD + ", " + CLASS_METHOD + ", " + HEADER + ", " + ARGS;
    public static final String F_ALL = F_IP_STR + "\n" + F_HOST_IP + "\n" + F_B_NAME + "\n" + F_URI + "\n" + F_HTTP_METHOD + "\n" + F_CLASS_METHOD + "\n" + F_HEADER + "\n" + F_ARGS;

    public static final String ALL_RET = ALL + ", " + RET;
    public static final String F_ALL_RET = F_ALL + "\n" + F_RET;

    public static final String ALL_SIMPLE = IP_STR + ", " + HOST_IP + ", " + B_NAME + ", " + URI + ", " + HTTP_METHOD + ", " + S_METHOD + ", " + HEADER + ", " + ARGS;
    public static final String F_ALL_SIMPLE = F_IP_STR + "\n" + F_HOST_IP + "\n" + F_B_NAME + "\n" + F_URI + "\n" + F_HTTP_METHOD + "\n" + F_S_METHOD + "\n" + F_HEADER + "\n" + F_ARGS;


    public static final String REQUEST = IP_STR + ", " + HOST_IP + ", " + URI + ", " + HTTP_METHOD + ", " + CLASS_METHOD + ", " + HEADER + ", " + ARGS;
    public static final String F_REQUEST = F_IP_STR + "\n" + F_HOST_IP + "\n" + F_URI + "\n" + F_HTTP_METHOD + "\n" + F_CLASS_METHOD + "\n" + F_HEADER + "\n" + F_ARGS;

    /**
     * 无 header ，减小字符串长度
     */
    public static final String REQUEST_SIMPLE = IP_STR + ", " + HOST_IP + ", " + URI + ", " + HTTP_METHOD + ", " + CLASS_METHOD + ", " + ARGS;

    /**
     * 无 header ，减小字符串长度
     */
    public static final String F_REQUEST_SIMPLE = F_IP_STR + "\n" + F_HOST_IP + "\n" + F_URI + "\n" + F_HTTP_METHOD + "\n" + F_CLASS_METHOD + "\n" + F_ARGS;

}
