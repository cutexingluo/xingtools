package top.cutexingluo.tools.common.valid.num.doublestatus;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Double 验证器
 *
 * <p>需要导入 spring-boot-starter-validation 包</p>
 * <ul>
 *     执行顺序
 *     <li>是否notNull，未设置则 null 会通过，建议@NotNull</li>
 *     <li>先进行数字精确匹配 matchNum</li>
 *     <li>再进行数字范围模糊匹配 limit</li>
 * </ul>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 18:47
 * @since 1.0.3
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {DoubleStatusValidator.class})
public @interface DoubleStatus {


    /**
     * 1.是否非空 null
     * <p>也可以使用 @NotNull 注解</p>
     * <p>不设置这个，如果为null则会通过</p>
     *
     * @return boolean
     */
    boolean notNull() default false;


    /**
     * 2.只要目标满足指定数字即可通过,无视大小限制
     *
     * @return {@link double[]}
     */
    double[] matchNum() default {};


    /**
     * 3.是否限制大小
     *
     * @return boolean
     */
    boolean limit() default false;


    /**
     * 精确匹配和范围匹配时，允许的误差
     */
    double eps() default 1E-6;

    /**
     * <p>limit开启</p>
     * 3.最小长度
     *
     * @return double
     */
    double min() default 0;

    /**
     * <p>limit开启</p>
     * 3.最大长度
     *
     * @return double
     */
    double max() default Double.MAX_VALUE;


    /**
     * 4.范围限制
     * <p>如果设置了range，则判定是否在此范围内, [ inclusive, inclusive] </p>
     * <p>可以设置多个</p>
     *
     * @return {@link DoubleRange[]}
     * @since 1.0.3
     */
    DoubleRange[] range() default {};


    /**
     * 不满足返回的消息
     *
     * @return {@link String}
     */
    String message() default "The transfer data is in the wrong format";


    /**
     * 分组
     *
     * @return {@link Class}<{@link ?}>{@link []}
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
