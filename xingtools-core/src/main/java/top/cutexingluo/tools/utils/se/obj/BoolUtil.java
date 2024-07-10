package top.cutexingluo.tools.utils.se.obj;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * bool 工具类
 * <p>用于解析 boolean 和 Boolean 变量</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/22 13:38
 * @since 1.0.3
 */

public class BoolUtil {

    /**
     * true to "true" , false to "false"
     */
    @NotNull
    @Contract(pure = true)
    public static String getString(boolean value) {
        return value ? "true" : "false";
    }

    /**
     * true to "true" , false to "false" , null to nullDefaultValue
     */
    public static String getString(Boolean value, String nullDefaultValue) {
        if (value == null) return nullDefaultValue;
        return value ? "true" : "false";
    }

    /**
     * true to "true" , false to "false" , null to ""
     */
    @NotNull
    @Contract(pure = true)
    public static String getString(Boolean value) {
        if (value == null) return "";
        return value ? "true" : "false";
    }

    /**
     * if value equals null or 0 , return false;
     * otherwise return true
     */
    public static <T extends Number> boolean toBoolean(T value) {
        if (value == null) return false;
        if (value instanceof Integer) return value.intValue() != 0;
        else if (value instanceof Long) return value.longValue() != 0;
        else if (value instanceof Float) return value.floatValue() != 0;
        else if (value instanceof Byte) return value.byteValue() != 0;
        else if (value instanceof Short) return value.shortValue() != 0;
        return value.doubleValue() != 0;
    }

}
