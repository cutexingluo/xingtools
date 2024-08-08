package top.cutexingluo.tools.security.self.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import top.cutexingluo.tools.bridge.servlet.HttpServletRequestData;

/**
 * bearer 令牌提取器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/24 17:44
 * @since 1.1.2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class XTAuthBearTokenExtractor extends AuthBearerTokenExtractor {

    protected String headerName = "Authorization";

    /**
     * 是否解析Cookies
     * <p>false => headers , true => cookies</p>
     */
    protected boolean useCookies = false;

    public XTAuthBearTokenExtractor() {
        this.useCookies = false;
    }

    public XTAuthBearTokenExtractor(boolean useCookies) {
        this.useCookies = useCookies;
    }

    /**
     * 提取为Authentication
     *
     * @param request    请求
     * @param headerName 请求头名称
     * @return {@link Authentication}
     */
    public Authentication extract(HttpServletRequestData request, String headerName) {
        this.headerName = headerName;
        return super.extract(request);
    }

    @Override
    protected String extractHeaderToken(HttpServletRequestData request) {
        if (useCookies) {
            return super.extractCookieToken(request, headerName, BEARER);
        } else {
            return super.extractHeaderToken(request, headerName, BEARER);
        }
    }


    /**
     * 重写，始终获取 token
     *
     * @param value  The header value of the header.
     * @param prefix The prefix that indicates a bearer token (typically "Bearer  ").
     * @return The token.
     */
    @NotNull
    @Override
    protected String parseToken(String value, String prefix) {
        return super.cutToken(value, prefix);
    }
}
