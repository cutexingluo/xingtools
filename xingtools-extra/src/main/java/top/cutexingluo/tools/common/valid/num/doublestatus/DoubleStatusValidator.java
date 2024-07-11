package top.cutexingluo.tools.common.valid.num.doublestatus;

import top.cutexingluo.tools.common.valid.StatusValidator;
import top.cutexingluo.tools.utils.se.array.XTArrayUtil;

import javax.validation.ConstraintValidatorContext;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 18:47
 * @since 1.0.3
 */
public class DoubleStatusValidator extends StatusValidator<DoubleStatus, Double> {

    protected DoubleStatusConfig statusConfig;

    @Override
    public void initialize(DoubleStatus constraintAnnotation) {
        statusConfig = new DoubleStatusConfig(
                constraintAnnotation.notNull(),
                XTArrayUtil.toList(constraintAnnotation.matchNum()),
                constraintAnnotation.limit(),
                constraintAnnotation.eps(),
                constraintAnnotation.min(),
                constraintAnnotation.max(),
                constraintAnnotation.range()
        );
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext constraintValidatorContext) {
        if (!statusConfig.notNull && value == null) {
            return true;
        }
        if (value != null) {
            if (!statusConfig.matchNum.isEmpty()) {
                for (int i = 0; i < statusConfig.matchNum.size(); i++) {
                    if (Math.abs(statusConfig.matchNum.get(i).compareTo(value)) <= statusConfig.eps) {
                        return true;
                    }
                }
            }
            if (statusConfig.limit) {
                double valueUp = Math.min(value + statusConfig.eps, Double.MAX_VALUE);
                double valueDown = Math.max(value - statusConfig.eps, -Double.MAX_VALUE);
                if (valueUp < statusConfig.min) {
                    return false;
                } else if (valueDown > statusConfig.max) {
                    return false;
                }
                if (statusConfig.range.length > 0) {
                    for (int i = 0; i < statusConfig.range.length; i++) {
                        if (valueUp >= statusConfig.range[i].min() && valueDown <= statusConfig.range[i].max()) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }
}
