package top.cutexingluo.tools.common.valid.num.intstatus;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Integer 验证器
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
 * @date 2023/7/19 17:21
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {IntStatusValidator.class})
public @interface IntStatus {


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
     * @return {@link int[]}
     */
    int[] matchNum() default {};


    /**
     * 3.是否限制大小
     *
     * @return boolean
     */
    boolean limit() default false;

    /**
     * lenLimit开启<br>
     * 3.最小长度
     *
     * @return int
     */
    int min() default 0;

    /**
     * lenLimit开启<br>
     * 3.最大长度
     *
     * @return int
     */
    int max() default Integer.MAX_VALUE;


    /**
     * 4.范围限制
     * <p>如果设置了range，则判定是否在此范围内, [ inclusive, inclusive] </p>
     * <p>可以设置多个</p>
     *
     * @return {@link IntRange[]}
     * @since 1.0.3
     */
    IntRange[] range() default {};


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
