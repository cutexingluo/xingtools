package top.cutexingluo.tools.common.valid.num.doublestatus;

/**
 *double 类型范围注解
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 18:47
 * @since 1.0.3
 */
public @interface DoubleRange {
    /**
     * 最小值(包含)
     *
     * @return min value (inclusive)
     */
    double min() default 0;

    /**
     * 最大值(包含)
     *
     * @return max value (inclusive)
     */
    double max() default 0;
}
