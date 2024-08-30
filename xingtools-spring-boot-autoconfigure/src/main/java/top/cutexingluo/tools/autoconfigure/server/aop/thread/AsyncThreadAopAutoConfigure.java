package top.cutexingluo.tools.autoconfigure.server.aop.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import top.cutexingluo.tools.aop.thread.AsyncThreadAop;
import top.cutexingluo.tools.aop.thread.ThreadResults;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;


/**
 * AsyncThread
 * <p>@MainThread @SonThread 注解</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/2 21:49
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({PlatformTransactionManager.class})
@ConditionalOnBean({XingToolsAutoConfiguration.class, PlatformTransactionManager.class})
@ConditionalOnProperty(prefix = "xingtools.enabled", value = "async-thread-aop-anno", havingValue = "true",
        matchIfMissing = false)
@Slf4j
public class AsyncThreadAopAutoConfigure {

    @ConditionalOnMissingBean
    @Bean
    public AsyncThreadAop asyncThreadAop(PlatformTransactionManager transactionManager) {
        if (LogInfoAuto.enabled) log.info("AsyncThreadAop ---->  {}",
                "异步注解Aop @MainThread  @SonThread ，自动注册成功");
        return new AsyncThreadAop(transactionManager);
    }

    @ConditionalOnMissingBean(name = "threadResults")
    @Bean
    public ThreadResults threadResults(AsyncThreadAop asyncThreadAop) {
        if (LogInfoAuto.enabled) log.info("ThreadResults ---->  {}",
                "异步线程结果 ThreadResults，自动注册成功");
        return new ThreadResults(asyncThreadAop.map);
    }
}
