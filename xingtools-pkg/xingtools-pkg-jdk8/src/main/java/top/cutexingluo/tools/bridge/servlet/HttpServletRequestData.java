package top.cutexingluo.tools.bridge.servlet;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.common.base.IData;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest 封装类
 * <p>目的：兼容多版本</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 14:27
 * @since 1.1.1
 */
@Data
@AllArgsConstructor
public class HttpServletRequestData implements IData<HttpServletRequest> {

    /**
     * HttpServletRequest
     */
    protected HttpServletRequest request;


    @Contract("_ -> new")
    public static @NotNull HttpServletRequestData of(HttpServletRequest request) {
        return new HttpServletRequestData(request);
    }

    @Override
    public HttpServletRequest data() {
        return request;
    }
}
