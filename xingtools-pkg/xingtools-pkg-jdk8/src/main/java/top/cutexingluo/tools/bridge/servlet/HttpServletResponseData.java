package top.cutexingluo.tools.bridge.servlet;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.common.base.IData;

import javax.servlet.http.HttpServletResponse;

/**
 * HttpServletResponse 封装类
 * <p>目的：兼容多版本</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 14:27
 * @since 1.1.1
 */
@Data
@AllArgsConstructor
public class HttpServletResponseData implements IData<HttpServletResponse> {

    /**
     * HttpServletResponse
     */
    protected HttpServletResponse response;

    @Contract("_ -> new")
    public static @NotNull HttpServletResponseData of(HttpServletResponse response) {
        return new HttpServletResponseData(response);
    }

    @Override
    public HttpServletResponse data() {
        return response;
    }
}
