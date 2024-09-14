package top.cutexingluo.tools.autoconfigure.utils.ee.web.limit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;
import top.cutexingluo.tools.utils.ee.redis.RYRedisCache;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.impl.RedisLimitStrategy;

/**
 * RedisLimitStrategy redis 限制策略
 * <p>需要导入 Redis 相关包，RedisTemplate 导入</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/6 11:16
 * @since 1.0.4
 */
@Slf4j
@ConditionalOnClass({RYRedisCache.class, RedisTemplate.class})
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "request-limit-redis", havingValue = "true", matchIfMissing = false)
@Configuration(proxyBeanMethods = false)
public class RedisLimitStrategyAuto {

    @ConditionalOnBean({RedisTemplate.class})
    @ConditionalOnMissingBean
    @Bean
    public RedisLimitStrategy redisLimitStrategy(@Autowired(required = false) RYRedisCache redisCache, RedisTemplate<String, Object> redisTemplate) {
        if (LogInfoAuto.enabled) log.info("RedisLimitStrategy ---> {}", "RequestLimitAspect 开启 自动注册 redis 策略，自动注册成功");
        return new RedisLimitStrategy(redisCache, redisTemplate);
    }
}
