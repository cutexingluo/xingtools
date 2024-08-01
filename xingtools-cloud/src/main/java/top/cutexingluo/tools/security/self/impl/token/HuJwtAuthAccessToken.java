package top.cutexingluo.tools.security.self.impl.token;

import cn.hutool.jwt.RegisteredPayload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.security.self.base.AbstractAuthAccessToken;
import top.cutexingluo.tools.security.self.base.AuthAccessToken;

import java.time.Instant;
import java.util.*;

/**
 * hutool jwt token
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/26 10:28
 * @since 1.1.2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HuJwtAuthAccessToken extends AbstractAuthAccessToken {

    public static String ACCESS_TOKEN = "access_token";

    public static String TOKEN_TYPE = "token_type";

    /**
     * override
     */
    public static String EXPIRES_IN = "exp_in";

    /**
     * override
     */
    public static String EXPIRES_AT = RegisteredPayload.EXPIRES_AT;

    /**
     * override
     */
    public static String ISSUED_AT = RegisteredPayload.ISSUED_AT;

    public static String SCOPE = "scope";

    public HuJwtAuthAccessToken(String token) {
        super(token);
    }

    public HuJwtAuthAccessToken() {
    }

    public HuJwtAuthAccessToken(String token, Instant issuedAt, Instant expiresAt) {
        super(token, issuedAt, expiresAt);
    }

    public HuJwtAuthAccessToken(String token, Instant issuedAt, Instant expiresAt, Map<String, Object> additionalInformation) {
        super(token, issuedAt, expiresAt, additionalInformation);
    }

    public HuJwtAuthAccessToken(String token, Instant issuedAt, Instant expiresAt, Map<String, Object> additionalInformation, Set<String> scope) {
        super(token, issuedAt, expiresAt, additionalInformation, scope);
    }

    public HuJwtAuthAccessToken(String token, Instant issuedAt, Instant expiresAt, Map<String, Object> additionalInformation, Set<String> scope, String tokenType) {
        super(token, issuedAt, expiresAt, additionalInformation, scope, tokenType);
    }

    public HuJwtAuthAccessToken(@NotNull AuthAccessToken accessToken) {
        super(accessToken);
    }

    @Override
    public HuJwtAuthAccessToken checkInit() {
        super.checkInit();
        return this;
    }

    @Override
    public HuJwtAuthAccessToken refresh() {
        Map<String, Object> objectMap = getAdditionalInformation();
        if (objectMap != null) {
            if (getIssuedAt() != null) objectMap.put(ISSUED_AT, toSeconds(getIssuedAt()));
            if (getExpiresAt() != null) objectMap.put(EXPIRES_AT, toSeconds(getExpiresAt()));
            if (getScope() != null) objectMap.put(SCOPE, getScope());
        }
        return this;
    }

    /**
     * 提取信息
     */
    public static HuJwtAuthAccessToken valueOf(Map<String, Object> tokenParams) {
        String tokenStr = String.valueOf(tokenParams.get(ACCESS_TOKEN));
        HuJwtAuthAccessToken token = new HuJwtAuthAccessToken(tokenStr);

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

    /**
     * 提取信息
     * <p>所有信息放入 additionalInformation </p>
     */
    public static HuJwtAuthAccessToken of(Map<String, Object> payloads) {
        HuJwtAuthAccessToken accessToken = HuJwtAuthAccessToken.valueOf(payloads);
        accessToken.setAdditionalInformation(payloads);
        return accessToken;
    }

    @Override
    public String toString() {
        return "HuJwtAuthAccessToken{" +
                "token='" + token + '\'' +
                ", issuedAt=" + issuedAt +
                ", expiresAt=" + expiresAt +
                ", additionalInformation=" + additionalInformation +
                ", scope=" + scope +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
