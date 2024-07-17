package top.cutexingluo.tools.common.valid.num.longstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * long 类型范围数据
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/17 10:49
 * @since 1.1.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LongRangeData {
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
    public LongRangeData(@NotNull LongRange range) {
        min = range.min();
        max = range.max();
    }

    @NotNull
    public static LongRangeData[] parse(@NotNull LongRange[] ranges) {
        LongRangeData[] rangeDataArray = new LongRangeData[ranges.length];
        for (int i = 0; i < ranges.length; i++) {
            if (ranges[i] != null) {
                rangeDataArray[i] = new LongRangeData(ranges[i]);
            }
        }
        return rangeDataArray;
    }
}
