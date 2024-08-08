package top.cutexingluo.tools.common.valid.num.floatstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * float 状态校验配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 16:12
 * @since 1.1.2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FloatStatusConfig {
    /**
     * 非空
     */
    boolean notNull = false;
    /**
     * 匹配数字
     */
    Set<Float> matchNum = null;
    /**
     * 限制
     */
    boolean limit = false;

    /**
     * 误差
     */
    float eps = 1E-6f;

    /**
     * 最小值(包含)
     *
     * <p> min value (inclusive)</p>
     */
    float min = 0;
    /**
     * 最大值
     *
     * <p>max value (inclusive)</p>
     */
    float max = Float.MAX_VALUE;

    /**
     * 区间
     */
    FloatRangeData[] range;


}
