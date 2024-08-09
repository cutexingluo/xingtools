package top.cutexingluo.tools.security.self.base;

import org.springframework.security.core.Authentication;
import top.cutexingluo.tools.bridge.servlet.HttpServletRequestData;

/**
 * token  extractor
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/24 17:21
 * @since 1.1.2
 */
@FunctionalInterface
public interface AuthTokenExtractor {

    /**
     * Extract a token value from an incoming request without authentication.
     */
    Authentication extract(HttpServletRequestData request);
}
