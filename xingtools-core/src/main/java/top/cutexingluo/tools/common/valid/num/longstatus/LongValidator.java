package top.cutexingluo.tools.common.valid.num.longstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.cutexingluo.tools.common.valid.Validator;

/**
 * Long 检验器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/17 12:21
 * @since 1.1.1
 */
@Data
@AllArgsConstructor
public class LongValidator implements Validator<Long> {

    /**
     * 条件
     */
    protected LongStatusConfig statusConfig;

    @Override
    public boolean isValid(Long value) {
        if (value == null) {
            return !statusConfig.notNull;
        } else {
            // 1.条件匹配
            if (statusConfig.matchNum != null && !statusConfig.matchNum.isEmpty()) {
                return statusConfig.matchNum.contains(value); // 开始通过
            }
            // 2.大小限制
            if (statusConfig.limit) {
                if (value < statusConfig.min || value > statusConfig.max) { // 进入条件一
                    return false;
                }
                if (statusConfig.range != null && statusConfig.range.length > 0) {  // 进入条件二
                    for (int i = 0; i < statusConfig.range.length; i++) {
                        if (statusConfig.range[i] != null) {
                            if (value >= statusConfig.range[i].getMin() && value <= statusConfig.range[i].getMax()) {
                                return true;
                            }
                        }
                    }
                    return false; // 二未通过
                }
            }
            return true; //pass
        }
    }
}
