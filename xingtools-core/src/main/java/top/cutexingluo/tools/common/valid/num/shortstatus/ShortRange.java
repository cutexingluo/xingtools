package top.cutexingluo.tools.common.valid.num.shortstatus;

/**
 * short 类型范围注解
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 18:47
 * @since 1.1.2
 */
public @interface ShortRange {
    /**
     * 最小值(包含)
     *
     * @return min value (inclusive)
     */
    short min() default 0;

    /**
     * 最大值(包含)
     *
     * @return max value (inclusive)
     */
    short max() default 0;
}
