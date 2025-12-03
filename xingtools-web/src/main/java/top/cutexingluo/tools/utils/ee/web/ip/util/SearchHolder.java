package top.cutexingluo.tools.utils.ee.web.ip.util;

import org.lionsoul.ip2region.xdb.Searcher;

/**
 * ip地址查询器持有者
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/12/1 21:32
 */
public class SearchHolder {

    protected static Searcher searcher;

    public static Searcher getSearcher() {
        if (searcher == null) {
            try {
                searcher = Searcher.newWithFileOnly("ipdb/ip2region.xdb");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return searcher;
    }
}
