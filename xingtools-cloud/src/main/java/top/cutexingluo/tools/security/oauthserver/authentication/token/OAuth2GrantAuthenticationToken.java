package top.cutexingluo.tools.security.oauthserver.authentication.token;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationToken;

import java.util.Map;
import java.util.Set;

/**
 * An implementation of an Authentication representing an OAuth 2.0 Authorization Grant.
 *
 * <p>提供的授权模式认证</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/4 10:10
 * @since 1.1.6
 */
public class OAuth2GrantAuthenticationToken extends OAuth2ClientCredentialsAuthenticationToken {

    /**
     * the authorization grant type
     *
     * <p>授权模式</p>
     */
    private final AuthorizationGrantType authorizationGrantType;

    public OAuth2GrantAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication clientPrincipal, @Nullable Set<String> scopes, @Nullable Map<String, Object> additionalParameters) {
        super(clientPrincipal, scopes, additionalParameters);
        this.authorizationGrantType = authorizationGrantType;
    }

}
