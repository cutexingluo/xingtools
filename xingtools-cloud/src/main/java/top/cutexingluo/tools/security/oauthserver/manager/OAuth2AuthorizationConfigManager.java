package top.cutexingluo.tools.security.oauthserver.manager;

import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

/**
 * 授权配置管理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/1 18:25
 */
public interface OAuth2AuthorizationConfigManager {


    /**
     * 授权同意服务
     *
     * <h2>描述 ： 用于管理新的和现有的授权许可</h2>
     * <h3>它是存储新授权同意和查询现有授权同意的中心组件。</h3>
     * <li>例如，
     *  <ol>它主要由实现OAuth2授权请求流的组件使用</ol>
     * </li>
     */
    default OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
    }


    /**
     * 授权服务
     * <h2>描述 ：用于管理新的和现有的授权信息</h2>
     * <h3>它是存储新授权和查询现有授权的中心组件。</h3>
     * <p>其他组件在遵循特定的协议流时使用它 </p>
     * <li>例如，
     * <ul>
     *         <li>客户端身份验证</li>
     *         <li>授权授予处理</li>
     *         <li>令牌自省</li>
     *         <li>令牌撤销</li>
     *         <li>动态客户端注册</li>
     *         <li>.....等</li>
     * </ul>
     * </li>
     */
    default OAuth2AuthorizationService oAuth2AuthorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }


}
