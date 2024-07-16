package top.cutexingluo.tools.autoconfigure.server.aop.log.xtlog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.aop.log.xtlog.aop.WebLogAspect;
import top.cutexingluo.tools.aop.log.xtlog.strategy.impl.DefaultWebLogStrategy;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/13 15:12
 */
@Slf4j
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "web-log-aop", havingValue = "true", matchIfMissing = false)
@Configuration
public class WebLogAutoConfigure {
    @ConditionalOnMissingBean
    @Bean
    public WebLogAspect webLogAspect() {
        if (LogInfoAuto.enabled) log.info("WebLogAspect ---->  {}", "WebLog 注解 AOP，自动注入成功");
        return new WebLogAspect();
    }

    @ConditionalOnMissingBean
    @Bean
    public DefaultWebLogStrategy defaultWebLogStrategy() {
        if (LogInfoAuto.enabled) log.info("DefaultWebLogStrategy ---->  {}", "WebLog 注解 AOP 默认策略，自动注入成功");
        return new DefaultWebLogStrategy();
    }

}
