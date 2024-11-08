package top.cutexingluo.tools.security.oauth.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import top.cutexingluo.tools.utils.ee.redis.RedisRepository;

import java.util.concurrent.TimeUnit;

/**
 * Redis 授权码服务 方式 <br>
 * 需要提前注入RedisTemplate
 *
 * <p>需要导入 spring-data-redis 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/12 21:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {
    /**
     * redis key prefix
     */
    private String authCodePrefix = "common:auth:code:";

    private final RedisRepository redisRepository;
    private final RedisSerializer<Object> valueSerializer;

    public RedisAuthorizationCodeServices(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
        this.valueSerializer = RedisSerializer.java();
    }

    public RedisAuthorizationCodeServices setAuthCodePrefix(String authCodePrefix) {
        this.authCodePrefix = authCodePrefix;
        return this;
    }

    public String getAuthCodePrefix() {
        return authCodePrefix;
    }


    /**
     * 将存储code到redis，并设置过期时间，10分钟
     */
    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        redisRepository.setExpire(redisKey(code), authentication, 10, TimeUnit.MINUTES, valueSerializer);
    }

    @Override
    protected OAuth2Authentication remove(final String code) {
        String codeKey = redisKey(code);
        OAuth2Authentication token = (OAuth2Authentication) redisRepository.get(codeKey, valueSerializer);
        redisRepository.del(codeKey);
        return token;
    }

    /**
     * redis中 code key的前缀
     */
    private String redisKey(String code) {
        return authCodePrefix + code;
    }
}
