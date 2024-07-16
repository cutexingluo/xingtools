package top.cutexingluo.tools.utils.ee.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;

import javax.annotation.PostConstruct;

/**
 * RedisUtil 工具
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/28 16:34
 */
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "redisconfig-util", havingValue = "true", matchIfMissing = false)
@AutoConfigureAfter(value = {RedisConfig.class, RedisTemplate.class}, name = {"xtRedisTemplate"})
@ConditionalOnBean(value = {XingToolsAutoConfiguration.class, RedisTemplate.class}, name = {"xtRedisTemplate"})
@Configuration
@Slf4j
public class RedisUtilAutoConfigure {

    public static RedisTemplate<String, Object> staticRedisTemplate;

    //    @Resource
    /**
     * 如果覆盖了RedisTemplate,需要把名称改为 xtRedisTemplate
     */
    @Autowired
    @Qualifier("xtRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void init() {
        RedisUtilAutoConfigure.staticRedisTemplate = redisTemplate;
    }

    @ConditionalOnMissingBean
    @Bean
    public QGRedisUtils qgRedisUtils() {
        if (LogInfoAuto.enabled)
            log.info("QGRedisUtils --->  RedisTemplate.class已存在,  Redis工具类  QGRedisUtils   {}", "自动注入完成");
        return new QGRedisUtils(redisTemplate);
    }

    @ConditionalOnMissingBean
    @Bean
    public RedisUtil redisUtil() {
        if (LogInfoAuto.enabled) log.info("RedisUtil --->  RedisTemplate.class已存在, Redis工具类 RedisUtil  {}", "自动注入完成");
        return new RedisUtil(redisTemplate);
    }

    /**
     * 推荐使用的工具
     */
    @ConditionalOnMissingBean
    @Bean
    public RYRedisCache redisCache() {
        if (LogInfoAuto.enabled)
            log.info("RYRedisCache --->  RedisTemplate.class已存在, Redis工具类 RYRedisCache  {}", "自动注入完成");
        return new RYRedisCache(redisTemplate);
    }

    @ConditionalOnMissingBean
    @Bean
    public RedisLockUtil redisLockUtil() {
        if (LogInfoAuto.enabled)
            log.info("RedisLockUtil --->  RedisTemplate.class已存在, Redis工具类 RedisLockUtil  {}", "自动注入完成");
        return new RedisLockUtil(redisTemplate);
    }

    @ConditionalOnMissingBean
    @Bean
    public RedisRepository redisRepository() {
        if (LogInfoAuto.enabled)
            log.info("RedisRepository --->  RedisTemplate.class已存在, Redis工具类 RedisRepository  {}", "自动注入完成");
        return new RedisRepository(redisTemplate);
    }

}
