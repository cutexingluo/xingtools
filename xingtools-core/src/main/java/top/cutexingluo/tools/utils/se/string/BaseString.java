package top.cutexingluo.tools.utils.se.string;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * 基础 string 接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/20 20:14
 * @since 1.0.2
 */
public interface BaseString {
    String getString();

    boolean isEmpty();

    boolean isBlank();

    boolean isNotEmpty();

    boolean isNotBlank();

    /**
     * 查找字符串，不考虑前导符号
     *
     * @param pattern      字符串
     * @param fromIndex    从第几个字符开始
     * @param symbol       symbol 前导符号 , 转义符号
     * @param deleteSymbol 是否删除前导符号, 转义符号
     * @param useJdk       是否使用 jdk 的 indexOf , false 使用 XTStrUtil 算法
     * @return 索引下标,  -1 表示没找到
     */
    int indexOfNoPreSymbol(@NotNull String pattern, int fromIndex, char symbol, boolean deleteSymbol, boolean useJdk);


    default int indexOfNoPreBackSlash(@NotNull String pattern, int fromIndex, boolean deleteSymbol, boolean useJdk) {
        return indexOfNoPreSymbol(pattern, fromIndex, '\\', deleteSymbol, useJdk);
    }

    default int indexOfNoPreBackSlash(@NotNull String pattern, int fromIndex, boolean deleteSymbol) {
        return indexOfNoPreSymbol(pattern, fromIndex, '\\', deleteSymbol, true);
    }

    default int indexOfNoPreBackSlash(@NotNull String pattern, int fromIndex) {
        return indexOfNoPreSymbol(pattern, fromIndex, '\\', true, true);
    }

    default int indexOfNoPreBackSlash(@NotNull String pattern) {
        return indexOfNoPreSymbol(pattern, 0, '\\', true, true);
    }


    /**
     * 字符串里面取出第一对模式串取出之间的字符串，并进行相应的操作
     *
     * @param beginPattern 开始模式串
     * @param endPattern   结束模式串
     * @param fromIndex    从第几个字符开始
     * @param symbol       symbol 前导符号 , 转义符号
     * @param useJdk       是否使用 jdk 的 indexOf , false 使用 XTStrUtil 算法
     * @return 模式模式串之间的字符串
     * @since 1.0.4
     */
    String getKeyBetweenPatterns(@NotNull String beginPattern, @NotNull String endPattern, int fromIndex, char symbol, boolean deleteSymbol, boolean useJdk);

    default String getKeyBetweenPatterns(@NotNull String beginPattern, @NotNull String endPattern, int fromIndex) {
        return getKeyBetweenPatterns(beginPattern, endPattern, fromIndex, '\\', true, true);
    }

    default String getKeyBetweenPatterns(@NotNull String beginPattern, @NotNull String endPattern) {
        return getKeyBetweenPatterns(beginPattern, endPattern, 0, '\\', true, true);
    }

    /**
     * 字符串里面取出第一对模式串取出之间的字符串，并进行相应的操作
     *
     * @param beginPattern  开始模式串
     * @param endPattern    结束模式串
     * @param fromIndex     从第几个字符开始
     * @param existsReplace 若存在则替换
     * @param symbol        symbol 前导符号 , 转义符号
     * @param useJdk        是否使用 jdk 的 indexOf , false 使用 XTStrUtil 算法
     * @return 替换后的字符串
     * @since 1.0.4
     */
    String replaceBetweenPatterns(@NotNull String beginPattern, @NotNull String endPattern, int fromIndex, Function<String, String> existsReplace, char symbol, boolean deleteSymbol, boolean useJdk);


    default String replaceBetweenPatterns(@NotNull String beginPattern, @NotNull String endPattern, int fromIndex, Function<String, String> existsReplace) {
        return replaceBetweenPatterns(beginPattern, endPattern, fromIndex, existsReplace, '\\', true, true);
    }


    default String replaceBetweenPatterns(@NotNull String beginPattern, @NotNull String endPattern, Function<String, String> existsReplace) {
        return replaceBetweenPatterns(beginPattern, endPattern, 0, existsReplace, '\\', true, true);
    }


    /**
     * 字符串里面取出所有这对模式串取出之间的字符串，并进行相应的操作
     *
     * @param beginPattern  开始模式串
     * @param endPattern    结束模式串
     * @param fromIndex     从第几个字符开始
     * @param existsReplace 若存在则替换
     * @param symbol        symbol 前导符号 , 转义符号
     * @param useJdk        是否使用 jdk 的 indexOf , false 使用 XTStrUtil 算法
     * @return 替换后的字符串
     * @since 1.0.4
     */
    String replaceAllBetweenPatterns(@NotNull String beginPattern, @NotNull String endPattern, int fromIndex, Function<String, String> existsReplace, char symbol, boolean deleteSymbol, boolean useJdk);


    default String replaceAllBetweenPatterns(@NotNull String beginPattern, @NotNull String endPattern, int fromIndex, Function<String, String> existsReplace) {
        return replaceAllBetweenPatterns(beginPattern, endPattern, fromIndex, existsReplace, '\\', true, true);
    }


    default String replaceAllBetweenPatterns(@NotNull String beginPattern, @NotNull String endPattern, Function<String, String> existsReplace) {
        return replaceAllBetweenPatterns(beginPattern, endPattern, 0, existsReplace, '\\', true, true);
    }


}
