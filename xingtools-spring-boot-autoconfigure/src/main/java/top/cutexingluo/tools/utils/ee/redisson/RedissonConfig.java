package top.cutexingluo.tools.utils.ee.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;

import java.io.IOException;

/**
 * RedissonClient 配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 22:31
 */
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "redisson-config", havingValue = "true", matchIfMissing = false)
//@ConditionalOnProperty(prefix = "spring.redis",name = "host",matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@Import(XTRedisUtilAutoConfigure.class)
@ConditionalOnClass({RedissonClient.class, Config.class})
public class RedissonConfig {

    public static final String PREFIX = "redis://";

    @Value("${spring.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.redis.port:6379}")
    private String redisPort;

    @ConditionalOnMissingBean
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        Config config = new Config();
        config.useSingleServer().setAddress(PREFIX + redisHost + ":" + redisPort);
        return Redisson.create(config);
    }


}
