package top.cutexingluo.tools.security.base;

import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;

/**
 * TokenExtractor
 * <p>oauth2 移植接口</p>
 *
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 14:59
 * @since 1.1.1
 */
public interface TokenExtractor {
    /**
     * Extract a token value from an incoming request without authentication.
     */
    Authentication extract(HttpServletRequest request);

}
