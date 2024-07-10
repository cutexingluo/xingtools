package top.cutexingluo.tools.utils.se.string;

import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.se.character.XTStrUtil;

import java.util.function.Function;

/**
 * 字符串工具类
 * <p>继承 CPPString , 兼容 c++ string 方法名称</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/20 20:15
 * @since 1.0.2
 */
public class XTString extends CPPString implements BaseString {

    public XTString() {
        super();
    }


    /**
     * 默认不检测 null 值
     *
     * @param string 字符串
     */
    public XTString(String string) {
        super(string);
    }

    /**
     * @param string       字符串
     * @param autoFillNull 是否自动将空字符串 null 赋值为 ""
     */
    public XTString(String string, boolean autoFillNull) {
        super(string, autoFillNull);
    }

    /**
     * @param string           字符串
     * @param nullDefaultValue 当 string 为 null 时，默认赋值
     */
    public XTString(String string, String nullDefaultValue) {
        super(string, nullDefaultValue);
    }

    public XTString(BaseString baseString) {
        super(baseString);
    }

    @Override
    public String toString() {
        return this.string;
    }


    @Override
    public boolean isEmpty() {
        return StrUtil.isEmpty(this.string);
    }

    @Override
    public boolean isBlank() {
        return StrUtil.isBlank(this.string);
    }

    @Override
    public boolean isNotEmpty() {
        return StrUtil.isNotEmpty(this.string);
    }

    @Override
    public boolean isNotBlank() {
        return StrUtil.isNotBlank(this.string);
    }


    /**
     * 查找指定字符串在原字符串中第一次出现的索引位置
     * <p>基本方法</p>
     */
    protected int indexOfNoPreSymbol(@NotNull StringBuilder string, @NotNull String pattern, int fromIndex, char symbol, boolean deleteSymbol, boolean useJdk) {
        if (symbol == '\0') {
            return useJdk ? string.indexOf(pattern, fromIndex) : XTStrUtil.find(string.toString(), fromIndex, pattern);
        } else {
            for (int index; fromIndex < string.length(); ) {
                if (useJdk) {
                    index = string.indexOf(pattern, fromIndex);
                } else {
                    index = XTStrUtil.find(string.toString(), fromIndex, pattern);
                }
                if (index == -1) return index; // not find
                if (index == 0 || (index > 0 && string.charAt(index - 1) != symbol)) { // find
                    return index;
                }
                if (deleteSymbol) {
                    string.deleteCharAt(index - 1);
                    fromIndex = index + pattern.length() - 1;
                } else {
                    fromIndex = index + pattern.length();
                }
            }
            return -1;
        }
    }

    @Override
    public int indexOfNoPreSymbol(@NotNull String pattern, int fromIndex, char symbol, boolean deleteSymbol, boolean useJdk) {
        StringBuilder builder = new StringBuilder(string);
        return indexOfNoPreSymbol(builder, pattern, fromIndex, symbol, deleteSymbol, useJdk);
    }

    @Override
    public String getKeyBetweenPatterns(@NotNull String beginPattern, @NotNull String endPattern, int fromIndex, char symbol, boolean deleteSymbol, boolean useJdk) {
        if (fromIndex < 0) fromIndex = 0;
        int index = indexOfNoPreSymbol(beginPattern, fromIndex, symbol, deleteSymbol, useJdk);
        if (index == -1) return string; // 不存在
        int leftBorder = index + beginPattern.length() - 1;
        int rightBorder = indexOfNoPreSymbol(endPattern, leftBorder + 1, symbol, deleteSymbol, useJdk);
        if (rightBorder == -1) return string;// 不存在
        return string.substring(leftBorder + 1, rightBorder);
    }

    @Override
    public String replaceBetweenPatterns(@NotNull String beginPattern, @NotNull String endPattern, int fromIndex, Function<String, String> existsReplace, char symbol, boolean deleteSymbol, boolean useJdk) {
        if (existsReplace == null) return string;
        if (fromIndex < 0) fromIndex = 0;
        int index = indexOfNoPreSymbol(beginPattern, fromIndex, symbol, deleteSymbol, useJdk);
        if (index == -1) return string; // 不存在
        int leftBorder = index + beginPattern.length() - 1;
        int rightBorder = indexOfNoPreSymbol(endPattern, leftBorder + 1, symbol, deleteSymbol, useJdk);
        if (rightBorder == -1) return string;// 不存在
        String inner = string.substring(leftBorder + 1, rightBorder);
        inner = existsReplace.apply(inner);
        return string.substring(0, index) + inner + string.substring(rightBorder + endPattern.length());
    }

    @Override
    public String replaceAllBetweenPatterns(@NotNull String beginPattern, @NotNull String endPattern, int fromIndex, Function<String, String> existsReplace, char symbol, boolean deleteSymbol, boolean useJdk) {
        if (existsReplace == null) return string;
        if (fromIndex < 0) fromIndex = 0;
        int findIndex = -1;
        StringBuilder sb = new StringBuilder(string);
        for (int sourceIndex = fromIndex; (findIndex = indexOfNoPreSymbol(sb, beginPattern, sourceIndex, symbol, deleteSymbol, useJdk)) != -1; ) {
            int leftBorder = findIndex + beginPattern.length() - 1;
            int rightBorder = indexOfNoPreSymbol(sb, endPattern, leftBorder + 1, symbol, deleteSymbol, useJdk);
            if (rightBorder == -1) return sb.toString();// 不存在
            String inner = sb.substring(leftBorder + 1, rightBorder);
            inner = existsReplace.apply(inner);
            int endIndex = rightBorder + endPattern.length();
            sb.replace(findIndex, endIndex, inner);
            sourceIndex = findIndex + inner.length();
        }
        return sb.toString();
    }


}
