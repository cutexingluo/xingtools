package top.cutexingluo.tools.utils.ee.web.ip.util;


import org.springframework.util.StringUtils;
import top.cutexingluo.core.bridge.servlet.adapter.HttpServletRequestAdapter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * IP地址工具类
 *
 * <p>需要导入 org.lionsoul:ip2region 包</p>
 *
 * @author ican
 */
@SuppressWarnings("all")
public class IpUtils {


    /**
     * 在Nginx等代理之后获取用户真实IP地址
     */
    public static String getIpAddress(HttpServletRequestAdapter request) {
        String ip;
        try {
            ip = request.getHeader("X-Real-IP");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("x-forwarded-for");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                    //根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        throw new UnknownHostException("无法确定主机的IP地址");
                    }
                    ip = inet.getHostAddress();
                }
            }
            // 使用代理，则获取第一个IP地址
            if (!StringUtils.hasText(ip) && Objects.requireNonNull(ip).length() > 15) {
                int idx = ip.indexOf(",");
                if (idx > 0) {
                    ip = ip.substring(0, idx);
                }
            }
        } catch (Exception e) {
            ip = "";
        }
        return ip;
    }


}
