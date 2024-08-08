package top.cutexingluo.tools.utils.ee.web.ip.util;

import org.lionsoul.ip2region.xdb.Searcher;

/**
 * ip地址工具
 *
 *
 */
public class AddressUtil {

    /**
     * 根据IP地址查询登录来源
     *
     */
    public static String getCityInfo(String ip) {
        try {
            Searcher searcher = Searcher.newWithFileOnly("ipdb/ip2region.xdb");
            //开始查询
            return searcher.search(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //默认返回空字符串
        return "";
    }

    public static void main(String[] args) {
        //204.16.111.255
        System.out.println(getCityInfo("204.16.111.255"));
    }

}