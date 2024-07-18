package top.cutexingluo.tools.utils.se.string;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * pick 替换工具类
 * <p>
 * 专门替换 ${} 字符串
 *
 * <p>于 1.0.4 版本翻新</p>
 * </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 14:42
 * @update 1.0.4
 * @updateFrom 2024/1/9 14:32
 */
public class XTPickUtil {


    /**
     * 截取 fromIndex 开始 第一个单个括号内的内容
     * <p>${}</p>
     */
    @Nullable
    public static String getKeyFromBraces(String line, int fromIndex) {
        XTString string = new XTString(line);
        return string.getKeyBetweenPatterns("${", "}", fromIndex);
    }


    /**
     * 替换第一个单个括号内的内容
     * <p>${}</p>
     */
    // 替换
    public static String putValueFromBraces(String line, String value) {
        return putValueFromBraces(line, null, (s) -> value);
    }

    /**
     * 替换第一个单个括号内的内容
     * <p>${}</p>
     */
    // 替换
    public static String putValueFromBraces(String line, Predicate<String> isContains, String value) {
        return putValueFromBraces(line, isContains, (s) -> value);
    }

    /**
     * 替换第一个单个括号内的内容
     * <p>${}</p>
     */
    //替换
    public static String putValueFromBraces(String line, Predicate<String> isContains, Function<String, String> map) {
        XTString string = new XTString(line);
        return string.replaceBetweenPatterns("${", "}", (s) -> {
            if (isContains != null) {
                boolean test = isContains.test(s);
                if (!test) return s;
            }
            if (map != null) {
                return map.apply(s);
            }
            return s;
        });
    }

    /**
     * 取出第一个单个括号内的内容
     * <p>${}</p>
     */
    // 取出
    public static String takeValueFromBraces(String line, Function<String, String> map) {
        XTString string = new XTString(line);
        return string.replaceBetweenPatterns("${", "}", map);
    }

    /**
     * 替换所有括号内的内容
     * <p>${}</p>
     */
    // 替换
    public static String putAllValueFromBraces(String line, String value) {
        return putAllValueFromBraces(line, null, (s) -> value);
    }

    /**
     * 替换所有括号内的内容
     * <p>${}</p>
     */
    // 替换
    public static String putAllValueFromBraces(String line, Predicate<String> isContains, String value) {
        return putAllValueFromBraces(line, isContains, (s) -> value);
    }

    /**
     * 替换所有括号内的内容
     * <p>${}</p>
     */
    //替换
    public static String putAllValueFromBraces(String line, Predicate<String> isContains, Function<String, String> map) {
        XTString string = new XTString(line);
        return string.replaceAllBetweenPatterns("${", "}", (s) -> {
            if (isContains != null) {
                boolean test = isContains.test(s);
                if (!test) return s;
            }
            if (map != null) {
                return map.apply(s);
            }
            return s;
        });
    }

    /**
     * 取出所有括号内的内容
     * <p>${}</p>
     */
    // 取出
    public static String takeAllValueFromBraces(String line, Function<String, String> map) {
        XTString string = new XTString(line);
        return string.replaceAllBetweenPatterns("${", "}", map);
    }


    /**
     * 清除多余转义字符 \
     * <p>\${=> ${</p>
     * <p>\}=> }</p>
     * <p>{@link XTString} 已经提供了该功能</p>
     */
    @NotNull
    @Contract(pure = true)
    public static String refreshBraces(@NotNull String line) {
        String tmp = line.replaceAll("\\\\\\$\\{", "\\$\\{");
        return tmp.replaceAll("\\\\}", "\\}");
    }

}
