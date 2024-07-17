package top.cutexingluo.tools.common.valid.num.longstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * long 状态校验配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 16:12
 * @since 1.0.3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LongStatusConfig {
    /**
     * 非空
     */
    boolean notNull = false;
    /**
     * 匹配数字
     */
    Set<Long> matchNum = null;
    /**
     *限制
     */
    boolean limit = false;
    /**
     * 最小值(包含)
     *
     * <p> min value (inclusive)</p>
     */
    long min = 0;
    /**
     * 最大值
     *
     * <p>max value (inclusive)</p>
     */
    long max = Long.MAX_VALUE;

    /**
     * 区间
     */
    LongRangeData[] range;


}
