package top.cutexingluo.tools.utils.ee.servlet.bridge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * HttpServletRequest 和 HttpServletResponse 封装类
 * <p>目的：兼容多版本</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 14:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpServletBundle implements Map.Entry<HttpServletRequest,HttpServletResponse>{

    /**
     * HttpServletRequest
     */
    protected HttpServletRequest request;

    /**
     * HttpServletResponse
     */
    protected HttpServletResponse response;


    public static @NotNull HttpServletBundle of( HttpServletRequest request,HttpServletResponse response) {
        return new HttpServletBundle(request,response);
    }

    @Override
    public HttpServletRequest getKey() {
        return request;
    }

    @Override
    public HttpServletResponse getValue() {
        return response;
    }

    @Override
    public HttpServletResponse setValue(HttpServletResponse value) {
        HttpServletResponse old=response;
        this.response=value;
        return old;
    }
}
