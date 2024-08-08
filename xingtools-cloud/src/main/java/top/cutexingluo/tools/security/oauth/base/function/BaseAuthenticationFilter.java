package top.cutexingluo.tools.security.oauth.base.function;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import java.util.function.BiFunction;

/**
 * 基本身份验证过滤器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/29 16:33
 */
@FunctionalInterface
public interface BaseAuthenticationFilter extends BiFunction<Authentication, DefaultOAuth2AccessToken, Authentication> {
}
