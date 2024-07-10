package top.cutexingluo.tools.common;


//public interface Constants { // 用枚举类也行
//    String CODE_200 = "200"; //成功
//    String CODE_500 = "500"; //系统错误
//    String CODE_401 = "401"; //权限不足
//    String CODE_400 = "400"; //参数错误
//    String CODE_600 = "600"; //其他业务异常
//}


import top.cutexingluo.tools.common.base.IResultData;
import top.cutexingluo.tools.common.base.XTIntCode;

/**
 * HttpStatus Constants枚举类
 *
 * @author XingTian
 * @version 1.1.0
 * @date 2023/2/1 22:48
 * @updateFrom 1.0.3
 */


public enum Constants implements IResultData<String>, XTIntCode {
    /**
     * 200 成功
     */
    CODE_200("200", "SUCCESS", "成功"),
    /**
     * 500 失败
     */
    CODE_500("500", "SYSTEM_ERROR", "系统错误"),
    CODE_401("401", "UNAUTHORIZED ", "权限不足"),
    CODE_400("400", "BAD_REQUEST ", "参数错误（缺少，格式不匹配）"),
    CODE_403("403", "FORBIDDEN ", "服务器拒绝请求, 访问受限 / 授权过期"),
    CODE_404("404", "NOT_FOUND ", "资源，服务未找到"),
    CODE_600("600", "OTHER_ERROR", "其他业务异常"),

    // 下面不常用

    CODE_201("201", "CREATED", "成功, 对象已创建"),
    CODE_202("202", "ACCEPTED", "成功, 请求已经被接受"),
    CODE_204("204", "NO_CONTENT", "成功, 但是没有返回数据"),

    CODE_301("301", "MOVED_PERM", "资源已被移除"),
    CODE_302("302", "FOUND", "资源已被临时移除"),
    CODE_303("303", "SEE_OTHER", "重定向"),
    CODE_307("307", "TEMP_REDIRECT", "临时重定向"),
    CODE_304("304", "NOT_MODIFIED", "资源没有被修改"),
    CODE_305("305", "USE_PROXY", "使用代理"),
    CODE_306("306", "RESERVED", "保留"),
    CODE_308("308", "PERMANENT_REDIRECT", "永久重定向"),


    CODE_405("405", "BAD_METHOD", "不允许的http方法"),
    CODE_408("408", "REQUEST_TIMEOUT", "请求超时"),
    CODE_409("409", "CONFLICT", "资源冲突，或者资源被锁"),
    CODE_415("415", "UNSUPPORTED_TYPE", "不支持的数据，媒体类型"),
    CODE_416("416", "RANGE_NOT_SATISFIABLE", "资源范围不满足"),
    CODE_417("417", "EXPECTATION_FAILED", "预期失败"),

    CODE_501("501", "NOT_IMPLEMENTED", "接口未实现"),
    CODE_502("502", "BAD_GATEWAY", "服务器非法应答"),
    CODE_503("503", "SERVICE_UNAVAILABLE", "服务不可用"),
    CODE_504("504", "GATEWAY_TIMEOUT", "网关超时"),
    CODE_505("505", "HTTP_VERSION", "HTTP版本不支持"),
    CODE_506("506", "VARIANT_ALSO_NEGOTIATES", "服务器不支持所请求的变体"),
    CODE_507("507", "INSUFFICIENT_STORAGE", "服务器存储空间不足"),
    CODE_508("508", "LOOP_DETECTED", "资源循环引用"),
    CODE_510("510", "NOT_EXTENDED", "服务器拒绝扩展"),
    CODE_511("511", "NETWORK_AUTHENTICATION_REQUIRED", "客户端需要进行身份验证"),
    CODE_520("520", "ENHANCE_YOUR_CALM", "服务器繁忙，请稍后再试"),
    CODE_521("521", "MISDIRECTED_REQUEST", "请求被导向到错误的地址"),
    CODE_522("522", "WEB_SERVER_IS_DOWN", "网站服务器宕机"),
    CODE_523("523", "CONNECTION_TIMED_OUT", "连接超时"),
    CODE_524("524", "ORIGIN_IS_UNREACHABLE", "源站不可达"),
    CODE_525("525", "TIMEOUT_OCCURRED", "超时"),
    CODE_526("526", "SSL_HANDSHAKE_FAILED", "SSL握手失败"),
    CODE_527("527", "INVALID_SSL_CERTIFICATE", "无效的SSL证书"),
    CODE_528("528", "RAILGUN_ERROR", "Railgun错误"),
    CODE_529("529", "SITE_IS_OVERLOADED", "网站过载"),
    CODE_530("530", "SITE_IS_FROZEN", "网站被冻结"),
    CODE_531("531", "NETWORK_READ_TIMEOUT_ERROR", "网络读超时"),
    CODE_532("532", "NETWORK_CONNECT_TIMEOUT_ERROR", "网络连接超时"),
    CODE_533("533", "NETWORK_IO_ERROR", "网络IO错误"),


    CODE_601("601", "WARN", "系统警告消息");

    private String code;
    private String name;
    private String msg;

    Constants(String code, String name, String msg) {
        this.code = code;
        this.name = name;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int intCode() {
        return Integer.parseInt(code);
    }
}
