package top.cutexingluo.tools.security.self.util;

import org.springframework.security.core.Authentication;
import top.cutexingluo.tools.bridge.servlet.HttpServletRequestData;
import top.cutexingluo.tools.security.self.base.AuthTokenExtractType;
import top.cutexingluo.tools.security.self.core.XTAuthBearTokenExtractor;


/**
 * 认证转化工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/25 17:10
 * @since 1.1.2
 */
public class XTAuthenticationUtils {

    /**
     * 打印验证
     *
     * @param title          标题
     * @param authentication 身份验证
     */
    protected static void printAuthentication(String title, Authentication authentication) {
        System.out.println("============== " + title + " ===============");
        System.out.println(authentication);
        System.out.println("-------------------------------------");
        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getCredentials());
        System.out.println(authentication.getAuthorities());
        System.out.println("======================================");
    }


    /**
     * 使用XTBearerTokenExtractor提取token，可以不加Bearer
     * <p>并且支持多模式解析</p>
     *
     * @param requestData http servlet请求
     * @param mode        模式          可以支持headers和cookies两种 , 优先读取 headers . 详见 {@link AuthTokenExtractType}
     * @param headerName  标题名称
     */
    public static Authentication extract(HttpServletRequestData requestData, int mode, String headerName) {
        XTAuthBearTokenExtractor extractor = new XTAuthBearTokenExtractor();
        Authentication preAuthentication = null;
        if ((mode & AuthTokenExtractType.USE_HEADERS) != 0) {
            preAuthentication = extractor.extract(requestData, headerName);
        }
        if (preAuthentication == null && (mode & AuthTokenExtractType.USE_COOKIES) != 0) {
            extractor.setUseCookies(true);
            preAuthentication = extractor.extract(requestData, headerName);
        }
        return preAuthentication;
    }

}
