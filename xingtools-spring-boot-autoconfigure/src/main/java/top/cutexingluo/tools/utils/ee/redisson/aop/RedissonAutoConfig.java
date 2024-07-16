package top.cutexingluo.tools.utils.ee.redisson.aop;

import com.baomidou.lock.DefaultLockKeyBuilder;
import com.baomidou.lock.LockFailureStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;
import top.cutexingluo.tools.utils.ee.redisson.CustomLockKeyBuilder;
import top.cutexingluo.tools.utils.ee.redisson.GrabLockFailureStrategy;

import javax.annotation.PostConstruct;

/**
 * Redisson 自带注解
 * <p>需要导入  com.baomidou:lock4j-core 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 18:33
 */
@Slf4j
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnClass({DefaultLockKeyBuilder.class})
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "redisson-aop", havingValue = "true")
@Configuration
public class RedissonAutoConfig {

    @PostConstruct
    public void init() {
        if (LogInfoAuto.enabled)
            log.info("RedissonAutoConfig RedissonAop   --->  {}", "自动装配完成");
    }

    @ConditionalOnMissingBean(DefaultLockKeyBuilder.class)
    @Bean
    public DefaultLockKeyBuilder lockKeyBuilder(BeanFactory beanFactory) {
        return new CustomLockKeyBuilder(beanFactory);
    }

    @ConditionalOnMissingBean(LockFailureStrategy.class)
    @Bean
    public LockFailureStrategy lockFailureStrategy() {
        return new GrabLockFailureStrategy();
    }
}
