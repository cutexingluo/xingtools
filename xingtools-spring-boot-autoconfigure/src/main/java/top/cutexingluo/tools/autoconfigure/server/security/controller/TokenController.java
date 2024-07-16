package top.cutexingluo.tools.autoconfigure.server.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.cutexingluo.tools.common.base.IResult;
import top.cutexingluo.tools.common.utils.GlobalResultFactory;
import top.cutexingluo.tools.security.oauth.controller.XTTokenController;
import top.cutexingluo.tools.start.log.LogInfoAuto;

import java.security.Principal;
import java.util.Map;

/**
 * 用 XT 的 Result 封装 /oauth/token 接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/15 16:55
 */
@ConditionalOnProperty(value = "xingtools.security.enabled.override-oauth-token", havingValue = "true", matchIfMissing = false)
@ConditionalOnBean({TokenEndpoint.class, SecurityControllerConfig.class})
@ConditionalOnClass({Principal.class, OAuth2AccessToken.class})
@Slf4j
@RestController
public class TokenController extends XTTokenController implements InitializingBean {

    //令牌请求的端点
    protected TokenEndpoint tokenEndpoint;

    @Autowired
    public TokenController(GlobalResultFactory globalResultFactory, TokenEndpoint tokenEndpoint) {
        super(globalResultFactory);
        this.tokenEndpoint = tokenEndpoint;
    }

    /**
     * 重写/oauth/token 这个默认接口，返回的数据格式统一
     */
    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
    public <C, T> IResult<C, T> postAccessToken(Principal principal, @RequestParam
            Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return super.postAccessToken(tokenEndpoint, principal, parameters);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (LogInfoAuto.enabled) log.info("XT-TokenController 已覆盖 token 接口");
    }
}
