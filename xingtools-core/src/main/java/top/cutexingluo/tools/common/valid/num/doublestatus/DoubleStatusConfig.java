package top.cutexingluo.tools.common.valid.num.doublestatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * double 状态校验配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 18:47
 * @since 1.0.3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoubleStatusConfig {

    /**
     * 非空
     */
    boolean notNull = false;

    /**
     * 匹配数字
     */
    List<Double> matchNum = null;


    /**
     * 限制
     */
    boolean limit = false;

    /**
     * 误差
     */
    double eps = 1E-6;

    /**
     * 最小值(包含)
     *
     * <p> min value (inclusive)</p>
     */
    double min = 0;

    /**
     * 最大值
     *
     * <p>max value (inclusive)</p>
     */
    double max = Double.MAX_VALUE;

    /**
     * 区间
     */
    DoubleRangeData[] range;


}
