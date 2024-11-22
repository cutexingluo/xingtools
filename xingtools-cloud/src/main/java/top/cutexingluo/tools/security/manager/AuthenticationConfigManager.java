package top.cutexingluo.tools.security.manager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;

/**
 * 认证配置管理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/4 9:48
 * @since 1.1.6
 */
public interface AuthenticationConfigManager {

    /**
     * 认证管理器
     *
     * <p>可以是对 Authentication 对象的操作，也可以是一组 AuthenticationProvider 的组合操作</p>
     */
    default AuthenticationManager authenticationManager() {
        return null;
    }


    /**
     * 认证转换器
     * <p>AuthenticationConverter是Spring Security中的一个接口，
     * 它用于将来自请求中的认证信息（例如token、用户名/密码、证书等）转换为Authentication对象，
     * 以便后续的认证流程能够使用它来进行鉴权。</p>
     *
     * <pre>
     * authorizationServerConfigurer.tokenEndpoint(tokenEndpoint -> {
     *       tokenEndpoint.accessTokenRequestConverter(new DelegatingAuthenticationConverter(converterList) );
     * });
     * </pre>
     */
    default AuthenticationConverter authenticationConverter() {
        return new BasicAuthenticationConverter();
    }

    /**
     * 认证提供者
     *
     * <p>通过解析上游 Converter 提供的结果，核心操作后，返回AccessToken 的 Authentication 对象</p>
     * <pre>
     *       AuthenticationProvider接口具有两个核心方法，分别是：
     *      1.这个方法接收一个Authentication对象作为参数，表示用户提供的身份验证信息。
     *      2.该方法会将用户提供的身份验证信息与内部存储的信息进行比对，如果验证通过，则返回一个新的Authentication对象，
     *      表示该用户已经被认证。如果验证失败，则会抛出一个AuthenticationException异常。
     * </pre>
     * <pre>
     * http.authenticationProvider(authenticationProvider)
     * </pre>
     */
    default AuthenticationProvider authenticationProvider() {
        return new DaoAuthenticationProvider();
    }


    /**
     * 认证成功处理器
     * <pre>
     *     authorizationServerConfigurer.tokenEndpoint(tokenEndpoint -> {
     *             tokenEndpoint.accessTokenResponseHandler(new AuthenticationSuccessHandler());
     *         });
     * </pre>
     */
    default AuthenticationSuccessHandler authenticationSuccessHandler() {
        return null;
    }

    /**
     * 认证失败处理器
     * <pre>
     *     authorizationServerConfigurer.tokenEndpoint(tokenEndpoint -> {
     *             tokenEndpoint.errorResponseHandler((new AuthenticationFailureHandler());
     *         });
     * </pre>
     */
    default AuthenticationFailureHandler authenticationFailureHandler() {
        return null;
    }

}
