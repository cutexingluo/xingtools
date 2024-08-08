package top.cutexingluo.tools.common.valid.num.intstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * int 类型范围数据
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/17 10:49
 * @since 1.1.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntRangeData {
    /**
     * 区间最小值(包含)
     *
     * <p> min value (inclusive)</p>
     */
    protected int min;
    /**
     * 区间最大值
     *
     * <p>max value (inclusive)</p>
     */
    protected int max;

    /**
     * 转化注解
     */
    public IntRangeData(@NotNull IntRange range) {
        min = range.min();
        max = range.max();
    }

    @NotNull
    public static IntRangeData[] parse(@NotNull IntRange[] ranges) {
        IntRangeData[] rangeDataArray = new IntRangeData[ranges.length];
        for (int i = 0; i < ranges.length; i++) {
            if (ranges[i] != null) {
                rangeDataArray[i] = new IntRangeData(ranges[i]);
            }
        }
        return rangeDataArray;
    }
}
