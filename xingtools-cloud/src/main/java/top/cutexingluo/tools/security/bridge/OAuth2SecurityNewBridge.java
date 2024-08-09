package top.cutexingluo.tools.security.bridge;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import top.cutexingluo.tools.security.self.base.AuthAccessToken;
import top.cutexingluo.tools.security.self.core.XTAuthAccessToken;

/**
 * oauth2 (spring-authorization-server)，self 包 转接器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/29 11:24
 */
public class OAuth2SecurityNewBridge {

    // spring-authorization-servet 转接器

    /**
     * 将 SAS 的 oAuth2Token 转换为 self 包的 AuthAccessToken
     */
    @NotNull
    public static XTAuthAccessToken toAuthAccessToken(@NotNull OAuth2Token oAuth2Token) {
        XTAuthAccessToken accessToken = new XTAuthAccessToken();
        accessToken.setToken(oAuth2Token.getTokenValue());
        accessToken.setExpiresAt(oAuth2Token.getExpiresAt());
        accessToken.setIssuedAt(oAuth2Token.getIssuedAt());
        return accessToken;
    }


    /**
     * 将 SAS 的 oAuth2Token 转换为 self 包的 AuthAccessToken
     */
    @NotNull
    public static XTAuthAccessToken toAuthAccessToken(@NotNull OAuth2AccessToken auth2AccessToken) {
        XTAuthAccessToken accessToken = new XTAuthAccessToken();
        accessToken.setToken(auth2AccessToken.getTokenValue());
        accessToken.setExpiresAt(auth2AccessToken.getExpiresAt());
        accessToken.setIssuedAt(auth2AccessToken.getIssuedAt());
        accessToken.setTokenType(auth2AccessToken.getTokenType().getValue());
        accessToken.setScope(auth2AccessToken.getScopes());
        return accessToken;
    }

    /**
     * 将 SAS 的 oAuth2Token 转换为 self 包的 AuthAccessToken
     *
     * <p> 由于  OAuth2AccessToken .TokenType 具有私有访问权限，所以 tokenType 只能为 Bearer </p>
     */
    @NotNull
    public static OAuth2AccessToken toAuthAccessToken(@NotNull AuthAccessToken authAccessToken) {
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                authAccessToken.getValue(),
                authAccessToken.getIssuedAt(),
                authAccessToken.getExpiresAt(),
                authAccessToken.getScope()
        );
        return accessToken;
    }
}
