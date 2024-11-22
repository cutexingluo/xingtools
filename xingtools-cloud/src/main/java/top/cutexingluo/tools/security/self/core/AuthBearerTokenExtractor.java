package top.cutexingluo.tools.security.self.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import top.cutexingluo.tools.bridge.servlet.HttpServletRequestData;
import top.cutexingluo.tools.bridge.servlet.adapter.HttpServletRequestDataAdapter;
import top.cutexingluo.tools.security.self.base.AbstractAuthTokenExtractor;

/**
 * BearerTokenExtractor
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/24 17:23
 * @since 1.1.2
 */
public class AuthBearerTokenExtractor extends AbstractAuthTokenExtractor {

    private final static Log logger = LogFactory.getLog(AuthBearerTokenExtractor.class);

    /**
     * bearer type
     */
    public static final String BEARER = "Bearer";


    @Override
    public String extractToken(HttpServletRequestData request) {
        // first check the header...
        String token = extractHeaderToken(request);

        // bearer type allows a request parameter as well
        if (token == null) {
            logger.debug("Token not found in headers. Trying request parameters.");
//            token = request.getParameter(org.springframework.security.oauth2.common.OAuth2AccessToken.ACCESS_TOKEN);
//            if (token == null) {
//                logger.debug("Token not found in request parameters.  Not an OAuth2 request.");
//            } else {
//                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, org.springframework.security.oauth2.common.OAuth2AccessToken.BEARER_TYPE);
//            }
        }
        return token;
    }


    /**
     * Extract the OAuth bearer token from a header.
     *
     * @param request The request.
     * @return The token, or null if no OAuth authorization header was supplied.
     */
    protected String extractHeaderToken(HttpServletRequestData request) {
        return extractHeaderToken(HttpServletRequestDataAdapter.of(request), "Authorization", BEARER);
    }
}
