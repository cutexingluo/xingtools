package top.cutexingluo.tools.autoconfigure.utils.ee.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.autoconfigure.utils.se.serializer.JacksonSerializerConfig;
import top.cutexingluo.tools.designtools.protocol.serializer.impl.json.JacksonSerializer;
import top.cutexingluo.tools.start.log.LogInfoAuto;
import top.cutexingluo.tools.utils.ee.redis.core.RedisConfigInitFactory;


/**
 * Redis 配置
 *
 * @author XingTian
 */
@AutoConfigureAfter({RedisConnectionConfig.class, JacksonSerializerConfig.class})
@ConditionalOnClass(GenericJackson2JsonRedisSerializer.class)
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "redisconfig", havingValue = "true", matchIfMissing = false)
@Configuration(proxyBeanMethods = false)
@Import({RedisConnectionConfig.class})
@Slf4j
public class RedisConfig {

    @Autowired(required = false)
    @Qualifier("redisJacksonSerializer")
    protected JacksonSerializer redisJacksonSerializer;


    @ConditionalOnProperty(prefix = "xingtools.enabled", name = "redisconfig-setting", havingValue = "jackson", matchIfMissing = true)
    @ConditionalOnBean(RedisConnectionFactory.class)
//    @ConditionalOnMissingBean(name = {"xtRedisTemplate"})  // 有一个 stringRedisTemplate
    @ConditionalOnMissingBean(name = {"xtRedisTemplate"})
    @Bean("xtRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        boolean checkJacksonSerializer = redisJacksonSerializer != null;
        ObjectMapper objectMapper = !checkJacksonSerializer ? null : redisJacksonSerializer.getObjectMapper();
        RedisTemplate<String, Object> template = RedisConfigInitFactory.initTemplate(redisConnectionFactory, objectMapper);
        if (LogInfoAuto.enabled) log.info("RedisTemplate ---> 已发现RedisConnectionFactory  , {} , jackson序列化  {}",
                checkJacksonSerializer ? "全局序列化器 redisJacksonSerializer 存在" : "每次将新建序列化器",
                "自动配置成功");
        return template;
    }

    @ConditionalOnProperty(prefix = "xingtools.enabled", name = "redisconfig-setting", havingValue = "fastjson", matchIfMissing = false)
    @ConditionalOnBean(RedisConnectionFactory.class)
    @ConditionalOnMissingBean(name = {"xtRedisTemplate"})
    @Bean("xtRedisTemplate")
    public RedisTemplate<String, Object> fastJsonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = RedisConfigInitFactory.initTemplateByFastJson(redisConnectionFactory);
        if (LogInfoAuto.enabled) log.info("RedisTemplate ---> 已发现RedisConnectionFactory  , fastjson序列化 {}", "自动配置成功");
        return template;
    }

}