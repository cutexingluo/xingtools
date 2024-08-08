package top.cutexingluo.tools.common.valid.num.floatstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.cutexingluo.tools.common.valid.Validator;
import top.cutexingluo.tools.utils.se.algo.cpp.math.XTMath;

/**
 * Float 检验器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/17 12:21
 * @since 1.1.2
 */
@Data
@AllArgsConstructor
public class FloatValidator implements Validator<Float> {

    /**
     * 条件
     */
    protected FloatStatusConfig statusConfig;

    @Override
    public boolean isValid(Float value) {
        if (value == null) {
            return !statusConfig.notNull;
        } else {
            // 1.条件匹配
            if (statusConfig.matchNum != null && !statusConfig.matchNum.isEmpty()) {
                for (Float match : statusConfig.matchNum) {
                    if (Math.abs(match - value) <= statusConfig.eps) {
                        return true;
                    }
                }
                return false; // 匹配值不通过
            }
            // 2.大小限制
            if (statusConfig.limit) {
                float valueUp = XTMath.getUpValue(value, statusConfig.eps);
                float valueDown = XTMath.getDownValue(value, statusConfig.eps);
                if (valueUp < statusConfig.min || valueDown > statusConfig.max) {
                    return false;
                }
                if (statusConfig.range != null && statusConfig.range.length > 0) { //       区间限制
                    for (int i = 0; i < statusConfig.range.length; i++) {
                        if (statusConfig.range[i] != null) {
                            if (valueUp >= XTMath.getDownValue(statusConfig.range[i].getMin(), statusConfig.eps) &&
                                    valueDown <= XTMath.getUpValue(statusConfig.range[i].getMax(), statusConfig.eps)) {
                                return true; // 在区间内通过
                            }
                        }
                    }
                    return false; // 不在区间内
                }
            }
            return true; // 所有通过
        }
    }
}
