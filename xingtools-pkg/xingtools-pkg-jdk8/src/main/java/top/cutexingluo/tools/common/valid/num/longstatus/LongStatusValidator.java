package top.cutexingluo.tools.common.valid.num.longstatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.cutexingluo.tools.common.valid.StatusValidator;
import top.cutexingluo.tools.utils.se.map.XTSetUtil;

import javax.validation.ConstraintValidatorContext;

/**
 * LongStatus 检验器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 16:12
 * @since 1.0.3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LongStatusValidator extends StatusValidator<LongStatus, Long> {

    protected LongStatusConfig statusConfig;

    protected LongValidator validator;

    @Override
    public void initialize(LongStatus constraintAnnotation) {
        statusConfig = new LongStatusConfig(
                constraintAnnotation.notNull(),
                XTSetUtil.toSet(constraintAnnotation.matchNum()),
                constraintAnnotation.limit(),
                constraintAnnotation.min(),
                constraintAnnotation.max(),
                LongRangeData.parse(constraintAnnotation.range())
        );
        validator = new LongValidator(statusConfig);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {
        return validator.isValid(value);
    }
}
