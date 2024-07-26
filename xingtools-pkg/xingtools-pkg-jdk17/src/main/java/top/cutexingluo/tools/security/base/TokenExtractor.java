package top.cutexingluo.tools.security.base;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 * TokenExtractor
 * <p>oauth2 移植接口</p>
 *
 * <p>未来将被移除，请使用 cloud 包里面的类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 14:59
 * @since 1.1.1
 */
@Deprecated
public interface TokenExtractor {
    /**
     * Extract a token value from an incoming request without authentication.
     */
    Authentication extract(HttpServletRequest request);

}
