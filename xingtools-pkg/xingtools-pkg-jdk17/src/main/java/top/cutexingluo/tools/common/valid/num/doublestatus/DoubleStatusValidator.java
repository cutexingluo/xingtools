package top.cutexingluo.tools.common.valid.num.doublestatus;

import jakarta.validation.ConstraintValidatorContext;
import top.cutexingluo.tools.common.valid.StatusValidator;
import top.cutexingluo.tools.utils.se.array.XTArrayUtil;

/**
 * DoubleStatus 检验器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 18:47
 * @since 1.0.3
 */
public class DoubleStatusValidator extends StatusValidator<DoubleStatus, Double> {

    protected DoubleStatusConfig statusConfig;

    protected DoubleValidator validator;

    @Override
    public void initialize(DoubleStatus constraintAnnotation) {
        statusConfig = new DoubleStatusConfig(
                constraintAnnotation.notNull(),
                XTArrayUtil.toList(constraintAnnotation.matchNum()),
                constraintAnnotation.limit(),
                constraintAnnotation.eps(),
                constraintAnnotation.min(),
                constraintAnnotation.max(),
                DoubleRangeData.parse(constraintAnnotation.range())
        );
        validator = new DoubleValidator(statusConfig);
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext constraintValidatorContext) {
        return validator.isValid(value);
    }
}