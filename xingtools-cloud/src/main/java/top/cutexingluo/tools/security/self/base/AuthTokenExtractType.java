package top.cutexingluo.tools.security.self.base;

/**
 * request 提取类型
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/25 17:20
 * @since 1.1.2
 */
public class AuthTokenExtractType {

    /**
     * 使用 request headers
     */
    public static final int USE_HEADERS = 1;

    /**
     * 使用 request cookies
     */
    public static final int USE_COOKIES = 2;
}
