package top.cutexingluo.tools.security.self.core;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.security.self.base.AbstractAuthAccessToken;
import top.cutexingluo.tools.security.self.base.AuthAccessToken;

import java.time.Instant;
import java.util.*;

/**
 * access token info
 * <p>token 信息类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/25 16:45
 * @see org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
 * @since 1.1.2
 */
public class XTAuthAccessToken extends AbstractAuthAccessToken {

    public XTAuthAccessToken(String token) {
        super(token);
    }

    public XTAuthAccessToken() {
    }

    public XTAuthAccessToken(String token, Instant issuedAt, Instant expiresAt) {
        super(token, issuedAt, expiresAt);
    }

    public XTAuthAccessToken(String token, Instant issuedAt, Instant expiresAt, Map<String, Object> additionalInformation) {
        super(token, issuedAt, expiresAt, additionalInformation);
    }

    public XTAuthAccessToken(String token, Instant issuedAt, Instant expiresAt, Map<String, Object> additionalInformation, Set<String> scope) {
        super(token, issuedAt, expiresAt, additionalInformation, scope);
    }

    public XTAuthAccessToken(String token, Instant issuedAt, Instant expiresAt, Map<String, Object> additionalInformation, Set<String> scope, String tokenType) {
        super(token, issuedAt, expiresAt, additionalInformation, scope, tokenType);
    }

    public XTAuthAccessToken(@NotNull AuthAccessToken accessToken) {
        super(accessToken);
    }

    @Override
    public XTAuthAccessToken checkInit() {
        super.checkInit();
        return this;
    }

    @Override
    public XTAuthAccessToken refresh() {
        super.refresh();
        return this;
    }

    /**
     * 提取信息
     */
    public static XTAuthAccessToken valueOf(Map<String, Object> tokenParams) {
        String tokenStr = String.valueOf(tokenParams.get(ACCESS_TOKEN));
        XTAuthAccessToken token = new XTAuthAccessToken(tokenStr);

        if (tokenParams.containsKey(EXPIRES_AT)) {
            long expiration = 0;
            try {
                expiration = Long.parseLong(String.valueOf(tokenParams.get(EXPIRES_AT)));
            } catch (NumberFormatException e) {
                // fall through...
            }
            token.setExpiresAt(new Date(expiration * 1000L).toInstant());
        } else if (tokenParams.containsKey(EXPIRES_IN)) {
            long expiresIn = 0;
            try {
                expiresIn = Long.parseLong(String.valueOf(tokenParams.get(EXPIRES_IN)));
            } catch (NumberFormatException e) {
                // fall through...
            }
            token.setExpiresAt(new Date(System.currentTimeMillis() + (expiresIn * 1000L)).toInstant());
        }

        if (tokenParams.containsKey(ISSUED_AT)) {
            long issuedAt = 0;
            try {
                issuedAt = Long.parseLong(String.valueOf(tokenParams.get(ISSUED_AT)));
            } catch (NumberFormatException e) {
                // fall through...
            }
            token.setIssuedAt(new Date(issuedAt * 1000L).toInstant());
        }

        if (tokenParams.containsKey(SCOPE)) {
            Set<String> scope = new TreeSet<String>();
            String s = String.valueOf(tokenParams.get(SCOPE));
            for (StringTokenizer tokenizer = new StringTokenizer(s, " ,"); tokenizer
                    .hasMoreTokens(); ) {
                scope.add(tokenizer.nextToken());
            }
            token.setScope(scope);
        }

        if (tokenParams.containsKey(TOKEN_TYPE)) {
            String s = String.valueOf(tokenParams.get(TOKEN_TYPE));
            token.setTokenType(s);
        }

        return token;
    }


}
