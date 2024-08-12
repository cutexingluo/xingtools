package top.cutexingluo.tools.common.valid.num.intstatus;

import jakarta.validation.ConstraintValidatorContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.cutexingluo.tools.common.valid.StatusValidator;
import top.cutexingluo.tools.utils.se.map.XTSetUtil;

/**
 * IntStatus 检验器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/19 16:12
 * @since 1.0.3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class IntStatusValidator extends StatusValidator<IntStatus, Integer> {

    protected IntStatusConfig statusConfig;

    protected IntValidator validator;

    @Override
    public void initialize(IntStatus constraintAnnotation) {
        statusConfig = new IntStatusConfig(
                constraintAnnotation.notNull(),
                XTSetUtil.toSet(constraintAnnotation.matchNum()),
                constraintAnnotation.limit(),
                constraintAnnotation.min(),
                constraintAnnotation.max(),
                IntRangeData.parse(constraintAnnotation.range())
        );
        validator = new IntValidator(statusConfig);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return validator.isValid(value);
    }
}
