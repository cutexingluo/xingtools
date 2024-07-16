package top.cutexingluo.tools.security.oauth.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import top.cutexingluo.tools.common.Result;
import top.cutexingluo.tools.common.base.IResult;
import top.cutexingluo.tools.common.utils.GlobalResultFactory;

import java.security.Principal;
import java.util.Map;
import java.util.function.Function;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 11:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XTTokenController {

    protected GlobalResultFactory globalResultFactory;

    public <C, T> IResult<C, T> postAccessToken(TokenEndpoint tokenEndpoint,Principal principal,
                                                Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return (IResult<C, T>) postAccessToken(tokenEndpoint, principal, parameters, accessToken -> {
            Result error = Result.success(accessToken);
            return GlobalResultFactory.selectResult(globalResultFactory,error);
        });
    }

    public <C, T> IResult<C, T> postAccessToken(TokenEndpoint tokenEndpoint, Principal principal,
                                                Map<String, String> parameters,@NotNull Function<OAuth2AccessToken,IResult<C, T>>  filter) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        return filter.apply(accessToken);
    }
}
