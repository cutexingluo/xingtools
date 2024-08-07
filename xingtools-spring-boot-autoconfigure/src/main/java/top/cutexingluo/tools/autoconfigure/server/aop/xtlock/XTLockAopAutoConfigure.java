package top.cutexingluo.tools.autoconfigure.server.aop.xtlock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.designtools.juc.lock.XTAopLockAop;
import top.cutexingluo.tools.start.log.LogInfoAuto;


/**
 * XTAopLock 注解
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 13:54
 */
//@ConditionalOnBean(XingToolsAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "xt-aop-lock", havingValue = "true", matchIfMissing = false)
//@EnableAspectJAutoProxy
@ConditionalOnClass({RedissonClient.class, Config.class})
@Slf4j
public class XTLockAopAutoConfigure {
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnMissingBean
    @Bean
    public XTAopLockAop xtAopLockAop() {
        if (LogInfoAuto.enabled) log.info("LogInfoAuto ---->  {}", "XT锁注解AOP，自动注册成功");
        return new XTAopLockAop();
    }
}
