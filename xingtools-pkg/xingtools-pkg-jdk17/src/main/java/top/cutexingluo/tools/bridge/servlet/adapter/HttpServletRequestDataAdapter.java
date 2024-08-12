package top.cutexingluo.tools.bridge.servlet.adapter;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.bridge.servlet.HttpServletRequestData;
import top.cutexingluo.tools.common.base.IData;

import java.util.Enumeration;
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
    public String getRequestURI() {
        return data.getRequest().getRequestURI();
    }

    @Override
    public String getMethod() {
        return data.getRequest().getMethod();
    }

    @Override
    public String getRemoteAddr() {
        return data.getRequest().getRemoteAddr();
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
