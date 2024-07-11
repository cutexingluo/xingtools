package top.cutexingluo.tools.common.valid.str;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * String 验证器
 *
 * <p>需要导入 spring-boot-starter-validation 包</p>
 *
 * <ul>
 *     执行顺序
 *     <li>是否notNull，未设置则 null 会通过，等同@NotNull</li>
 *     <li>是否 notBlankIfPresent  未设置则 "" 会通过，<br>
 *     1.如果和上一条一起开启等同@NotBlank; <br>
 *     2.如果上一条不开启这条开启，则 null 通过 "" 不通过,  适用于很多框架的update</i>
 *     <li>先进行长度范围匹配 lenLimit</li>
 *     <li>再进行字符串精确匹配 anyStr</li>
 *     <li>字符串精确匹配不匹配  则进行字符串正则匹配 anyReg</li>
 *     <li>都不匹配则不通过</li>
 * </ul>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/19 16:15
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {StrStatusValidator.class})
public @interface StrStatus {

    /**
     * 1.是否非空 null
     * <p>也可以使用 @NotNull 注解</p>
     * <p>不设置这个，如果为null则会通过</p>
     *
     * @return boolean
     */
    boolean notNull() default false;

    /**
     * 2.是否不为空字符串""
     * <p>不设置这个，如果为 ""  则会通过</p>
     *
     * @return boolean
     */
    boolean notBlankIfPresent() default false;

    /**
     * 3.是否限制长度
     *
     * @return boolean
     */
    boolean lenLimit() default false;

    /**
     * lenLimit开启<br>
     * 3.最小长度
     *
     * @return int
     */
    int minLength() default 0;

    /**
     * lenLimit开启<br>
     * 3.最大长度
     *
     * @return int
     */
    int maxLength() default Integer.MAX_VALUE;


    /**
     * 4.只要目标满足任意字符串即可通过
     *
     * @return {@link String[]}
     */
    String[] anyStr() default {};

    /**
     * 5.只要目标满足任意正则表达式即可通过
     *
     * @return {@link String[]}
     */
    String[] anyReg() default {};


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
