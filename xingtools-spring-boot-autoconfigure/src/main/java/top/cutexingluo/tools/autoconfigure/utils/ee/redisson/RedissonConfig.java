package top.cutexingluo.tools.autoconfigure.utils.ee.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;

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
@ConditionalOnClass({RedissonClient.class, Config.class})
@Slf4j
public class RedissonConfig {

    public static final String PREFIX = "redis://";

    @Autowired
    private RedisProperties redisProperties;

    @ConditionalOnMissingBean
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        String redisHost = redisProperties.getHost();
        int redisPort = redisProperties.getPort();
        String password = redisProperties.getPassword();
        int database = redisProperties.getDatabase();

        Config config = new Config();
        config.useSingleServer()
                .setAddress(PREFIX + redisHost + ":" + redisPort)
                .setPassword(password)
                .setDatabase(database);
        if (LogInfoAuto.enabled)
            log.info("RedissonConfig   ---> RedissonClient  {}", "自动装配完成");
        return Redisson.create(config);
    }


}
