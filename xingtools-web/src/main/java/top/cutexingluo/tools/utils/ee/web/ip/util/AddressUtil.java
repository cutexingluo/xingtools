package top.cutexingluo.tools.utils.ee.web.ip.util;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.util.StringUtils;

/**
 * ip地址工具
 */
public class AddressUtil {


    /**
     * 根据IP地址查询登录来源
     */
    public static String getCityInfo(String ip) throws Exception {
        Searcher searcher = SearchHolder.getSearcher();
        //开始查询
        return searcher.search(ip);
    }

    /**
     * 根据ip从 ip2region.db 中获取地理位置
     *
     */
    public static String getIpSource(String ip) throws Exception {
        Searcher searcher = SearchHolder.getSearcher();
        String address = searcher.search(ip);
        if (StringUtils.hasText(address)) {
            address = address.replace("|0", "");
            address = address.replace("0|", "");
            return address;
        }
        return address;
    }


    public static void main(String[] args) throws Exception {
        //204.16.111.255
        System.out.println(getCityInfo("204.16.111.255"));
    }

}