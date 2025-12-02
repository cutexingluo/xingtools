package top.cutexingluo.tools.security.oauthserver.service;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;
import top.cutexingluo.core.basepackage.struct.ExtInitializable;
import top.cutexingluo.tools.utils.ee.redis.RYRedisCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * OAuth2AuthorizationConsent Redis 存储服务
 * <p>OAuth2AuthorizationConsentService 实现</p>
 * <p>
 * redis oauth2 authorization service (redis oauth2 授权同意服务)
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/6 17:33
 * @since 1.1.6
 */
@Data
public class RedisOAuth2AuthorizationConsentService implements ExtInitializable<RedisOAuth2AuthorizationConsentService>, OAuth2AuthorizationConsentService {

    @NotNull
    protected final RYRedisCache redisCache;

    /**
     * redis key
     */
    protected String rootKey = "common:auth:code";

    /**
     * keyBuilder key 提供器
     */
    protected BiFunction<String, String, String> keyBuilder;

    /**
     * 所有数据 缓存超时时间 (毫秒), -1 永久
     */
    protected long timeout = -1;

    public RedisOAuth2AuthorizationConsentService(@NotNull RYRedisCache redisCache) {
        Objects.requireNonNull(redisCache, "redisCache cannot be null");
        this.redisCache = redisCache;
    }

    public RedisOAuth2AuthorizationConsentService(@NotNull RYRedisCache redisCache, String rootKey) {
        Objects.requireNonNull(redisCache, "redisCache cannot be null");
        Objects.requireNonNull(rootKey, "rootKey cannot be null");
        this.redisCache = redisCache;
        this.rootKey = rootKey;
    }

    /**
     * 初始化
     */
    @Override
    public RedisOAuth2AuthorizationConsentService initSelf() {
        this.redisCache.setEnableTransactionSupport(true); // 开启事务支持
        return this;
    }

    /**
     * 批量保存
     */
    public void saveBatch(List<OAuth2AuthorizationConsent> authorizationConsents) {
        Assert.notNull(authorizationConsents, "authorizationConsents cannot be null");
        HashMap<String, OAuth2AuthorizationConsent> map = new HashMap<>(authorizationConsents.size());
        for (OAuth2AuthorizationConsent authorizationConsent : authorizationConsents) {
            Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
            String key = getKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
            map.put(key, authorizationConsent);
        }
        saveAction(() -> redisCache.setCacheMap(rootKey, map));
    }

    /**
     * 批量保存
     */
    public void saveBatch(Map<String, OAuth2AuthorizationConsent> authorizationConsentsMap) {
        Assert.notNull(authorizationConsentsMap, "authorizationConsents cannot be null");
        saveAction(() -> redisCache.setCacheMap(rootKey, authorizationConsentsMap));
    }

    /**
     * 初始化 rootKey 过期时间
     */
    public boolean initRootKeyTimeout() {
        return redisCache.expireCheck(rootKey, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String key = getKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
        this.redisCache.setCacheMapValue(rootKey, key, authorizationConsent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String key = getKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
        redisCache.deleteCacheMapValue(rootKey, key);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        String key = getKey(registeredClientId, principalName);
        return redisCache.getCacheMapValue(rootKey, key);
    }

    @NonNull
    protected String getKey(String registeredClientId, String principalName) {
        String key;
        if (keyBuilder == null) {
            key = String.valueOf(getId(registeredClientId, principalName));
        } else {
            key = keyBuilder.apply(registeredClientId, principalName);
        }
        return key;
    }

    /**
     * 保存时原子操作
     */
    protected void saveAction(Runnable runnable) {
        if (timeout == -1) {
            runnable.run();
        } else {
            redisCache.executeTransaction(new SessionCallback<Boolean>() {
                @Override
                public <K, V> Boolean execute(RedisOperations<K, V> operations) throws DataAccessException {
                    runnable.run();
                    return initRootKeyTimeout();
                }
            });
        }
    }

    protected static int getId(String registeredClientId, String principalName) {
        return Objects.hash(registeredClientId, principalName);
    }

    protected static int getId(@NonNull OAuth2AuthorizationConsent authorizationConsent) {
        return getId(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
    }


}
