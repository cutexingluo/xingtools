package top.cutexingluo.tools.autoconfigure.utils.ee.web.limit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.utils.web.limit.guava.LimitAop;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 14:09
 */
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.cloud.enabled", name = "current-limit", havingValue = "true")
@Configuration(proxyBeanMethods = false)
public class LimitAopAuto {


    @ConditionalOnMissingBean
    @Bean
    public LimitAop limitAop() {
        return new LimitAop();
    }

}
