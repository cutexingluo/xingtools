package top.cutexingluo.tools.bridge.servlet.adapter;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * ServletRequest 适配器
 *
 * <p>详见 ServletRequest</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/4 11:59
 * @since 1.1.6
 */
public interface ServletRequestAdapter {

    Object getAttribute(String name);

    Enumeration<String> getAttributeNames();

    String getCharacterEncoding();

    void setCharacterEncoding(String env)
            throws java.io.UnsupportedEncodingException;

    int getContentLength();

    long getContentLengthLong();

    String getContentType();


    String getParameter(String name);

    Enumeration<String> getParameterNames();

    String[] getParameterValues(String name);

    Map<String, String[]> getParameterMap();

    String getProtocol();

    String getScheme();

    String getServerName();

    int getServerPort();

    BufferedReader getReader() throws IOException;

    String getRemoteAddr();

    String getRemoteHost();

    void setAttribute(String name, Object o);

    void removeAttribute(String name);

    Locale getLocale();

    Enumeration<Locale> getLocales();

    boolean isSecure();

    int getRemotePort();

    String getLocalName();

    String getLocalAddr();

    int getLocalPort();

    boolean isAsyncStarted();

    boolean isAsyncSupported();

}
