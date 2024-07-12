package top.cutexingluo.tools.utils.se.office.EChartData;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;

import java.util.Date;
import java.util.List;

/**
 * 数据操作类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 21:37
 */
public class XTStatistics {

    /**
     * 获取每个季节数量
     */
    public static List<Integer> getQuarterCount(List<Date> dates) {
        int q1 = 0, q2 = 0, q3 = 0, q4 = 0;
        for (Date date : dates) {
            Quarter quarter = DateUtil.quarterEnum(date);
            switch (quarter) {
                case Q1:
                    q1++;
                    break;
                case Q2:
                    q2++;
                    break;
                case Q3:
                    q3++;
                    break;
                case Q4:
                    q4++;
                    break;
                default:
                    break;
            }
        }
        return CollUtil.newArrayList(q1, q2, q3, q4);
    }

    /**
     * 获取每个季节数量
     */
    public static List<Integer> getDateCount(XTDataType xtDataType, List<Date> objects) {
        if (xtDataType == null) return null;
        if (xtDataType.equals(XTDataType.QUARTER)) {
            return getQuarterCount(objects);
        }
        return null;
    }
}
