package top.cutexingluo.tools.common.valid.num.floatstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * float 类型范围数据
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/17 10:49
 * @since 1.1.2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FloatRangeData {
    /**
     * 区间最小值(包含)
     *
     * <p> min value (inclusive)</p>
     */
    protected float min;
    /**
     * 区间最大值
     *
     * <p>max value (inclusive)</p>
     */
    protected float max;

    /**
     * 转化注解
     */
    public FloatRangeData(@NotNull FloatRange range) {
        min = range.min();
        max = range.max();
    }

    @NotNull
    public static FloatRangeData[] parse(@NotNull FloatRange[] ranges) {
        FloatRangeData[] rangeDataArray = new FloatRangeData[ranges.length];
        for (int i = 0; i < ranges.length; i++) {
            if (ranges[i] != null) {
                rangeDataArray[i] = new FloatRangeData(ranges[i]);
            }
        }
        return rangeDataArray;
    }
}
