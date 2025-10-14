package top.cutexingluo.tools.bridge.servlet.adapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.bridge.servlet.adapter.HttpServletResponseAdapter;
import top.cutexingluo.core.common.base.IData;
import top.cutexingluo.core.common.base.IResult;
import top.cutexingluo.tools.bridge.servlet.HttpServletResponseData;
import top.cutexingluo.tools.utils.ee.web.front.XTResponseUtil;

import java.io.IOException;

/**
 * HttpServletResponseData 适配器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/12 11:40
 */
@Data
@AllArgsConstructor
public class HttpServletResponseDataAdapter implements HttpServletResponseAdapter, IData<HttpServletResponseData> {

    protected HttpServletResponseData data;

    @Contract("_ -> new")
    public static @NotNull
    HttpServletResponseDataAdapter of(HttpServletResponseData data) {
        return new HttpServletResponseDataAdapter(data);
    }

    @Override
    public HttpServletResponseData data() {
        return data;
    }

    @Override
    public void response(String content, int rspStatusCode) throws IOException {
        XTResponseUtil.response(data.getResponse(), content, rspStatusCode);
    }

    @Override
    public <C, T> void response(IResult<C, T> result, int rspStatusCode) throws IOException {
        XTResponseUtil.response(data.getResponse(), result, rspStatusCode);
    }
}
