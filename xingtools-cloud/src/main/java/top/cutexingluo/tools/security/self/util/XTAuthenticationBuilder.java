package top.cutexingluo.tools.security.self.util;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import top.cutexingluo.core.basepackage.struct.ExtInitializable;
import top.cutexingluo.core.designtools.builder.XTBuilder;
import top.cutexingluo.tools.bridge.servlet.HttpServletRequestData;
import top.cutexingluo.tools.security.base.function.BaseAccessTokenAdditionalConverter;
import top.cutexingluo.tools.security.base.function.BaseAuthenticationConsumer;
import top.cutexingluo.tools.security.base.function.BasePreAuthenticatedFilter;
import top.cutexingluo.tools.security.oauth.util.XTAuthenticationUtil;
import top.cutexingluo.tools.security.self.base.AuthAccessToken;
import top.cutexingluo.tools.security.self.base.AuthAccessTokenParser;
import top.cutexingluo.tools.security.self.base.AuthTokenExtractType;
import top.cutexingluo.tools.security.self.base.AuthTokenExtractor;
import top.cutexingluo.tools.security.self.base.function.BaseAuthenticationFilter;
import top.cutexingluo.tools.security.self.core.XTAuthTokenExtractor;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 认证转化 执行链工具类 Builder
 *
 * <p>SpringBoot security  任何版本均可使用</p>
 * 身份验证构建器
 * <p>执行链如下</p>
 * <ul>
 *     <li>1. authTokenExtractor 提取器 (非必需set，默认Bear，设置 HttpServletRequest 则执行)</li>
 *     <li>2. preAuthenticatedAuthenticationTokenFilter  (非必需set，默认Bear)</li>
 *     <li>3. tokenConsumer  (非必需set，直接获取 token 字符串)</li>
 *     <li> 3. authAccessTokenParser  (*必需set，解析 token 字符串)</li>
 *     <li>3. accessTokenConsumer  (非必需set，消费 OAuth2AccessToken 对象)</li>
 *     <li>3. accessTokenAdditionalConverter  (非必需set，读取 OAuth2AccessToken 额外信息 返回新认证 （空则不覆盖原来认证）)</li>
 *     <li>3. authenticationFilter  (非必需set，获得Authentication和 OAuth2AccessToken 对象，返回新认证 （空则不覆盖原来认证）)</li>
 *     <li>4. authenticationManager  (非必需set，对Authentication进行认证)</li>
 *     <li>4. authenticationConsumer  (非必需set，最后对Authentication进行操作)</li>
 * </ul>
 *  <p>使用样例</p>
 *  <pre><code>
 *      Authentication authentication = new XTAuthenticationBuilder(request)
 *              .setAuthTokenExtractor(new XTAuthTokenExtractor())
 *              .setAuthAccessTokenParser(new JJwtAuthAccessTokenParser()) // 这里没有提供默认的，必须设置
 *              .setAccessTokenConsumer ( accessToken -> {  // to do  })
 *              .setAccessTokenAdditionalConverter((map,auth) ->  {// to do})
 *              .create("").build();
 *   </code></pre>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/24 15:30
 * @since 1.1.2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class XTAuthenticationBuilder extends XTBuilder<Authentication> implements ExtInitializable<XTAuthenticationBuilder> {

    // const 常量


    public static String defaultHeaderName = "Authorization";

    // constructor 构造

    /**
     * 存入 request
     */
    public XTAuthenticationBuilder(@NotNull HttpServletRequestData request) {
        this.request = request;
    }

    /**
     * 存入 request, 使用 mode 模式
     *
     * @param mode 提取模式
     */
    public XTAuthenticationBuilder(@NotNull HttpServletRequestData request, int mode) {
        this.request = request;
        this.mode = mode;
    }

    /**
     * 存入 token
     */
    public XTAuthenticationBuilder(String token) {
        this.token = token;
    }

    /**
     * 构造初始值 存入 authentication
     * <p>构造后需要调用 initSelf 判断authentication 的值，来决定执行链位置</p>
     */
    public XTAuthenticationBuilder(Authentication authentication) {
        this.target = authentication;
    }

    // chain 流程
    /**
     * 是否已提取
     */
    protected boolean isExtracted = false;

    /**
     * 是否已 预验证 token
     */
    protected boolean isAuthenticated = false;

    /**
     * 是否已完成建立
     */
    protected boolean isBuild = false;

    // properties 属性
    //  --- 开始位置

    // - extractor -
    /**
     * request
     */
    protected HttpServletRequestData request;

    /**
     * 使用的提取模式，默认使用header
     */
    protected int mode = AuthTokenExtractType.USE_HEADERS;

    /**
     * 令牌提取器
     */
    protected AuthTokenExtractor authTokenExtractor; //展开


    /**
     * 提取或存入的token
     */
    protected String token;

    // isExtracted => true

    // isAuthenticated => true

    // - parser -
    /**
     * token 解析器
     */
    protected AuthAccessTokenParser authAccessTokenParser;


    // isBuild => true
    // --- 结束位置

    // execution chain entity and handler 执行链 实体类 和操作类
    //  --- 开始位置
    /**
     * 前验证身份验证令牌
     */
    protected PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken; //预认证

    /**
     * 前验证身份验证标记过滤器
     * <p>设置该项可以在 isAuthenticated=true 之前 进行操作</p>
     * <p>可使用 {@link BasePreAuthenticatedFilter}</p>
     */
    private Function<Authentication, Authentication> preAuthenticatedAuthenticationTokenFilter;

    //-------- 得到 token , isAuthenticated => true --------------

    /**
     * 令牌消费者
     */
    private Consumer<String> tokenConsumer;

    //---------------  解析 token  ---------------
    /**
     * token 解析结果
     */
    protected AuthAccessToken authAccessToken;

    /**
     * 令牌消费者
     */
    protected Consumer<AuthAccessToken> authAccessTokenConsumer; // token消费者

    /**
     * 令牌额外信息转化器<br>
     * 例如可以适配 DefaultUserAuthenticationConverter::extractAuthentication
     * <p>可使用 {@link BaseAccessTokenAdditionalConverter}</p>
     * <p>后面四个处理器最好至少设置一个，否则未处理，最终结果还是原来的 token </p>
     */
    protected Function<Map<String, ?>, Authentication> accessTokenAdditionalConverter;

    /**
     * 令牌认证转化器
     * <p>可使用 {@link BaseAuthenticationFilter}</p>
     */
    private BiFunction<Authentication, AuthAccessToken, Authentication> authenticationFilter; // 令牌认证转化器

    //--------------- 认证中心 ----------------
    /**
     * 身份验证管理器
     */
    private AuthenticationManager authenticationManager; //认证中心
    /**
     * 身份验证消费者
     * <p>可使用 {@link BaseAuthenticationConsumer}</p>
     */
    private Consumer<Authentication> authenticationConsumer; //认证消费者

    // --- 结束位置 --- isBuild => true  ---------------


    // methods 方法
    // -static-

    @Contract("_, _ -> new")
    public static @NotNull
    XTAuthenticationBuilder builder(@NotNull HttpServletRequestData request, int mode) {
        return new XTAuthenticationBuilder(request, mode);
    }

    @Contract("_ -> new")
    public static @NotNull
    XTAuthenticationBuilder builder(@NotNull HttpServletRequestData request) {
        return new XTAuthenticationBuilder(request);
    }

    @Contract("_ -> new")
    public static @NotNull
    XTAuthenticationBuilder builder(@NotNull String token) {
        return new XTAuthenticationBuilder(token);
    }

    // -self-
    protected void setExtracted(boolean extracted) {
        isExtracted = extracted;
    }

    protected void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    protected void setBuild(boolean build) {
        isBuild = build;
    }

    /**
     * 根据参数初始化
     *
     * <p>优先判断位置 </p>
     */
    @Override
    public XTAuthenticationBuilder initSelf() {
        if (this.target != null) {
            if (target instanceof PreAuthenticatedAuthenticationToken) {
                PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken = (PreAuthenticatedAuthenticationToken) target;
                if (preAuthenticatedAuthenticationToken.getPrincipal() instanceof String) {
                    if (this.token == null) this.token = target.getPrincipal().toString();
                    isExtracted = true;
                }
                if (preAuthenticatedAuthenticationToken.isAuthenticated()) {
                    isAuthenticated = true;
                }
//                UsernamePasswordAuthenticationToken 等其他不需要判断
            }
        }
        return this;
    }

    /**
     * 获得令牌
     * <p>需要设置 isAuthenticated = true 或者执行 preAuthenticatedAuthenticationWork()</p>
     * <p>HttpServletRequest 需要执行 preAuthenticatedAuthenticationWork() </p>
     *
     * @return {@link String}
     * @throws AuthenticationServiceException 身份验证服务异常
     * @updateFrom 1.0.4
     */
    public String getTokenNow() {
        if (token != null) { // 自己设置的 token
            return token;
        }
        if (!isAuthenticated) {
            throw new AuthenticationServiceException("no token, please call create(..) or  preAuthenticatedAuthenticationWork()");
        }
        if (isBuild) {
            Object principal = preAuthenticatedAuthenticationToken.getPrincipal();
            if (principal != null) {
                return principal.toString();
            }
            return null;
        } else {
            if (target != null && target.getPrincipal() != null) {
                return target.getPrincipal().toString();
            }
        }
        return null;
    }


    // -no static-
    // ----- 开始位置 -----

    /**
     * 提取 token
     */
    public Authentication extractRequest() {
        Objects.requireNonNull(request, "No HttpServletRequest");
        Objects.requireNonNull(authTokenExtractor, "No AuthTokenExtractor");
        return authTokenExtractor.extract(request);
    }

    /**
     * 自动提取 token
     */
    public Authentication extractRequest(String headerName) {
        Objects.requireNonNull(request, "No HttpServletRequest");
        Objects.requireNonNull(authTokenExtractor, "No AuthTokenExtractor");
        if (headerName == null) { //如果 null, 则默认执行自己设置的tokenExtractor
            return authTokenExtractor.extract(request);
        }
        // 如果为空，默认Authorization，然后后续无需加Bearer
        if (StrUtil.isBlank(headerName)) headerName = defaultHeaderName;
        // 多模式解析
        return XTAuthenticationUtils.extract(request, mode, headerName);
    }

    // ----- 结束位置 -----


    // -work-

    /**
     * 默认初始化
     */
    protected void initTokenExtractor() {
        if (authTokenExtractor == null) {
            authTokenExtractor = new XTAuthTokenExtractor();
        }
    }

    /**
     * 令牌器展开工作
     * <p>TokenExtractor 展开 HttpServletRequest 请求 获取 token 封装 Authentication</p>
     * <p>于 1.0.4 移除非空判断，保证 无token 也能通过执行链</p>
     *
     * @param authorizationName 授权名字
     * @return {@link XTAuthenticationUtil.AuthenticationBuilder}
     */
    public XTAuthenticationBuilder tokenExtractorWork(String authorizationName) {
        initTokenExtractor();
        this.target = extractRequest(authorizationName);
        this.isExtracted = true;
        return this;
    }

    /**
     * tokenWork
     * <p>通过token 直接获取 Authentication</p>
     *
     * @return {@link XTAuthenticationUtil.AuthenticationBuilder}
     */
    public XTAuthenticationBuilder tokenWork() throws AuthenticationServiceException {
        preAuthenticatedAuthenticationToken = new PreAuthenticatedAuthenticationToken(token, "");
        target = preAuthenticatedAuthenticationToken;
        this.isExtracted = true;
        return this;
    }

    /**
     * 前验证身份验证工作
     * <p>preAuthenticatedAuthenticationTokenFilter 进行预处理操作</p>
     *
     * @return {@link XTAuthenticationUtil.AuthenticationBuilder}
     * @throws AuthenticationServiceException 身份验证服务异常
     */
    public XTAuthenticationBuilder preAuthenticatedAuthenticationWork() throws AuthenticationServiceException {
        if (preAuthenticatedAuthenticationTokenFilter == null) { // isAuthenticated
            preAuthenticatedAuthenticationToken = new PreAuthenticatedAuthenticationToken(target.getPrincipal(), target.getCredentials(), target.getAuthorities());
            target = preAuthenticatedAuthenticationToken;
        } else {
            Authentication applyR = preAuthenticatedAuthenticationTokenFilter.apply(target);
            if (applyR instanceof PreAuthenticatedAuthenticationToken) {
                preAuthenticatedAuthenticationToken = (PreAuthenticatedAuthenticationToken) applyR;
            }
            target = applyR;
        }
        isAuthenticated = true;
        return this;
    }

    /**
     * 令牌解析器工作
     * <p>可以从这里读取 token 信息, 生成认证</p>
     * <ul>
     *     <li> tokenConsumer 获取消费 token字符串</li>
     *     <li> authAccessTokenParser 读取 token信息，若没有提供则不会执行以下操作</li>
     *     <li> accessTokenConsumer 读取 OAuth2AccessToken信息，并进行操作</li>
     *     <ul>
     *         <li> accessTokenAdditionalConverter 读取 token 额外信息并返回新认证（若为null则不覆盖认证）</li>
     *          <li> authenticationFilter 过滤 原认证authentication和OAuth2AccessToken信息，返回新认证（若为null则不覆盖认证） </li>
     *     </ul>
     * </ul>
     *
     * @return {@link XTAuthenticationBuilder}
     */
    public XTAuthenticationBuilder tokenParserWork() throws AuthenticationServiceException {
        Object principal = target.getPrincipal();
        if (principal instanceof String) {
            String token = (String) principal;
            if (tokenConsumer != null) tokenConsumer.accept(token);
            if (authAccessTokenParser != null) { // authAccessTokenParser 存在 则使用
                try {
                    authAccessToken = authAccessTokenParser.parse(token);
                } catch (Exception e) {
                    authAccessToken = null;
                }
                if (authAccessToken == null) {
                    throw new AuthenticationServiceException("token parse failed");
                }
                if (authAccessTokenConsumer != null) {
                    authAccessTokenConsumer.accept(authAccessToken);
                }
                if (accessTokenAdditionalConverter != null) {
                    Authentication apply = accessTokenAdditionalConverter.apply(authAccessToken.getAdditionalInformation());
                    if (apply != null) {
                        target = apply;
                    }
                }
                if (authenticationFilter != null) {
                    Authentication apply = authenticationFilter.apply(target, authAccessToken);
                    if (apply != null) {
                        target = apply;
                    }
                }
            }
        }
        return this;
    }

    /**
     * 认证工作
     * <ul>
     *     <li> 执行 authenticationManager 认证 (没有则不执行)</li>
     *     <li> 执行 authenticationConsumer 消费 (没有则不执行)</li>
     * </ul>
     */
    public XTAuthenticationBuilder authenticationWork() {
        if (target != null) {
            if (authenticationManager != null) {
                target = authenticationManager.authenticate(target);
            }
        }
        if (target != null) {
            if (authenticationConsumer != null) {
                authenticationConsumer.accept(target);
            }
        }
        isBuild = true; // over
        return this;
    }
    // ----- create -----

    /**
     * 创建 , 得到 target 为认证后的 Authentication 对象
     * <p> 如果自己设置了 tokenExtractor ，则使用自己的tokenExtractor</p>
     * <p>否则使用默认提取器 {@link XTAuthTokenExtractor}  读取请求头的 "Authorization" 并且需要加 "Bearer" </p>
     * <p>等同于repairCreate(null)</p>
     *
     * @throws AuthenticationServiceException 身份验证服务异常
     */
    public XTAuthenticationBuilder create() throws AuthenticationServiceException {
        return create(null);
    }


    /**
     * 修复创建 , 得到 target 为认证后的 Authentication 对象
     *
     * <ul>
     *     <li>authorizationName 为 null ,  等同于create()</li>
     *     <p>如果自己设置了 tokenExtractor ，则使用自己的tokenExtractor</p>
     *     <p>否则使用默认扩展器 {@link XTAuthTokenExtractor}  读取请求头的 "Authorization" 并且需要加 "Bearer" </p>
     * </ul>
     * <ul>
     *     <li>authorizationName 不为 null , 使用作者提供的 工具扩展器  {@link XTAuthTokenExtractor} </li>
     *     <p>authorizationName 为 ""  使用读取 mode 模式的 "Authorization"  会自动识别 "Bearer" (可加可不加) </p>
     *     <p>authorizationName  不为 ""  使用读取 mode 模式的 authorizationName  会自动识别 "Bearer" (可加可不加) </p>
     * </ul>
     *
     * @param authorizationName 授权名字 (header  名称)
     * @throws AuthenticationServiceException 身份验证服务异常
     */
    public XTAuthenticationBuilder create(String authorizationName) throws AuthenticationServiceException {
        if (!isExtracted) {
            if (token != null) {
                tokenWork();// 直接设置 token
            } else if (request != null) {
                tokenExtractorWork(authorizationName); // 获取 token
            }
        }

        if (target != null) { // 解析到 token
            if (!isAuthenticated) {
                preAuthenticatedAuthenticationWork(); // 预验证 token
            }
            if (!isBuild) {
                tokenParserWork(); // 解析附加信息
                authenticationWork();  // 消费认证
            }
        }
        return this;
    }


}
