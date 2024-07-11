package top.cutexingluo.tools.common.valid.num.longstatus;

import top.cutexingluo.tools.common.valid.StatusValidator;
import top.cutexingluo.tools.utils.se.map.XTSetUtil;

import javax.validation.ConstraintValidatorContext;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 16:12
 * @since 1.0.3
 */
public class LongStatusValidator extends StatusValidator<LongStatus, Long> {

    protected LongStatusConfig statusConfig;

    @Override
    public void initialize(LongStatus constraintAnnotation) {
        statusConfig = new LongStatusConfig(
                constraintAnnotation.notNull(),
                XTSetUtil.toSet(constraintAnnotation.matchNum()),
                constraintAnnotation.limit(),
                constraintAnnotation.min(),
                constraintAnnotation.max(),
                constraintAnnotation.range()
        );
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {
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
