package top.cutexingluo.tools.security.self.base;

import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import top.cutexingluo.core.basepackage.function.Parser;
import top.cutexingluo.core.bridge.servlet.adapter.HttpServletRequestAdapter;
import top.cutexingluo.tools.bridge.servlet.HttpServletRequestData;

import java.util.Enumeration;

/**
 * abstract token  extractor
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/25 10:38
 * @since 1.1.2
 */
public abstract class AbstractAuthTokenExtractor implements AuthTokenExtractor, Parser<String, String> {


    @Override
    public Authentication extract(HttpServletRequestData request) {
        String tokenValue = extractToken(request);
        if (tokenValue != null) {
            PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(tokenValue, "");
            return authentication;
        }
        return null;
    }

    /**
     * extract token from request
     *
     * @param request HttpServletRequestData
     * @return token
     */
    public abstract String extractToken(HttpServletRequestData request);


    /**
     * Extract the OAuth token from a header.
     *
     * @param request    The request.
     * @param headerName The header name.
     * @param prefix     The prefix that indicates a bearer token (typically "Bearer ").
     * @return The token, or null if no OAuth authorization header was supplied.
     */
    protected String extractHeaderToken(HttpServletRequestAdapter request, String headerName, @Nullable String prefix) {
        Enumeration<String> headers = request.getHeaders(headerName);
        while (headers.hasMoreElements()) { // typically there is only one (most servers enforce that)
            String value = headers.nextElement();
            return parseToken(value, prefix);
        }
        return null;
    }

    /**
     * Extract the OAuth token from a Cookie.
     *
     * @param request    The request.
     * @param headerName The header name.
     * @param prefix     The prefix that indicates a bearer token (typically "Bearer ").
     * @return The token, or null if no OAuth authorization header was supplied.
     */
    protected String extractCookieToken(HttpServletRequestAdapter request, String headerName, @Nullable String prefix) {
        String cookieValue = request.getCookieValue(headerName);
        if (cookieValue != null) {
            return parseToken(cookieValue, prefix);
        }
        return null;
    }


    /**
     * parse the token from header value
     *
     * @param value  The header value of the header.
     * @param prefix The prefix that indicates a bearer token (typically "Bearer  ").
     * @return Qualified token .
     */
    protected String parseToken(String value, @Nullable String prefix) {
        String authHeaderValue;
        if (prefix == null || prefix.length() == 0) {
            authHeaderValue = value.trim();
            int commaIndex = authHeaderValue.indexOf(',');
            if (commaIndex > 0) {
                authHeaderValue = authHeaderValue.substring(0, commaIndex);
            }
            return authHeaderValue;
        } else if (value.toLowerCase().startsWith(prefix.toLowerCase())) {
            authHeaderValue = value.substring(prefix.length()).trim();
            int commaIndex = authHeaderValue.indexOf(',');
            if (commaIndex > 0) {
                authHeaderValue = authHeaderValue.substring(0, commaIndex);
            }
            return authHeaderValue;
        }
        return null;
    }

    /**
     * cut the token from header value
     *
     * @param value  The header value of the header.
     * @param prefix The prefix that indicates a bearer token (typically "Bearer  ").
     * @return Reduced token .
     */
    protected String cutToken(String value, @Nullable String prefix) {
        String authHeaderValue;
        if (prefix != null && (value.toLowerCase().startsWith(prefix))) {
            authHeaderValue = value.substring(prefix.length()).trim();
        } else {
            authHeaderValue = value.trim();
        }
        int commaIndex = authHeaderValue.indexOf(',');
        if (commaIndex > 0) {
            authHeaderValue = authHeaderValue.substring(0, commaIndex);
        }
        return authHeaderValue;
    }

    /**
     * cut the token from header value
     *
     * @param value The header value of the header.
     * @return Reduced token .
     */
    @Override
    public String parse(String value) {
        return cutToken(value, null);
    }
}
