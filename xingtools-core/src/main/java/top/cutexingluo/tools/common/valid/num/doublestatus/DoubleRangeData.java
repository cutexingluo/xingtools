package top.cutexingluo.tools.common.valid.num.doublestatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * double 类型范围数据
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/17 10:49
 * @since 1.1.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoubleRangeData {
    /**
     * 区间最小值(包含)
     *
     * <p> min value (inclusive)</p>
     */
    protected double min;
    /**
     * 区间最大值
     *
     * <p>max value (inclusive)</p>
     */
    protected double max;


    /**
     * 转化注解
     */
    public DoubleRangeData(@NotNull DoubleRange range) {
        min = range.min();
        max = range.max();
    }


    @NotNull
    public static DoubleRangeData[] parse(@NotNull DoubleRange[] ranges) {
        DoubleRangeData[] rangeDataArray = new DoubleRangeData[ranges.length];
        for (int i = 0; i < ranges.length; i++) {
            if (ranges[i] != null) {
                rangeDataArray[i] = new DoubleRangeData(ranges[i]);
            }
        }
        return rangeDataArray;
    }
}
