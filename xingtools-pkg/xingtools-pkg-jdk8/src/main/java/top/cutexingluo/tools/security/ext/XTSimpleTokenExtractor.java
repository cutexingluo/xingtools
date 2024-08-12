package top.cutexingluo.tools.security.ext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import top.cutexingluo.tools.security.base.BearerTokenExtractor;

import javax.servlet.http.HttpServletRequest;

/**
 * 简易 BearerTokenExtractor
 *
 * <p>未来将被移除，请使用 cloud 包里面的类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/30 11:35
 */
@Deprecated
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XTSimpleTokenExtractor extends BearerTokenExtractor {
    private String headerName = "Authorization";

    /**
     * 提取为Authentication
     *
     * @param request    请求
     * @param headerName 请求头名称
     * @return {@link Authentication}
     */
    public Authentication extract(HttpServletRequest request, String headerName) {
        this.headerName = headerName;
        return super.extract(request);
    }

    @Override
    protected String extractHeaderToken(HttpServletRequest request) {
        String token = request.getHeader(headerName);
        if (token != null) {
            if (token.length() > 7 && token.startsWith(OAuth2AccessToken.BEARER_TYPE)) {
                token = token.substring(7);
            }
            return token;
        }
        return null;
    }

}
