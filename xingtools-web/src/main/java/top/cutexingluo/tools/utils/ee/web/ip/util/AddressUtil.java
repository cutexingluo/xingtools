package top.cutexingluo.tools.utils.ee.web.ip.util;

import org.lionsoul.ip2region.xdb.Searcher;

import java.util.function.Consumer;

/**
 * ip地址工具
 */
public class AddressUtil {

    public static boolean printTrace = true;
    public static Consumer<Exception> exceptionHandler = null;

    /**
     * 根据IP地址查询登录来源
     */
    public static String getCityInfo(String ip) {
        try {
            Searcher searcher = Searcher.newWithFileOnly("ipdb/ip2region.xdb");
            //开始查询
            return searcher.search(ip);
        } catch (Exception e) {
            if (exceptionHandler != null) exceptionHandler.accept(e);
            else if (printTrace) e.printStackTrace();
        }
        //默认返回空字符串
        return "";
    }

    public static void main(String[] args) {
        //204.16.111.255
        System.out.println(getCityInfo("204.16.111.255"));
    }

}