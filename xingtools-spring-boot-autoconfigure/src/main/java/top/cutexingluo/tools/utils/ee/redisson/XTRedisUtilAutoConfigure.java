package top.cutexingluo.tools.utils.ee.redisson;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.designtools.juc.thread.XTThreadPool;
import top.cutexingluo.tools.utils.ee.redis.RedisUtil;
import top.cutexingluo.tools.utils.ee.redis.XTRedisData;

/**
 * RedissonClient 配置-附加数据 XTRedisData
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/28 22:32
 */
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "redisson-config", havingValue = "true", matchIfMissing = false)
@ConditionalOnBean({XingToolsAutoConfiguration.class, RedissonClient.class, RedisUtil.class})
@AutoConfigureAfter(RedissonConfig.class)
public class XTRedisUtilAutoConfigure {


    @ConditionalOnMissingBean
    @Bean
    public XTRedisData xtRedisData(RedissonClient client, RedisUtil redisUtil) {
        XTThreadPool threadPool = new XTThreadPool();
        return new XTRedisData(redisUtil, threadPool, client);
    }
}
