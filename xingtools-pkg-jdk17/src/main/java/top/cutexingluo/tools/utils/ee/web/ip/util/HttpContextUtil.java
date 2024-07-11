package top.cutexingluo.tools.utils.ee.web.ip.util;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * 全局获取HttpServletRequest、HttpServletResponse
 *
 * @since 1.0.4
 */

public class HttpContextUtil {

    private HttpContextUtil() {

    }

    public static RequestAttributes getRequestAttributes() {
        return RequestContextHolder.getRequestAttributes();
    }

    @Nullable
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }


    /**
     * fix v1.0.5
     *
     * @return HttpServletRequest
     */
    @Nullable
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = getServletRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return null;
        }
        return requestAttributes.getRequest();
    }

    /**
     * fix v1.0.5
     *
     * @return HttpServletResponse
     */
    @Nullable
    public static HttpServletResponse getHttpServletResponse() {
        ServletRequestAttributes requestAttributes = getServletRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return null;
        } else {
            return requestAttributes.getResponse();
        }
    }


}
