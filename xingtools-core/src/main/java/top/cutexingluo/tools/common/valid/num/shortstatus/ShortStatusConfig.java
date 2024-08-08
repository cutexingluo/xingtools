package top.cutexingluo.tools.common.valid.num.shortstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * short 状态校验配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 16:12
 * @since 1.1.2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortStatusConfig {
    /**
     * 非空
     */
    boolean notNull = false;
    /**
     * 匹配数字
     */
    Set<Short> matchNum = null;
    /**
     * 限制
     */
    boolean limit = false;
    /**
     * 最小值(包含)
     *
     * <p> min value (inclusive)</p>
     */
    short min = 0;
    /**
     * 最大值
     *
     * <p>max value (inclusive)</p>
     */
    short max = Short.MAX_VALUE;

    /**
     * 区间
     */
    ShortRangeData[] range;


}
