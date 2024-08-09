package top.cutexingluo.tools.autoconfigure.server.aop.log.methodlog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.aop.log.methodlog.MethodLogAop;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;

/**
 * MethodLog 注解
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/16 16:05
 */
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.enabled", value = "methodlog-anno", havingValue = "true",
        matchIfMissing = false)
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MethodLogAutoConfigure {

    @ConditionalOnMissingBean
    @Bean
    public MethodLogAop methodLogAop() {
        if (LogInfoAuto.enabled) log.info("MethodLog Aop ---->  {}", "方法调用日志 AOP，自动注册成功");
        return new MethodLogAop();
    }
}
