package top.cutexingluo.tools.security.oauthserver.manager;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

/**
 * token 令牌配置管理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/4 10:19
 * @since 1.1.6
 */
public interface TokenConfigManager {


    /**
     * 令牌定制器
     * <p>Sets the {@link OAuth2TokenCustomizer} that customizes the
     * {@link OAuth2TokenClaimsContext#getClaims() claims} for the {@link OAuth2AccessToken}.</p>
     */
    default OAuth2TokenCustomizer<?> oAuth2TokenCustomizer() {
        return null;
    }


    /**
     * 令牌生成器
     * <p>{@link OAuth2TokenFormat#REFERENCE "self-contained"} (opaque) {@link OAuth2AccessToken}.</p>
     * <pre>
     *     authorizationServerConfigurer.tokenGenerator(oAuth2TokenGenerator);
     * </pre>
     */
    default OAuth2TokenGenerator<?> oAuth2TokenGenerator() {
//        return new JwtGenerator(parameters -> null);
        return null;
    }
}
