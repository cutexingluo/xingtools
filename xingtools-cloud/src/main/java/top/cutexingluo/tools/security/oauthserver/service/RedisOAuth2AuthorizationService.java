package top.cutexingluo.tools.security.oauthserver.service;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;
import top.cutexingluo.tools.utils.ee.redis.RYRedisCache;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * OAuth2Authorization Redis 存储服务
 * <p>OAuth2AuthorizationService 实现</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/6 16:47
 * @since 1.1.6
 */
@Data
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    @NotNull
    protected final RYRedisCache redisCache;

    /**
     * redis key prefix, Stores "completed" authorizations, where an access token has been granted.
     * <p>存储已授权的</p>
     */
    protected String rootPrefix = "common:auth:authorizations:";

    /**
     * redis key prefix, Stores "initialized" (uncompleted) authorizations, where an access token has not yet been granted.
     * <p>存储初始化, 未授权的</p>
     */
    protected String notGrantRootPrefix = "common:auth:initializedAuthorizations:";

    /**
     * 是否分类未授权数据,
     */
    protected boolean classifyNotGranted = true;


//    /**
//     * tokenType 匹配 class map
//     * <p>key 为 class name</p>
//     * <p>value 为 token  class类型</p>
//     */
//    protected Map<String, Class<? extends OAuth2Token>> classMap;
    /**
     * tokenType 匹配 matches map
     * <p>key  为 tokenType 的value 值</p>
     * <p>value 为判定器（可为空） 如果为空，需要 tokenTypeClassMap 有对应值</p>
     */
    protected Map<String, BiPredicate<OAuth2Authorization, String>> tokenTypeMatchesMap;

    /**
     * tokenType 匹配 class map
     * <p>key  为 tokenType 的value 值</p>
     * <p>value 为 token  class类型</p>
     */
    protected Map<String, Class<? extends OAuth2Token>> tokenTypeClassMap;

    /**
     * 所有数据 缓存超时时间 (毫秒), -1 永久
     */
    protected long timeout = -1;

    public RedisOAuth2AuthorizationService(@NotNull RYRedisCache redisCache) {
        Objects.requireNonNull(redisCache, "redisCache cannot be null");
        this.redisCache = redisCache;
        this.redisCache.setEnableTransactionSupport(true); // 开启事务支持
    }

    public RedisOAuth2AuthorizationService(@NotNull RYRedisCache redisCache, String rootPrefix, String notGrantRootPrefix) {
        Objects.requireNonNull(redisCache, "redisCache cannot be null");
        Objects.requireNonNull(rootPrefix, "rootPrefix cannot be null");
        Objects.requireNonNull(notGrantRootPrefix, "notGrantRootPrefix cannot be null");
        this.redisCache = redisCache;
        this.rootPrefix = rootPrefix;
        this.notGrantRootPrefix = notGrantRootPrefix;
        this.redisCache.setEnableTransactionSupport(true);  // 开启事务支持
    }

    /**
     * 初始化 tokenType 匹配 map
     */
    public RedisOAuth2AuthorizationService initMap(
            Map<String, BiPredicate<OAuth2Authorization, String>> tokenTypeMatchesMap,
            Map<String, Class<? extends OAuth2Token>> tokenTypeClassMap
    ) {
        if (this.tokenTypeMatchesMap == null) {
            this.tokenTypeMatchesMap = tokenTypeMatchesMap != null ? tokenTypeMatchesMap : new HashMap<>();
        } else {
            if (tokenTypeMatchesMap != null) this.tokenTypeMatchesMap.putAll(tokenTypeMatchesMap);
        }
        if (this.tokenTypeClassMap == null) {
            this.tokenTypeClassMap = tokenTypeClassMap != null ? tokenTypeClassMap : new HashMap<>();
        } else {
            if (tokenTypeClassMap != null) this.tokenTypeClassMap.putAll(tokenTypeClassMap);
        }
        return this;
    }

    /**
     * 初始化默认 map
     */
    public RedisOAuth2AuthorizationService initDefaultMap() {
        Map<String, BiPredicate<OAuth2Authorization, String>> matchesMap = new HashMap<>();
        Map<String, Class<? extends OAuth2Token>> classMap = new HashMap<>();

        // OAuth2ParameterNames.STATE
        matchesMap.put(OAuth2ParameterNames.STATE, RedisOAuth2AuthorizationService::matchesState);

        // OAuth2ParameterNames.CODE , OAuth2AuthorizationCode
        matchesMap.put(OAuth2ParameterNames.CODE, null);
        classMap.put(OAuth2ParameterNames.CODE, OAuth2AuthorizationCode.class);

        // OAuth2ParameterNames.ACCESS_TOKEN , OAuth2AccessToken , OAuth2TokenType.ACCESS_TOKEN
        matchesMap.put(OAuth2ParameterNames.ACCESS_TOKEN, null);
        classMap.put(OAuth2ParameterNames.ACCESS_TOKEN, OAuth2AccessToken.class);

        // OidcParameterNames.ID_TOKEN , OidcIdToken
        matchesMap.put(OidcParameterNames.ID_TOKEN, null);
        classMap.put(OidcParameterNames.ID_TOKEN, OidcIdToken.class);

        // OAuth2ParameterNames.REFRESH_TOKEN , OAuth2RefreshToken , OAuth2TokenType.REFRESH_TOKEN
        matchesMap.put(OAuth2ParameterNames.REFRESH_TOKEN, null);
        classMap.put(OAuth2ParameterNames.REFRESH_TOKEN, OAuth2RefreshToken.class);

        return this.initMap(matchesMap, classMap);
    }


    public RedisOAuth2AuthorizationService tokenTypeMatchesMapPeek(Consumer<Map<String, BiPredicate<OAuth2Authorization, String>>> consumer) {
        if (consumer != null) consumer.accept(tokenTypeMatchesMap);
        return this;
    }

    public RedisOAuth2AuthorizationService tokenTypeClassMapPeek(Consumer<Map<String, Class<? extends OAuth2Token>>> consumer) {
        if (consumer != null) consumer.accept(tokenTypeClassMap);
        return this;
    }


    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        if (isComplete(authorization)) {
            this.redisCache.setCacheObject(rootPrefix + authorization.getId(), authorization, timeout, TimeUnit.MILLISECONDS);
        } else {
            this.redisCache.setCacheObject(notGrantRootPrefix + authorization.getId(), authorization, timeout, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        if (isComplete(authorization)) {
            this.redisCache.deleteObject(rootPrefix + authorization.getId());
        } else {
            this.redisCache.deleteObject(notGrantRootPrefix + authorization.getId());
        }
    }

    @Nullable
    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        OAuth2Authorization authorization = this.redisCache.getCacheObject(rootPrefix + id, OAuth2Authorization.class);
        return authorization != null ?
                authorization :
                this.redisCache.getCacheObject(notGrantRootPrefix + id, OAuth2Authorization.class);
    }

    @Nullable
    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        Set<String> rootPrefixKeys = this.redisCache.keys(rootPrefix + "*");
        List<Object> objects = this.redisCache.getMultiCacheObject(rootPrefixKeys);
        for (Object obj : objects) {
            if (obj instanceof OAuth2Authorization) {
                OAuth2Authorization authorization = (OAuth2Authorization) obj;
                if (hasToken(authorization, token, tokenType)) {
                    return authorization;
                }
            }
        }
        Set<String> notGrantRootPrefixKeys = this.redisCache.keys(notGrantRootPrefix + "*");
        List<Object> notGrantObjects = this.redisCache.getMultiCacheObject(notGrantRootPrefixKeys);
        for (Object obj : notGrantObjects) {
            if (obj instanceof OAuth2Authorization) {
                OAuth2Authorization authorization = (OAuth2Authorization) obj;
                if (hasToken(authorization, token, tokenType)) {
                    return authorization;
                }
            }
        }
        return null;
    }


    private static boolean isComplete(@NotNull OAuth2Authorization authorization) {
        return authorization.getAccessToken() != null;
    }

    private boolean hasToken(OAuth2Authorization authorization, String token, @Nullable OAuth2TokenType tokenType) {
        boolean matches = false;
        if (tokenType == null) { // 遍历所有
            for (Map.Entry<String, BiPredicate<OAuth2Authorization, String>> entry : tokenTypeMatchesMap.entrySet()) {
                BiPredicate<OAuth2Authorization, String> predicate = entry.getValue();
                if (predicate == null) { // 如果key存在, predicate 不存在则使用默认匹配器
                    Class<? extends OAuth2Token> tokenClass = tokenTypeClassMap.get(entry.getKey());
                    if (tokenClass != null) { // 映射关系不为空 使用默认匹配器
                        matches = matchesToken(authorization, tokenClass, token);
                    }
                } else {
                    matches = predicate.test(authorization, token);
                }
                if (matches) {
                    return true;
                }
            }
        } else {
            BiPredicate<OAuth2Authorization, String> predicate = tokenTypeMatchesMap.get(tokenType.getValue());
            if (predicate != null) {
                return predicate.test(authorization, token);
            }
        }

        return false;
    }

    private static boolean matchesState(@NotNull OAuth2Authorization authorization, @NotNull String token) {
        return token.equals(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }

    /**
     * 判断是否匹配 (默认匹配器)
     */
    public static <T extends OAuth2Token> boolean matchesToken(@NotNull OAuth2Authorization authorization, Class<T> tokenType, String token) {
        OAuth2Authorization.Token<T> authorizationToken = authorization.getToken(tokenType);
        return authorizationToken != null && authorizationToken.getToken().getTokenValue().equals(token);
    }


}
