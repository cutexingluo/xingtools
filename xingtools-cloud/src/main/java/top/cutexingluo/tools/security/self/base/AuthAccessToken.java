package top.cutexingluo.tools.security.self.base;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * access token
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/25 15:44
 * @see org.springframework.security.oauth2.common.OAuth2AccessToken
 * @since 1.1.2
 */
public interface AuthAccessToken extends AuthToken {

    public static String BEARER_TYPE = "Bearer";

    public static String ACCESS_TOKEN = "access_token";

    public static String TOKEN_TYPE = "token_type";

    public static String EXPIRES_IN = "expires_in";

    public static String EXPIRES_AT = "expires_at";

    public static String ISSUED_AT = "issued_at";

    public static String SCOPE = "scope";

    Map<String, Object> getAdditionalInformation();

    Set<String> getScope();

    String getTokenType();

    boolean isExpired();

    Date getExpiration();

    /**
     * time interval . seconds , less than about 6 years
     */
    int getExpiresIn();

    String getValue();

}
