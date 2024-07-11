package top.cutexingluo.tools.common.valid.num;

import top.cutexingluo.tools.common.valid.StatusValidator;
import top.cutexingluo.tools.utils.se.map.XTSetUtil;

import javax.validation.ConstraintValidatorContext;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/19 16:12
 */
public class IntStatusValidator extends StatusValidator<IntStatus, Integer> {

    protected IntStatusConfig statusConfig;

    @Override
    public void initialize(IntStatus constraintAnnotation) {
        statusConfig = new IntStatusConfig(
                constraintAnnotation.notNull(),
                XTSetUtil.toSet(constraintAnnotation.matchNum()),
                constraintAnnotation.limit(),
                constraintAnnotation.min(),
                constraintAnnotation.max(),
                constraintAnnotation.range()
        );
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if (!statusConfig.notNull && value == null) {
            return true;
        }
        if (value != null) {
            if (!statusConfig.matchNum.isEmpty() && statusConfig.matchNum.contains(value)) {
                return true;
            }
            if (statusConfig.limit) {
                if (value < statusConfig.min) {
                    return false;
                } else if (value > statusConfig.max) {
                    return false;
                }
                if (statusConfig.range.length > 0) {
                    for (int i = 0; i < statusConfig.range.length; i++) {
                        if (value >= statusConfig.range[i].min() && value <= statusConfig.range[i].max()) {
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
