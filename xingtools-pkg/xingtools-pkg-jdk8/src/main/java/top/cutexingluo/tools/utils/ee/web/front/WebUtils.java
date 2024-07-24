package top.cutexingluo.tools.utils.ee.web.front;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.cutexingluo.tools.common.base.IResult;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
        byte[] responseBytes = new ObjectMapper().writeValueAsBytes(result);
        response(rsp, new String(responseBytes), rspStatusCode);
    }


    /**
     * 返回请求
     *
     * @param rsp           返回请求
     * @param result        返回消息
     * @param rspStatusCode 返回码
     */
    public static <C, T> void response(HttpServletResponse rsp, String result, int rspStatusCode) throws IOException {
        rsp.addHeader("Content-Type", "application/json;charset=UTF-8");
        rsp.setStatus(rspStatusCode);
        rsp.getWriter().write(result);
    }

    /**
     * 将字符串渲染到客户端
     * <p>未来将被移除</p>
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     */
    @Deprecated
    public static void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置下载文件头
     */
    public static void setDownLoadHeader(String filename, ServletContext context, HttpServletResponse response) throws UnsupportedEncodingException {
        String mimeType = context.getMimeType(filename);//获取文件的mime类型
        response.setHeader("content-type", mimeType);
        String fname = URLEncoder.encode(filename, "UTF-8");
        response.setHeader("Content-disposition", "attachment; filename=" + fname);

//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setCharacterEncoding("utf-8");
    }
}