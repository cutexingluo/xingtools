package top.cutexingluo.tools.autoconfigure.server.aop.log.printlog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.aop.log.printlog.PrintLog;
import top.cutexingluo.tools.aop.log.printlog.PrintLogAop;
import top.cutexingluo.tools.aop.log.printlog.custom.PrintLogAdapter;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;


/**
 * PrintLog 注解
 * <p> {@link PrintLog} 新版默认关闭</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/1 22:59
 */
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "xingtools.printlog-anno", value = "enabled", havingValue = "true",
        matchIfMissing = false)
//@EnableAspectJAutoProxy
@Slf4j
public class PrintLogAutoConfigure {

    @Autowired(required = false)
    private PrintLogAdapter printLogAdapter;

    @ConditionalOnMissingBean(PrintLogAop.class)
    @Bean
    public PrintLogAop printLogAop() {
        if (LogInfoAuto.enabled) log.info("PrintLogAop ---> {}", "打印输出AOP，自动注册成功");
        return new PrintLogAop(printLogAdapter);
    }
}
