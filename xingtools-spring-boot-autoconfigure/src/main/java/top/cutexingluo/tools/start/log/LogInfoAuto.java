package top.cutexingluo.tools.start.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * xingtool 所有日志
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/26 16:59
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "xingtools.enabled", value = "log-info", havingValue = "false",
        matchIfMissing = false)
@Order(1)
@Slf4j
public class LogInfoAuto {
    public static boolean enabled = true;

    @ConditionalOnMissingBean(LogInfoDisable.class)
    @Bean
    public LogInfoDisable logInfo() {
        enabled = false;
        log.info("XingTool Log-info is disabled");
        return new LogInfoDisable();
    }
}
