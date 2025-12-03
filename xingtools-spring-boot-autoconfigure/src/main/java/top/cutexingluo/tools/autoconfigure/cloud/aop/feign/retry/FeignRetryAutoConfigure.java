package top.cutexingluo.tools.autoconfigure.cloud.aop.feign.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.auto.cloud.XTSpringCloudAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;
import top.cutexingluo.tools.utils.ee.feign.retry.FeignRetryAop;

/**
 * 需要导入spring-retry 包
 * <br>
 * 使用方法：
 * <code>@FeignRetry(maxAttempt = 6, backoff = @Backoff(delay = 500L, maxDelay = 20000L, multiplier = 4))</code>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 15:49
 */
@ConditionalOnBean(XTSpringCloudAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtool.cloud.enabled", name = "feign-retry", havingValue = "true", matchIfMissing = false)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class FeignRetryAutoConfigure {


    @ConditionalOnMissingBean
    @Bean
    public FeignRetryAop feignRetryAop() {
        if(LogInfoAuto.enabled)log.info("FeignRetryAop init ---> {}", "自动装配完成");
        return new FeignRetryAop();
    }
}
