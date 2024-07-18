package top.cutexingluo.tools.utils.ee.servlet.util;

import top.cutexingluo.tools.utils.ee.web.front.WebUtils;
import top.cutexingluo.tools.utils.ee.web.ip.util.HttpContextUtil;

import jakarta.servlet.http.HttpServletResponse;

/**
 * HttpServlet 工具类
 * <p>尽量将 javax 包或 jakarta 包限制在当前类中</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 14:13
 * @since 1.1.1
 */
public class HttpServletUtil extends HttpContextUtil {


    /**
     * 发送字符串
     */
    public static HttpServletResponse rendString(String msg){
        HttpServletResponse response = getHttpServletResponse();
        if(response != null){
            WebUtils.renderString(response, msg);
        }
        return response;
    }
}
