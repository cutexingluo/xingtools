package top.cutexingluo.tools.security.oauth.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * OAuth2AccessToken 封装类 <br>
 * 可自行继承扩展
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/18 14:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractOAuth2AccessToken implements OAuth2AccessToken {

    protected OAuth2AccessToken accessToken;

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return accessToken.getAdditionalInformation();
    }

    @Override
    public Set<String> getScope() {
        return accessToken.getScope();
    }

    @Override
    public OAuth2RefreshToken getRefreshToken() {
        return accessToken.getRefreshToken();
    }

    @Override
    public String getTokenType() {
        return accessToken.getTokenType();
    }

    @Override
    public boolean isExpired() {
        return accessToken.isExpired();
    }

    @Override
    public Date getExpiration() {
        return accessToken.getExpiration();
    }

    @Override
    public int getExpiresIn() {
        return accessToken.getExpiresIn();
    }

    @Override
    public String getValue() {
        return accessToken.getValue();
    }
}
