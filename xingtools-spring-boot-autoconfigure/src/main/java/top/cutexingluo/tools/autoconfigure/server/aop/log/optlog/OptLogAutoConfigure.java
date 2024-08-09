package top.cutexingluo.tools.autoconfigure.server.aop.log.optlog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.aop.log.optlog.OptLogAop;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;


/**
 * OptLog 注解
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/1 22:59
 */
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "xingtools.enabled", value = "optlog-anno", havingValue = "true",
        matchIfMissing = false)
@Slf4j
public class OptLogAutoConfigure {
    @ConditionalOnMissingBean
    @Bean
    public OptLogAop optLogAop() {
        if (LogInfoAuto.enabled) log.info("OptLogAop ---->  {}", " 自定义操作 AOP，自动注册成功");
        return new OptLogAop();
    }
}
