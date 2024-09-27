package top.cutexingluo.tools.common;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.base.IResultData;
import top.cutexingluo.tools.common.base.XTStrCode;

/**
 * HttpStatus Constants枚举类
 * <p>如果习惯使用 code 标识符可以使用 {@link Constants}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/29 16:22
 * @since 1.1.2
 */
public enum HttpStatus implements IResultData<Integer>, XTStrCode {

    /**
     * 200 成功
     */
    SUCCESS(200, "SUCCESS", "成功"),
    /**
     * 500 失败
     */
    ERROR(500, "ERROR", "错误"),
    SYSTEM_ERROR(500, "SYSTEM_ERROR", "系统错误"),
    UNAUTHORIZED(401, "UNAUTHORIZED ", "权限不足"),
    BAD_REQUEST(400, "BAD_REQUEST ", "参数错误（缺少，格式不匹配）"),
    FORBIDDEN(403, "FORBIDDEN ", "服务器拒绝请求, 访问受限 / 授权过期"),
    NOT_FOUND(404, "NOT_FOUND ", "资源，服务未找到"),
    OTHER_ERROR(600, "OTHER_ERROR", "其他业务异常"),

    // 下面不常用
    CREATED(201, "CREATED", "成功, 对象已创建"),
    ACCEPTED(202, "ACCEPTED", "成功, 请求已经被接受"),
    NO_CONTENT(204, "NO_CONTENT", "成功, 但是没有返回数据"),

    MOVED_PERM(301, "MOVED_PERM", "资源已被移除"),
    FOUND(302, "FOUND", "资源已被临时移除"),
    SEE_OTHER(303, "SEE_OTHER", "重定向"),
    TEMP_REDIRECT(307, "TEMP_REDIRECT", "临时重定向"),
    NOT_MODIFIED(304, "NOT_MODIFIED", "资源没有被修改"),
    USE_PROXY(305, "USE_PROXY", "使用代理"),
    RESERVED(306, "RESERVED", "保留"),
    PERMANENT_REDIRECT(308, "PERMANENT_REDIRECT", "永久重定向"),


    BAD_METHOD(405, "BAD_METHOD", "不允许的http方法"),
    REQUEST_TIMEOUT(408, "REQUEST_TIMEOUT", "请求超时"),
    CONFLICT(409, "CONFLICT", "资源冲突，或者资源被锁"),
    UNSUPPORTED_TYPE(415, "UNSUPPORTED_TYPE", "不支持的数据，媒体类型"),
    RANGE_NOT_SATISFIABLE(416, "RANGE_NOT_SATISFIABLE", "资源范围不满足"),
    EXPECTATION_FAILED(417, "EXPECTATION_FAILED", "预期失败"),

    NOT_IMPLEMENTED(501, "NOT_IMPLEMENTED", "接口未实现"),
    BAD_GATEWAY(502, "BAD_GATEWAY", "服务器非法应答"),
    SERVICE_UNAVAILABLE(503, "SERVICE_UNAVAILABLE", "服务不可用"),
    GATEWAY_TIMEOUT(504, "GATEWAY_TIMEOUT", "网关超时"),
    HTTP_VERSION(505, "HTTP_VERSION", "HTTP版本不支持"),
    VARIANT_ALSO_NEGOTIATES(506, "VARIANT_ALSO_NEGOTIATES", "服务器不支持所请求的变体"),
    INSUFFICIENT_STORAGE(507, "INSUFFICIENT_STORAGE", "服务器存储空间不足"),
    LOOP_DETECTED(508, "LOOP_DETECTED", "资源循环引用"),
    NOT_EXTENDED(510, "NOT_EXTENDED", "服务器拒绝扩展"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "NETWORK_AUTHENTICATION_REQUIRED", "客户端需要进行身份验证"),
    ENHANCE_YOUR_CALM(520, "ENHANCE_YOUR_CALM", "服务器繁忙，请稍后再试"),
    MISDIRECTED_REQUEST(521, "MISDIRECTED_REQUEST", "请求被导向到错误的地址"),
    WEB_SERVER_IS_DOWN(522, "WEB_SERVER_IS_DOWN", "网站服务器宕机"),
    CONNECTION_TIMED_OUT(523, "CONNECTION_TIMED_OUT", "连接超时"),
    ORIGIN_IS_UNREACHABLE(524, "ORIGIN_IS_UNREACHABLE", "源站不可达"),
    TIMEOUT_OCCURRED(525, "TIMEOUT_OCCURRED", "超时"),
    SSL_HANDSHAKE_FAILED(526, "SSL_HANDSHAKE_FAILED", "SSL握手失败"),
    INVALID_SSL_CERTIFICATE(527, "INVALID_SSL_CERTIFICATE", "无效的SSL证书"),
    RAILGUN_ERROR(528, "RAILGUN_ERROR", "Railgun错误"),
    SITE_IS_OVERLOADED(529, "SITE_IS_OVERLOADED", "网站过载"),
    SITE_IS_FROZEN(530, "SITE_IS_FROZEN", "网站被冻结"),
    NETWORK_READ_TIMEOUT_ERROR(531, "NETWORK_READ_TIMEOUT_ERROR", "网络读超时"),
    NETWORK_CONNECT_TIMEOUT_ERROR(532, "NETWORK_CONNECT_TIMEOUT_ERROR", "网络连接超时"),
    NETWORK_IO_ERROR(533, "NETWORK_IO_ERROR", "网络IO错误"),


    WARN(601, "WARN", "系统警告消息");

    private int code;
    private String name;
    private String msg;

    HttpStatus(int code, String name, String msg) {
        this.code = code;
        this.name = name;
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @NotNull
    @Override
    public String strCode() {
        return String.valueOf(code);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
