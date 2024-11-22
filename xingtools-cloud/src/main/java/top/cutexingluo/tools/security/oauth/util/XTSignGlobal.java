package top.cutexingluo.tools.security.oauth.util;

/**
 * sign 全局配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 13:43
 */
public class XTSignGlobal {


    public static String signKey = "sign_key";

    /**
     * 得到签
     *
     * @return {@link String}
     */
    public static String getSign() {
        return signKey;
    }

    /**
     * 设置签
     */
    public static void setSign(String sign) {
        signKey = sign;
    }
}
