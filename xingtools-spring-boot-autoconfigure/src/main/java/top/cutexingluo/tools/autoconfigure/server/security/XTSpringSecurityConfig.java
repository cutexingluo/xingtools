package top.cutexingluo.tools.autoconfigure.server.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.security.oauth.util.XTSignGlobal;

/**
 * 自动配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/27 18:29
 */

@ConditionalOnClass({TokenStore.class, JwtAccessTokenConverter.class})
//@AutoConfigureAfter(XTSecurityBeanProcessor.class)
@ConditionalOnBean({XingToolsAutoConfiguration.class})
@Configuration
@Slf4j
public class XTSpringSecurityConfig {


    @ConditionalOnMissingBean
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * JWT令牌校验工具
     */
    @ConditionalOnMissingBean
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //设置JWT签名密钥。可以是简单的MAC密钥，也可以是RSA密钥
        jwtAccessTokenConverter.setSigningKey(XTSignGlobal.getSign());
        return jwtAccessTokenConverter;
    }


}
