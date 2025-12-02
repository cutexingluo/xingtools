package top.cutexingluo.tools.autoconfigure.satoken;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
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
 * satoken - jwt
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/4 22:54
 */
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "satokenjwt", havingValue = "true", matchIfMissing = false)
@ConditionalOnClass({StpUtil.class, StpLogic.class, StpLogicJwtForSimple.class})
@Configuration(proxyBeanMethods = false)
@Slf4j
public class SaTokenConfiguration {
    // Sa-Token 整合 jwt (Simple 简单模式)
    @ConditionalOnMissingBean
    @Bean
    public StpLogic getStpLogicJwt() {
        if (LogInfoAuto.enabled) log.info("SaTokenConfiguration ---> {}", "Sa-Token 整合 jwt (Simple 简单模式) 配置，自动注册成功");
        return new StpLogicJwtForSimple();
    }
}

