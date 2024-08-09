package top.cutexingluo.tools.bridge.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.data.Tuple;

/**
 * HttpServletRequest 和 HttpServletResponse 封装类
 * <p>目的：兼容多版本</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 14:31
 * @since 1.1.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpServletBundle implements Tuple<HttpServletRequest, HttpServletResponse> {

    /**
     * HttpServletRequest
     */
    protected HttpServletRequest request;

    /**
     * HttpServletResponse
     */
    protected HttpServletResponse response;


    /**
     * 构造方法
     */
    public static @NotNull HttpServletBundle of(HttpServletRequest request, HttpServletResponse response) {
        return new HttpServletBundle(request, response);
    }

    @Override
    public HttpServletRequest getKey() {
        return request;
    }

    @Override
    public HttpServletResponse getValue() {
        return response;
    }

    /**
     * 实现Tuple 接口 后添加此方法
     *
     * @since 1.1.2
     */
    @Override
    public HttpServletRequest setKey(HttpServletRequest key) {
        HttpServletRequest old = request;
        this.request = key;
        return old;
    }

    @Override
    public HttpServletResponse setValue(HttpServletResponse value) {
        HttpServletResponse old = response;
        this.response = value;
        return old;
    }
}
