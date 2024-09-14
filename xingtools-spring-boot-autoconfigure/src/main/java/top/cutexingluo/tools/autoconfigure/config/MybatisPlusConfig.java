package top.cutexingluo.tools.autoconfigure.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.start.log.LogInfoAuto;


/**
 * MybatisPlus 分页插件
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/9/24 11:34
 */
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnClass({MybatisPlusInterceptor.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "mybatis-plus-config", havingValue = "true", matchIfMissing = false)
//@MapperScan("scan.your.mapper.package")
//@MapperScan("com.xing.springboot.mapper")
@Slf4j
public class MybatisPlusConfig {

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @ConditionalOnMissingBean
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        if (LogInfoAuto.enabled) log.info("MybatisPlusConfig ---> {}", "分页插件配置，自动注册成功");
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.setUseDeprecatedExecutor(false);
//    }
}

