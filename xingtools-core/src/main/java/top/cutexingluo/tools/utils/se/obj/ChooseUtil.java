package top.cutexingluo.tools.utils.se.obj;

import cn.hutool.core.util.StrUtil;

import java.util.Objects;

/**
 * 选择工具类
 * <p>根据策略覆盖原值</p>
 * <p>原值存在则不覆盖</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/18 17:46
 * @since 1.0.4
 */
public class ChooseUtil {


    /**
     * 如果原始地址等于指定值地址，覆盖值地址不是指定值地址，则被覆盖
     *
     * @param originalValue  原始值
     * @param overrideValue  覆盖值
     * @param specifiedValue 指定值
     * @return 选择的值
     */
    public static <T> T checkAddrOverride(T originalValue, T overrideValue, T specifiedValue) {
        return originalValue == specifiedValue && overrideValue != specifiedValue ? overrideValue : originalValue;
    }

    /**
     * 如果原始值等于指定值，覆盖值不是指定值，则被覆盖
     *
     * @param originalValue  原始值
     * @param overrideValue  覆盖值
     * @param specifiedValue 指定值
     * @return 选择的值
     */
    public static <T> T checkValueOverride(T originalValue, T overrideValue, T specifiedValue) {
        return Objects.equals(originalValue, overrideValue) && Objects.equals(overrideValue, specifiedValue) ? overrideValue : originalValue;
    }

    /**
     * 如果原始值等于 null ，覆盖值不是 null，则被覆盖
     *
     * @param originalValue 原始值
     * @param overrideValue 覆盖值
     * @return 选择的值
     */
    public static <T> T checkNullOverride(T originalValue, T overrideValue) {
        return checkAddrOverride(originalValue, overrideValue, null);
    }

    /**
     * 如果原始值等于指定值，覆盖值不是指定值，则被覆盖
     *
     * @param originalValue 原始值
     * @param overrideValue 覆盖值
     * @return 选择的值
     */
    public static boolean checkOverride(boolean originalValue, boolean overrideValue, boolean specifiedValue) {
        return originalValue == specifiedValue && overrideValue != specifiedValue ? overrideValue : originalValue;
    }


    /**
     * 如果原始值等于指定值，覆盖值不是指定值，则被覆盖
     *
     * @param originalValue  原始值
     * @param overrideValue  覆盖值
     * @param specifiedValue 指定值
     * @return 选择的值
     */
    public static String checkOverride(String originalValue, String overrideValue, String specifiedValue) {
        return Objects.equals(originalValue, specifiedValue) && Objects.equals(overrideValue, specifiedValue) ? overrideValue : originalValue;
    }

    /**
     * 如果原始值为空，覆盖值不为空，则被覆盖
     *
     * @param originalValue 原始值
     * @param overrideValue 覆盖值
     * @return 选择的值
     */
    public static String checkBlankOverride(String originalValue, String overrideValue) {
        return StrUtil.isBlank(originalValue) && StrUtil.isNotBlank(overrideValue) ? overrideValue : originalValue;
    }


    /**
     * 如果原始值等于指定值，覆盖值不是指定值，则被覆盖
     *
     * @param originalValue  原始值
     * @param overrideValue  覆盖值
     * @param specifiedValue 指定值
     * @return 选择的值
     */
    public static int checkOverride(int originalValue, int overrideValue, int specifiedValue) {
        return originalValue == specifiedValue && overrideValue != specifiedValue ? overrideValue : originalValue;
    }

    /**
     * 如果原始值等于0，覆盖值不是0，则被覆盖
     *
     * @param originalValue 原始值
     * @param overrideValue 覆盖值
     * @return 选择的值
     */
    public static int checkZeroOverride(int originalValue, int overrideValue) {
        return checkOverride(originalValue, overrideValue, 0);
    }

    /**
     * 如果原始值等于指定值，覆盖值不是指定值，则被覆盖
     *
     * @param originalValue  原始值
     * @param overrideValue  覆盖值
     * @param specifiedValue 指定值
     * @return 选择的值
     */
    public static long checkOverride(long originalValue, long overrideValue, long specifiedValue) {
        return originalValue == specifiedValue && overrideValue != specifiedValue ? overrideValue : originalValue;
    }

    /**
     * 如果原始值等于0，覆盖值不是0，则被覆盖
     *
     * @param originalValue 原始值
     * @param overrideValue 覆盖值
     * @return 选择的值
     */
    public static long checkZeroOverride(long originalValue, long overrideValue) {
        return checkOverride(originalValue, overrideValue, 0L);
    }

    /**
     * 如果原始值等于指定值，覆盖值不是指定值，则被覆盖
     *
     * @param originalValue  原始值
     * @param overrideValue  覆盖值
     * @param specifiedValue 指定值
     * @param eps            精度 如 1e-6
     * @return 选择的值
     */
    public static double checkOverride(double originalValue, double overrideValue, double specifiedValue, double eps) {
        return Math.abs(originalValue - specifiedValue) <= eps && Math.abs(overrideValue - specifiedValue) > eps ? overrideValue : originalValue;
    }


}
