package top.cutexingluo.tools.common.valid.num.shortstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * short 类型范围数据
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/17 10:49
 * @since 1.1.2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortRangeData {
    /**
     * 区间最小值(包含)
     *
     * <p> min value (inclusive)</p>
     */
    protected short min;
    /**
     * 区间最大值
     *
     * <p>max value (inclusive)</p>
     */
    protected short max;

    /**
     * 转化注解
     */
    public ShortRangeData(@NotNull ShortRange range) {
        min = range.min();
        max = range.max();
    }

    @NotNull
    public static ShortRangeData[] parse(@NotNull ShortRange[] ranges) {
        ShortRangeData[] rangeDataArray = new ShortRangeData[ranges.length];
        for (int i = 0; i < ranges.length; i++) {
            if (ranges[i] != null) {
                rangeDataArray[i] = new ShortRangeData(ranges[i]);
            }
        }
        return rangeDataArray;
    }
}
