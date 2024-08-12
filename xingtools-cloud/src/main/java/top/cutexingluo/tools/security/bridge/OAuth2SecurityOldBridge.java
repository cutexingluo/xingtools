package top.cutexingluo.tools.security.bridge;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.boot.system.JavaVersion;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import top.cutexingluo.tools.bridge.servlet.HttpServletRequestData;
import top.cutexingluo.tools.security.base.TokenExtractor;
import top.cutexingluo.tools.security.self.base.AuthAccessToken;
import top.cutexingluo.tools.security.self.base.AuthTokenExtractor;
import top.cutexingluo.tools.security.self.core.XTAuthAccessToken;

import java.util.Date;

/**
 * oauth2，self 包 转接器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/29 11:01
 */
public class OAuth2SecurityOldBridge {


    // oauth 2 转接器

    /**
     * 将老版 oauth2 的 OAuth2AccessToken 转换为 self 包的 AuthAccessToken
     * <p>注意 refreshToken 和 字符串等信息没有进行移植</p>
     */
    @NotNull
    public static XTAuthAccessToken toAuthAccessToken(@NotNull OAuth2AccessToken oAuth2AccessToken) {
        XTAuthAccessToken accessToken = new XTAuthAccessToken();
        accessToken.setToken(oAuth2AccessToken.getValue());
        Date expiration = oAuth2AccessToken.getExpiration();
        if (expiration != null) accessToken.setExpiresAt(expiration.toInstant());
        accessToken.setScope(oAuth2AccessToken.getScope());
        accessToken.setTokenType(oAuth2AccessToken.getTokenType());
        accessToken.setAdditionalInformation(oAuth2AccessToken.getAdditionalInformation());
        return accessToken;
    }

    /**
     * 将  self 包的 AuthAccessToken 转换为 老版 oauth2 的 OAuth2AccessToken
     * <p>注意 refreshToken 和 字符串等信息没有进行移植</p>
     */
    @NotNull
    public static DefaultOAuth2AccessToken toOAuth2AccessToken(@NotNull AuthAccessToken authAccessToken) {
        DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(authAccessToken.getValue());
        accessToken.setExpiration(authAccessToken.getExpiration());
        accessToken.setScope(authAccessToken.getScope());
        accessToken.setTokenType(authAccessToken.getTokenType());
        accessToken.setAdditionalInformation(authAccessToken.getAdditionalInformation());
        return accessToken;
    }


    /**
     * 将  self 包的 authTokenExtractor 转换为 老版 oauth2 的 TokenExtractor
     *
     * <p>仅支持 jdk8 </p>
     */
    @ConditionalOnJava(value = JavaVersion.EIGHT)
    @NotNull
    public static TokenExtractor toTokenExtractor(@NotNull AuthTokenExtractor authTokenExtractor) {
        return (request) -> authTokenExtractor.extract(new HttpServletRequestData(request));
    }

    /**
     * 将 老版 oauth2 的 TokenExtractor  转换为 self 包的 authTokenExtractor
     *
     * <p>仅支持 jdk8 </p>
     */
    @ConditionalOnJava(value = JavaVersion.EIGHT)
    @NotNull
    public static AuthTokenExtractor toAuthTokenExtractor(@NotNull TokenExtractor tokenExtractor) {
        return (request) -> tokenExtractor.extract(request.getRequest());
    }


}


