package top.cutexingluo.tools.common.valid.num.floatstatus;

import jakarta.validation.ConstraintValidatorContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.cutexingluo.tools.common.valid.StatusValidator;
import top.cutexingluo.tools.utils.se.map.XTSetUtil;


/**
 * FloatStatus 检验器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 18:47
 * @since 1.1.2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class FloatStatusValidator extends StatusValidator<FloatStatus, Float> {

    protected FloatStatusConfig statusConfig;

    protected FloatValidator validator;

    @Override
    public void initialize(FloatStatus constraintAnnotation) {
        statusConfig = new FloatStatusConfig(
                constraintAnnotation.notNull(),
                XTSetUtil.toSet(constraintAnnotation.matchNum()),
                constraintAnnotation.limit(),
                constraintAnnotation.eps(),
                constraintAnnotation.min(),
                constraintAnnotation.max(),
                FloatRangeData.parse(constraintAnnotation.range())
        );
        validator = new FloatValidator(statusConfig);
    }

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext constraintValidatorContext) {
        return validator.isValid(value);
    }
}
