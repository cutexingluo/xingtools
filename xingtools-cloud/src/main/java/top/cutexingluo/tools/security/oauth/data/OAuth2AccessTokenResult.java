package top.cutexingluo.tools.security.oauth.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import top.cutexingluo.tools.common.MSResult;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 对 OAuth2AccessToken 2次封装<br>
 * 可以重写 TokenEndpoint 的 postAccessToken 方法 <br>
 * <ul>
 *     <li>
 *         1. 返回该 ResponseEntity < OAuth2AccessTokenResult > 对象
 *     </li>
 *     <li>
 *         2. 里面的 OAuth2AccessTokenResult  对象可以设置 data 数据 T extends {@link AbstractOAuth2AccessToken}
 *     </li>
 * </ul>
 *
 * <br>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/18 14:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OAuth2AccessTokenResult extends MSResult<OAuth2AccessToken> implements OAuth2AccessToken {
    @Override
    public Map<String, Object> getAdditionalInformation() {
        return data.getAdditionalInformation();
    }

    @Override
    public Set<String> getScope() {
        return data.getScope();
    }

    @Override
    public OAuth2RefreshToken getRefreshToken() {
        return data.getRefreshToken();
    }

    @Override
    public String getTokenType() {
        return data.getTokenType();
    }

    @Override
    public boolean isExpired() {
        return data.isExpired();
    }

    @Override
    public Date getExpiration() {
        return data.getExpiration();
    }

    @Override
    public int getExpiresIn() {
        return data.getExpiresIn();
    }

    @Override
    public String getValue() {
        return data.getValue();
    }
}
