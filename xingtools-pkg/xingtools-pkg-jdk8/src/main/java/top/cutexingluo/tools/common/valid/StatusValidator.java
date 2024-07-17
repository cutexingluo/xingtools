package top.cutexingluo.tools.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * 初始检验器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/19 16:12
 */
public class StatusValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {


    @Override
    public void initialize(A constraintAnnotation) {

    }

    @Override
    public boolean isValid(T t, ConstraintValidatorContext constraintValidatorContext) {
        return t != null;
    }
}
