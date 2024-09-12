package top.cutexingluo.tools.autoconfigure.utils.se.serializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.designtools.protocol.serializer.impl.json.JacksonSerializer;
import top.cutexingluo.tools.start.log.LogInfoAuto;

/**
 * Jackson 序列化器 自动配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/12 12:03
 * @since 1.1.4
 */

@ConditionalOnClass(com.fasterxml.jackson.databind.ObjectMapper.class)
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "jackson-serializer", havingValue = "true", matchIfMissing = false)
@Configuration(proxyBeanMethods = false)
@Slf4j
public class JacksonSerializerConfig {


    @ConditionalOnMissingBean(name = "jacksonSerializer")
    @Bean("jacksonSerializer")
    public JacksonSerializer jacksonSerializer() {
        if (LogInfoAuto.enabled)
            log.info("JacksonSerializer ---> {}", "jacksonSerializer 开启, 自动注册 Jackson 序列化器, 自动注册成功");
        return new JacksonSerializer();
    }

    @ConditionalOnMissingBean(name = "redisJacksonSerializer")
    @Bean("redisJacksonSerializer")
    public JacksonSerializer redisJacksonSerializer() {
        if (LogInfoAuto.enabled)
            log.info("JacksonSerializer ---> {}", "redisJacksonSerializer 开启, 自动注册 Redis Jackson 序列化器, 自动注册成功");
        return new JacksonSerializer().initRedis();
    }

}
