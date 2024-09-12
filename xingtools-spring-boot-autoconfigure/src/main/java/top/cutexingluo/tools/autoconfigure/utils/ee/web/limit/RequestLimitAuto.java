package top.cutexingluo.tools.autoconfigure.utils.ee.web.limit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;
import top.cutexingluo.tools.utils.web.limit.submit.aop.RequestLimitAspect;


/**
 * 请求限制 RequestLimitAspect AOP
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/5 23:02
 * @since 1.0.4
 */
@Slf4j
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "request-limit", havingValue = "true", matchIfMissing = false)
@Configuration(proxyBeanMethods = false)
public class RequestLimitAuto {

    @ConditionalOnMissingBean(RequestLimitAspect.class)
    @Bean
    public RequestLimitAspect requestLimitAspect() {
        if (LogInfoAuto.enabled) log.info("RequestLimitAspect ---> {}", "请求限制 AOP，自动注册成功");
        return new RequestLimitAspect();
    }
}
