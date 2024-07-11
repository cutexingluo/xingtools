package top.cutexingluo.tools.common.valid.num;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 18:47
 * @since 1.0.3
 */
public @interface IntRange {
    /**
     * 最小值(包含)
     *
     * @return min value (inclusive)
     */
    int min() default 0;

    /**
     * 最大值(包含)
     *
     * @return max value (inclusive)
     */
    int max() default 0;
}
