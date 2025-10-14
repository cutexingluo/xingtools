package top.cutexingluo.tools.security.self.base;

import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.basepackage.struct.Checkable;
import top.cutexingluo.core.basepackage.struct.Refreshable;

import java.time.Instant;
import java.util.*;

/**
 * AbstractAccessToken
 * <p>抽象类 access token</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/25 16:22
 * @see org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
 * @see org.springframework.security.oauth2.core.AbstractOAuth2Token
 * @since 1.1.2
 */
@Data
public abstract class AbstractAuthAccessToken implements AuthAccessToken, Checkable<AbstractAuthAccessToken>, Refreshable<AbstractAuthAccessToken> {

    /**
     * token
     */
    protected String token;

    /**
     * 签发时间
     */
    protected Instant issuedAt;

    /**
     * 过期时间
     */
    protected Instant expiresAt;


    protected Map<String, Object> additionalInformation = Collections.emptyMap();


    protected Set<String> scope;


    protected String tokenType = BEARER_TYPE.toLowerCase();


    /**
     * Create an access token from the value provided.
     */
    public AbstractAuthAccessToken(String token) {
        setToken(token);
    }

    protected AbstractAuthAccessToken() {
    }

    public AbstractAuthAccessToken(String token, Instant issuedAt, Instant expiresAt) {
        setToken(token);
        setIssuedAt(issuedAt);
        setExpiresAt(expiresAt);
    }

    public AbstractAuthAccessToken(String token, Instant issuedAt, Instant expiresAt, Map<String, Object> additionalInformation) {
        setToken(token);
        setIssuedAt(issuedAt);
        setExpiresAt(expiresAt);
        setAdditionalInformation(additionalInformation);
    }

    public AbstractAuthAccessToken(String token, Instant issuedAt, Instant expiresAt, Map<String, Object> additionalInformation, Set<String> scope) {
        setToken(token);
        setIssuedAt(issuedAt);
        setExpiresAt(expiresAt);
        setAdditionalInformation(additionalInformation);
        setScope(scope);
    }

    public AbstractAuthAccessToken(String token, Instant issuedAt, Instant expiresAt, Map<String, Object> additionalInformation, Set<String> scope, String tokenType) {
        this(token);
        setIssuedAt(issuedAt);
        setExpiresAt(expiresAt);
        setAdditionalInformation(additionalInformation);
        setScope(scope);
        setTokenType(tokenType);
    }

    public AbstractAuthAccessToken(@NotNull AuthAccessToken accessToken) {
        this(accessToken.getValue());
        setIssuedAt(accessToken.getIssuedAt());
        setExpiresAt(accessToken.getExpiresAt());
        setAdditionalInformation(accessToken.getAdditionalInformation());
        setScope(accessToken.getScope());
        setTokenType(accessToken.getTokenType());
    }

    /**
     * 刷新进入 map 里面
     *
     * <p>默认 ISSUED_AT , EXPIRES_AT ,  SCOPE</p>
     */
    @Override
    public AbstractAuthAccessToken refresh() {
        Map<String, Object> objectMap = getAdditionalInformation();
        if (objectMap != null) {
            if (getIssuedAt() != null) objectMap.put(ISSUED_AT, toSeconds(getIssuedAt()));
            if (getExpiresAt() != null) objectMap.put(EXPIRES_AT, toSeconds(getExpiresAt()));
            if (getScope() != null) objectMap.put(SCOPE, getScope());
        }
        return this;
    }

    /**
     * 判断 参数是否合法
     */
    @Override
    public AbstractAuthAccessToken checkInit() {
        if (issuedAt != null && expiresAt != null && expiresAt.isBefore(issuedAt)) {
            throw new IllegalArgumentException("expiresAt must be after issuedAt");
        }
        return this;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = new LinkedHashMap<>(additionalInformation);
    }

    @Override
    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(Instant.now());
    }

    @Override
    public Date getExpiration() {
        return expiresAt != null ? Date.from(expiresAt) : null;
    }

    @Override
    public int getExpiresIn() {
        return expiresAt != null ? Long.valueOf((Date.from(expiresAt).getTime() - System.currentTimeMillis()) / 1000L)
                .intValue() : 0;
    }

    @Override
    public String getValue() {
        return token;
    }

    @Override
    public String getTokenValue() {
        return token;
    }


    public static Instant toInstant(long seconds) {
        return Instant.ofEpochSecond(seconds * 1000L);
    }

    @Contract(pure = true)
    public static long toSeconds(@NotNull Instant instant) {
        return instant.toEpochMilli() / 1000L;
    }

    public static long timesBetween(@NotNull Instant before, @NotNull Instant after) {
        return after.toEpochMilli() - before.toEpochMilli();
    }
}
