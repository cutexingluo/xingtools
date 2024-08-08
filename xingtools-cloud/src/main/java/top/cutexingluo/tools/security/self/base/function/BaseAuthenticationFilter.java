package top.cutexingluo.tools.security.self.base.function;

import org.springframework.security.core.Authentication;
import top.cutexingluo.tools.security.self.base.AuthAccessToken;

import java.util.function.BiFunction;

/**
 * 基本身份验证过滤器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/29 16:33
 * @since 1.1.2
 */
@FunctionalInterface
public interface BaseAuthenticationFilter extends BiFunction<Authentication, AuthAccessToken, Authentication> {
}
