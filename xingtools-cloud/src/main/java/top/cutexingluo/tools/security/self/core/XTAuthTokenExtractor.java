package top.cutexingluo.tools.security.self.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.cutexingluo.tools.bridge.servlet.HttpServletRequestData;
import top.cutexingluo.tools.security.self.base.AbstractAuthTokenExtractor;

/**
 * 常规令牌提取器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/25 10:36
 * @since 1.1.2
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class XTAuthTokenExtractor extends AbstractAuthTokenExtractor {

    protected String headerName = "Authorization";

    protected String prefix = "Bearer ";

    /**
     * 是否解析Cookies
     * <p>false => headers , true => cookies</p>
     */
    protected boolean useCookies = false;

    public XTAuthTokenExtractor() {
        this.useCookies = false;
    }

    public XTAuthTokenExtractor(boolean useCookies) {
        this.useCookies = useCookies;
    }

    @Override
    public String extractToken(HttpServletRequestData request) {
        if (useCookies) {
            return super.extractCookieToken(request, headerName, prefix);
        } else {
            return super.extractHeaderToken(request, headerName, prefix);
        }
    }
}
