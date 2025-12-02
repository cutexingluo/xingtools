package top.cutexingluo.tools.autoconfigure.utils.ee.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.core.designtools.juc.thread.XTThreadPool;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;
import top.cutexingluo.tools.utils.ee.redis.RedisUtil;
import top.cutexingluo.tools.utils.ee.redis.XTRedisData;
import top.cutexingluo.tools.utils.ee.redis.XTRedisUtil;

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
@Configuration(proxyBeanMethods = false)
@Slf4j
public class XTRedisUtilAutoConfigure {


    @ConditionalOnMissingBean
    @Bean
    public XTRedisData xtRedisData(RedissonClient client, RedisUtil redisUtil) {
        if (LogInfoAuto.enabled)
            log.info("XTRedisUtilAutoConfigure   ---> XTRedisData  {}", "自动装配完成");
        XTThreadPool threadPool = new XTThreadPool();
        return new XTRedisData(redisUtil, threadPool, client);
    }


    @ConditionalOnMissingBean
    @Bean
    public XTRedisUtil xtRedisUtil(XTRedisData xtRedisData) {
        if (LogInfoAuto.enabled)
            log.info("XTRedisUtilAutoConfigure   ---> XTRedisUtil  {}", "自动装配完成");
        return new XTRedisUtil(xtRedisData);
    }
}
