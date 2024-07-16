package top.cutexingluo.tools.utils.ee.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import top.cutexingluo.tools.start.log.LogInfoAuto;

/**
 * Redis 连接工厂
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/28 16:26
 */

@ConditionalOnClass({RedisConnectionFactory.class, JedisConnectionFactory.class})
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "redisconfig", havingValue = "true", matchIfMissing = false)
@Configuration(proxyBeanMethods = false)
@EnableRedisRepositories
@Slf4j
public class RedisConnectionConfig {

//    @PostConstruct
//    public void init() {
//        log.info("RedisConnectionConfig 已开启， 请自行导入 连接池 , 例如 commons-pool2 连接池");
//    }


//    @ConditionalOnMissingBean
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        log.info("RedisConnectionConfig ---> {}", "默认Jedis连接已开启，请自行导入Jedis依赖项");
//        return new JedisConnectionFactory();
//    }


    @ConditionalOnMissingBean
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        if(LogInfoAuto.enabled)log.info("RedisConnectionConfig ---> {}", "默认Jedis连接已开启，请自行导入Jedis依赖项");
        return new JedisConnectionFactory();

    }

//    @ConditionalOnMissingBean
//    @Bean
//    JedisConnectionConfiguration(RedisProperties properties, ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration, ObjectProvider<RedisClusterConfiguration> clusterConfiguration) {
//        super(properties, sentinelConfiguration, clusterConfiguration);
//    }

}
