package top.cutexingluo.tools.common.valid.str;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.cutexingluo.tools.common.valid.StatusValidator;
import top.cutexingluo.tools.utils.se.map.XTSetUtil;

import javax.validation.ConstraintValidatorContext;

/**
 * StrStatus 检验器
 *
 * @author XingTian
 * @version 1.0.1
 * @date 2023/7/19 16:12
 * @update 2023/12/8 16:12
 * @since 1.0.3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StrStatusValidator extends StatusValidator<StrStatus, String> {

    protected StrStatusConfig statusConfig;

    protected StrValidator validator;

    @Override
    public void initialize(StrStatus constraintAnnotation) {
        statusConfig = new StrStatusConfig(
                constraintAnnotation.notNull(),
                constraintAnnotation.notBlankIfPresent(),
                constraintAnnotation.lenLimit(),
                constraintAnnotation.minLength(),
                constraintAnnotation.maxLength(),
                XTSetUtil.toSet(constraintAnnotation.anyStr()),
                constraintAnnotation.anyReg()
        );
        validator = new StrValidator(statusConfig);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return validator.isValid(s);
    }
}
