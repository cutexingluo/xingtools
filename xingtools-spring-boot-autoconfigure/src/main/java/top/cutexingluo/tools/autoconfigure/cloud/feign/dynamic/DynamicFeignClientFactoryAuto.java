package top.cutexingluo.tools.autoconfigure.cloud.feign.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.cutexingluo.tools.auto.cloud.XTSpringCloudAutoConfiguration;
import top.cutexingluo.tools.cloud.feign.dynamic.DynamicClient;
import top.cutexingluo.tools.cloud.feign.dynamic.DynamicFeignClientFactory;
import top.cutexingluo.tools.start.log.LogInfoAuto;

/**
 * dynamic-feign 开启
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 13:05
 */
@ConditionalOnBean(XTSpringCloudAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtool.cloud.enabled", name = "dynamic-feign", havingValue = "true", matchIfMissing = false)
@Configuration(proxyBeanMethods = false)
@Slf4j
@Import(DynamicClient.class)
public class DynamicFeignClientFactoryAuto {
    @ConditionalOnMissingBean
    @Bean
    public <T> DynamicFeignClientFactory<T> dynamicFeignClientFactory() {
        if (LogInfoAuto.enabled) log.info("DynamicFeignClientFactory init ---> {}", "自动装配完成");
        return new DynamicFeignClientFactory<>();
    }

}
