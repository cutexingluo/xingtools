package top.cutexingluo.tools.autoconfigure.server.aop.transaction;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.cutexingluo.tools.aop.transactional.ExtTransactionalAop;
import top.cutexingluo.tools.aop.transactional.TransactionalUtils;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;


/**
 * ExtTransactional 注解
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 12:56
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DataSourceTransactionManager.class)
@ConditionalOnBean({XingToolsAutoConfiguration.class, DataSourceTransactionManager.class})
@ConditionalOnProperty(prefix = "xingtools.ext-transaction-anno", value = "enabled", havingValue = "true",
        matchIfMissing = false)
@Import({TransactionalUtils.class})
//@EnableAspectJAutoProxy
@EnableTransactionManagement
@Slf4j
public class ExtTransactionAutoConfigure {
    @ConditionalOnMissingBean
    @Bean
    public ExtTransactionalAop extTransactionAop() {
        if (LogInfoAuto.enabled) log.info("ExtTransactionalAop ---->  {}", "Ext事务注解AOP，自动注册成功");
        return new ExtTransactionalAop();
    }

}
