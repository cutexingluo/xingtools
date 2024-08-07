package top.cutexingluo.tools.autoconfigure.server.aop.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.aop.exception.XTException;
import top.cutexingluo.tools.aop.exception.XTExceptionAop;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;

/**
 * XTException 注解  AOP
 * <p> {@link XTException} 新版默认关闭</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 12:41
 */
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "xingtools.xtexception-anno", value = "enabled", havingValue = "true",
        matchIfMissing = false)
//@EnableAspectJAutoProxy
@Slf4j
public class XTExceptionAutoConfigure {
    @ConditionalOnMissingBean(XTExceptionAop.class)
    @Bean
    public XTExceptionAop xtExceptionAop() {
        if (LogInfoAuto.enabled) log.info("XTExceptionAop ---->  {}", "异常拦截AOP，自动注册成功");
        return new XTExceptionAop();
    }
}
