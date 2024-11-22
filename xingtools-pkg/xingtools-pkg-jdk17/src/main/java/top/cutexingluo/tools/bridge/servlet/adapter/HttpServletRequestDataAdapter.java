package top.cutexingluo.tools.bridge.servlet.adapter;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.bridge.servlet.HttpServletRequestData;
import top.cutexingluo.tools.common.base.IData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * HttpServletRequestData 适配器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/12 11:40
 * @since 1.1.3
 */
@Data
@AllArgsConstructor
public class HttpServletRequestDataAdapter implements HttpServletRequestAdapter, IData<HttpServletRequestData> {

    protected HttpServletRequestData data;

    @Contract("_ -> new")
    public static @NotNull HttpServletRequestDataAdapter of(HttpServletRequestData data) {
        return new HttpServletRequestDataAdapter(data);
    }


    @Override
    public String getAuthType() {
        return data.getRequest().getAuthType();
    }

    @Override
    public long getDateHeader(String name) {
        return data.getRequest().getDateHeader(name);
    }

    @Override
    public String getHeader(String name) {
        return data.getRequest().getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return data.getRequest().getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return data.getRequest().getHeaderNames();
    }

    @Override
    public int getIntHeader(String name) {
        return data.getRequest().getIntHeader(name);
    }

    @Override
    public String getMethod() {
        return data.getRequest().getMethod();
    }

    @Override
    public String getRequestURI() {
        return data.getRequest().getRequestURI();
    }

    @Override
    public StringBuffer getRequestURL() {
        return data.getRequest().getRequestURL();
    }

    @Override
    public String getServletPath() {
        return data.getRequest().getServletPath();
    }

    @Override
    public String changeSessionId() {
        return data.getRequest().changeSessionId();
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return data.getRequest().isRequestedSessionIdValid();
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return data.getRequest().isRequestedSessionIdFromCookie();
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return data.getRequest().isRequestedSessionIdFromURL();
    }


    @Override
    public String getPathInfo() {
        return data.getRequest().getPathInfo();
    }

    @Override
    public String getPathTranslated() {
        return data.getRequest().getPathTranslated();
    }

    @Override
    public String getContextPath() {
        return data.getRequest().getContextPath();
    }

    @Override
    public String getQueryString() {
        return data.getRequest().getQueryString();
    }

    @Override
    public String getRemoteUser() {
        return data.getRequest().getRemoteUser();
    }

    @Override
    public boolean isUserInRole(String role) {
        return data.getRequest().isUserInRole(role);
    }

    @Override
    public Principal getUserPrincipal() {
        return data.getRequest().getUserPrincipal();
    }

    @Override
    public String getRequestedSessionId() {
        return data.getRequest().getRequestedSessionId();
    }

    @Override
    public Object getAttribute(String name) {
        return data.getRequest().getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return data.getRequest().getAttributeNames();
    }

    @Override
    public String getCharacterEncoding() {
        return data.getRequest().getCharacterEncoding();
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        data.getRequest().setCharacterEncoding(env);
    }

    @Override
    public int getContentLength() {
        return data.getRequest().getContentLength();
    }

    @Override
    public long getContentLengthLong() {
        return data.getRequest().getContentLengthLong();
    }

    @Override
    public String getContentType() {
        return data.getRequest().getContentType();
    }

    @Override
    public String getParameter(String name) {
        return data.getRequest().getParameter(name);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return data.getRequest().getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        return data.getRequest().getParameterValues(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return data.getRequest().getParameterMap();
    }

    @Override
    public String getProtocol() {
        return data.getRequest().getProtocol();
    }

    @Override
    public String getScheme() {
        return data.getRequest().getScheme();
    }

    @Override
    public String getServerName() {
        return data.getRequest().getServerName();
    }

    @Override
    public int getServerPort() {
        return data.getRequest().getServerPort();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return data.getRequest().getReader();
    }

    @Override
    public String getRemoteAddr() {
        return data.getRequest().getRemoteAddr();
    }

    @Override
    public String getRemoteHost() {
        return data.getRequest().getRemoteHost();
    }

    @Override
    public void setAttribute(String name, Object o) {
        data.getRequest().setAttribute(name, o);
    }

    @Override
    public void removeAttribute(String name) {
        data.getRequest().removeAttribute(name);
    }

    @Override
    public Locale getLocale() {
        return data.getRequest().getLocale();
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return data.getRequest().getLocales();
    }

    @Override
    public boolean isSecure() {
        return data.getRequest().isSecure();
    }

    @Override
    public int getRemotePort() {
        return data.getRequest().getRemotePort();
    }

    @Override
    public String getLocalName() {
        return data.getRequest().getLocalName();
    }

    @Override
    public String getLocalAddr() {
        return data.getRequest().getLocalAddr();
    }

    @Override
    public int getLocalPort() {
        return data.getRequest().getLocalPort();
    }

    @Override
    public boolean isAsyncStarted() {
        return data.getRequest().isAsyncStarted();
    }

    @Override
    public boolean isAsyncSupported() {
        return data.getRequest().isAsyncSupported();
    }

    @Override
    public Map<String, String> getTrailerFields() {
        return data.getRequest().getTrailerFields();
    }

    @Override
    public boolean isTrailerFieldsReady() {
        return data.getRequest().isTrailerFieldsReady();
    }

    @Override
    public String getCookieValue(String name) {
        Cookie[] cookies = data.getRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName() != null && cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public void cookieFilter(Predicate<String> namePredicate, BiConsumer<String, String> cookieConsumer) {
        Cookie[] cookies = data.getRequest().getCookies();
        if (cookies != null && namePredicate != null && cookieConsumer != null) {
            for (Cookie cookie : cookies) {
                if (namePredicate.test(cookie.getName())) {
                    cookieConsumer.accept(cookie.getName(), cookie.getValue());
                }
            }
        }
    }

    @Override
    public HttpServletRequestData data() {
        return data;
    }
}
