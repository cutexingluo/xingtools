package top.cutexingluo.tools.common.valid.num.intstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * int 状态校验配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/19 17:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntStatusConfig {

    /**
     * 非空
     */
    boolean notNull = false;

    /**
     * 匹配数字
     */
    Set<Integer> matchNum = null;

    /**
     *限制
     */
    boolean limit = false;

    /**
     * 最小值(包含)
     *
     * <p> min value (inclusive)</p>
     */
    int min = 0;

    /**
     * 最大值
     *
     * <p>max value (inclusive)</p>
     */
    int max = Integer.MAX_VALUE;

    /**
     * 区间
     */
    IntRangeData[] range;


}
