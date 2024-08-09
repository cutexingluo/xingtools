package top.cutexingluo.tools.common.valid.num.floatstatus;

/**
 * float 类型范围注解
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 18:47
 * @since 1.1.2
 */
public @interface FloatRange {
    /**
     * 最小值(包含)
     *
     * @return min value (inclusive)
     */
    float min() default 0;

    /**
     * 最大值(包含)
     *
     * @return max value (inclusive)
     */
    float max() default 0;
}
