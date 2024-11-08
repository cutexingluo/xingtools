package top.cutexingluo.tools.security.oauthserver.manager;

import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * 资源服务配置管理器
 *
 * <p>通过 实现并配置 bean</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/1 18:13
 * @since 1.1.6
 */
public interface ResourceServerConfigManager {

    /**
     * 令牌解析器
     * <p>token -> OAuth2AuthenticatedPrincipal</p>
     * <h2>描述 ：处理不透明令牌的方法</h2>
     * 不透明令牌是指在令牌中不包含任何实际信息，而是包含一个指向的令牌服务器中包含真实信息的令牌的标识符。
     * 在OAuth2中，Bearer令牌通常就是不透明令牌。OpaqueTokenIntrospector的实现类可以通过向令牌服务器发送请求获取令牌的真实信息，并
     * 将其作为认证信息保存在Spring Security的SecurityContext中，以便后续的鉴权操作。
     */
    default OpaqueTokenIntrospector opaqueTokenIntrospector() {
//        return new SpringOpaqueTokenIntrospector();
        return null;
    }
}
