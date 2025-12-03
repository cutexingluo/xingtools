package top.cutexingluo.tools.utils.ee.web.front;

import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.common.base.IResult;
import top.cutexingluo.core.designtools.protocol.serializer.impl.json.JacksonSerializer;
import top.cutexingluo.tools.bridge.servlet.HttpServletResponseData;
import top.cutexingluo.tools.utils.ee.web.data.WebResponse;

import java.io.IOException;

/**
 * web 返回工具类
 *
 * <p>推荐使用 {@link XTResponseUtil}</p>
 */
public class WebUtils {


    /**
     * 返回请求封装
     *
     * @param rsp           返回请求
     * @param result        返回数据
     * @param rspStatusCode 返回码
     */
    public static <C, T> void response(HttpServletResponse rsp, IResult<C, T> result, int rspStatusCode) throws IOException {
        JacksonSerializer serializer = new JacksonSerializer();
        String stringify = serializer.stringify(result);
        response(rsp, stringify, rspStatusCode);
    }

    /**
     * 返回请求
     *
     * @param rsp           返回请求
     * @param result        返回消息
     * @param rspStatusCode 返回码
     * @since 1.1.3
     */
    public static <C, T> void response(@NotNull HttpServletResponseData rsp, String result, int rspStatusCode) throws IOException {
        response(rsp.getResponse(), result, rspStatusCode);
    }


    /**
     * 返回请求
     *
     * @param response      返回请求
     * @param result        返回消息
     * @param rspStatusCode 返回码
     */
    public static <C, T> void response(HttpServletResponse response, String result, int rspStatusCode) throws IOException {
        WebResponse webResponse = new WebResponse(response);
        webResponse.utf8().contentTypeJson().status(rspStatusCode);
        response.getWriter().print(result);
    }

    /**
     * 返回请求
     *
     * @param response 返回请求
     * @param result   返回消息
     */
    public static void renderString(HttpServletResponse response, String result) throws IOException {
        response(response, result, HttpServletResponse.SC_OK);
    }


}