package top.cutexingluo.tools.autoconfigure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;

import javax.sql.DataSource;

/**
 * 事务配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/28 11:34
 */
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "transaction-config", havingValue = "true", matchIfMissing = false)
@Slf4j
public class TransactionConfig {

    @ConditionalOnMissingBean
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        if (LogInfoAuto.enabled) log.info("TransactionConfig ---> {}", "事务管理器配置，自动注册成功");
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    @ConditionalOnMissingBean
    @Bean
    public TransactionTemplate transactionTemplate(DataSourceTransactionManager transactionManager) {
        if (LogInfoAuto.enabled) log.info("TransactionConfig ---> {}", "事务模板配置，自动注册成功");
        return new TransactionTemplate(transactionManager);
    }
}
