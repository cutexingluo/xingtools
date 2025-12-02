package top.cutexingluo.tools.common.valid.num.shortstatus;

import jakarta.validation.ConstraintValidatorContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.cutexingluo.core.common.valid.num.shortstatus.ShortRangeData;
import top.cutexingluo.core.common.valid.num.shortstatus.ShortStatusConfig;
import top.cutexingluo.core.common.valid.num.shortstatus.ShortValidator;
import top.cutexingluo.core.utils.se.map.XTSetUtil;
import top.cutexingluo.tools.common.valid.StatusValidator;


/**
 * ShortStatus 检验器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/19 16:12
 * @since 1.1.2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ShortStatusValidator extends StatusValidator<ShortStatus, Short> {

    protected ShortStatusConfig statusConfig;

    protected ShortValidator validator;

    @Override
    public void initialize(ShortStatus constraintAnnotation) {
        statusConfig = new ShortStatusConfig(
                constraintAnnotation.notNull(),
                XTSetUtil.toSet(constraintAnnotation.matchNum()),
                constraintAnnotation.limit(),
                constraintAnnotation.min(),
                constraintAnnotation.max(),
                ShortRangeData.parse(constraintAnnotation.range())
        );
        validator = new ShortValidator(statusConfig);
    }

    @Override
    public boolean isValid(Short value, ConstraintValidatorContext constraintValidatorContext) {
        return validator.isValid(value);
    }
}
