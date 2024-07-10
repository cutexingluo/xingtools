package top.cutexingluo.tools.utils.se.string;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.se.character.XTStrUtil;

/**
 * C++ 标准库 的 string 方法 操作字符串
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/20 19:20
 * @since 1.0.2
 */
public class CPPString implements BaseCPPString, Comparable<String>, CharSequence {
    protected String string;


    public CPPString() {
        this("", false);
    }

    /**
     * 默认不检测 null 值
     *
     * @param string 字符串
     */
    public CPPString(String string) {
        this(string, false);
    }

    /**
     * @param string       字符串
     * @param autoFillNull 是否自动将空字符串 null 赋值为 ""
     */
    public CPPString(String string, boolean autoFillNull) {
        if (string == null) {
            if (autoFillNull) string = "";
            else throw new NullPointerException("string is null");
        }
        this.string = string;
    }

    /**
     * @param string           字符串
     * @param nullDefaultValue 当 string 为 null 时，默认赋值
     */
    public CPPString(String string, String nullDefaultValue) {
        if (string == null) {
            string = nullDefaultValue;
        }
        this.string = string;
    }

    public CPPString(@NotNull BaseString baseString) {
        this.string = baseString.toString();
    }

    @Override
    public String getString() {
        return string;
    }

    @Override
    public char charAt(int index) {
        return string.charAt(index);
    }

    @NotNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return string.subSequence(start, end);
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public int getSize() {
        return string.length();
    }

    @Override
    public int length() {
        return string.length();
    }


    @Override
    public String substr(int begIndex, int len) {
        return string.substring(begIndex, begIndex + len);
    }

    @Override
    public String substr(int begIndex) {
        return string.substring(begIndex);
    }

    @Override
    public void swap(@NotNull BaseCPPString aString) {
        String newString = aString.getString();
        String origin = string;
        string = newString;
        aString.assign(origin);
    }

    @Override
    public BaseCPPString assign(String str) {
        this.string = str;
        return this;
    }

    @Override
    public BaseCPPString assign(@NotNull String str, int begIndex, int len) {
        this.string = str.substring(begIndex, begIndex + len);
        return this;
    }

    @Override
    public BaseCPPString assign(int count, char ch) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        this.string = sb.toString();
        return this;
    }

    @Override
    public BaseCPPString assign(int count, String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        this.string = sb.toString();
        return this;
    }

    @Override
    public BaseCPPString append(String str) {
        this.string += str;
        return this;
    }

    @Override
    public BaseCPPString append(@NotNull String str, int begIndex, int len) {
        this.string += str.substring(begIndex, begIndex + len);
        return this;
    }

    @Override
    public BaseCPPString append(int count, char ch) {
        StringBuilder sb = new StringBuilder(this.string);
        for (int i = 0; i < count; i++) {
            sb.append(ch);
        }
        this.string = sb.toString();
        return this;
    }

    @Override
    public BaseCPPString append(int count, String str) {
        StringBuilder sb = new StringBuilder(this.string);
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        this.string = sb.toString();
        return this;
    }

    @Override
    public int compare(int beginIndex, int len, @NotNull String str, int strBegIndex, int strLen) {
        String substring = string.substring(beginIndex, beginIndex + len);
        String target = str.substring(strBegIndex, strBegIndex + strLen);
        return substring.compareTo(target);
    }

    @Override
    public int compare(int beginIndex, int len, @NotNull String str, int strBegIndex) {
        String substring = string.substring(beginIndex, beginIndex + len);
        String target = str.substring(strBegIndex, strBegIndex);
        return substring.compareTo(target);
    }

    @Override
    public int compare(int beginIndex, int len, String str) {
        return string.substring(beginIndex, beginIndex + len).compareTo(str);
    }


    @Override
    public int compare(String str) {
        return string.compareTo(str);
    }

    @Override
    public int compareTo(@NotNull String o) {
        return compare(o);
    }


    @Override
    public int find(String subString, int startIndex) {
        return XTStrUtil.find(this.string, startIndex, subString);
    }

    @Override
    public int find(String subString) {
        return find(subString, 0);
    }

    @Override
    public int rFind(String subString, int startIndexToPre) {
        return XTStrUtil.rFind(this.string, startIndexToPre, subString);
    }

    @Override
    public int rFind(String subString) {
        return rFind(subString, length() - 1);
    }

    @Override
    public int findFirstOf(String charCollection, int startIndex) {
        return XTStrUtil.findFirstOf(this.string, startIndex, charCollection, false);
    }

    @Override
    public int findFirstOf(String charCollection) {
        return findFirstOf(charCollection, 0);
    }

    @Override
    public int findLastOf(String charCollection, int startIndex) {
        return XTStrUtil.findLastOf(this.string, startIndex, charCollection, false);
    }

    @Override
    public int findLastOf(String charCollection) {
        return findLastOf(charCollection, 0);
    }

    @Override
    public int findFirstNotOf(String charCollection, int startIndex) {
        return XTStrUtil.findFirstNotOf(this.string, startIndex, charCollection, false);
    }

    @Override
    public int findFirstNotOf(String charCollection) {
        return findFirstNotOf(charCollection, 0);
    }

    @Override
    public int findLastNotOf(String charCollection, int startIndex) {
        return XTStrUtil.findLastNotOf(this.string, startIndex, charCollection, false);
    }

    @Override
    public int findLastNotOf(String charCollection) {
        return findLastNotOf(charCollection, 0);
    }


    @Override
    public BaseCPPString replace(int beginIndex, int len, @NotNull String str, int strBegIndex, int strLen) {
        StringBuilder builder = new StringBuilder(this.string);
        this.string = builder.replace(beginIndex, beginIndex + len,
                str.substring(strBegIndex, strBegIndex + strLen)).toString();
        return this;
    }

    @Override
    public BaseCPPString replace(int beginIndex, int len, @NotNull String str, int strBegIndex) {
        StringBuilder builder = new StringBuilder(this.string);
        this.string = builder.replace(beginIndex, beginIndex + len,
                str.substring(strBegIndex)).toString();
        return this;
    }

    @Override
    public BaseCPPString replace(int beginIndex, int len, String str) {
        StringBuilder builder = new StringBuilder(this.string);
        this.string = builder.replace(beginIndex, beginIndex + len,
                str).toString();
        return this;
    }

    @Override
    public BaseCPPString replace(int beginIndex, int len, int count, String str) {
        StringBuilder builder = new StringBuilder(this.string);
        String s = new CPPString().assign(count, str).toString();
        this.string = builder.replace(beginIndex, beginIndex + len,
                s).toString();
        return this;
    }

    @Override
    public BaseCPPString erase(int beginIndex, int len) {
        StringBuilder builder = new StringBuilder(this.string)
                .delete(beginIndex, beginIndex + len);
        this.string = builder.toString();
        return this;
    }

    @Override
    public BaseCPPString erase(int beginIndex) {
        StringBuilder builder = new StringBuilder(this.string)
                .delete(beginIndex, this.string.length());
        this.string = builder.toString();
        return this;
    }

    @Override
    public BaseCPPString eraseCharAt(int index) {
        StringBuilder builder = new StringBuilder(this.string)
                .deleteCharAt(index);
        this.string = builder.toString();
        return this;
    }

    @Override
    public BaseCPPString insert(int beginIndex, String str) {
        StringBuilder builder = new StringBuilder(this.string)
                .insert(beginIndex, str);
        this.string = builder.toString();
        return this;
    }

    @Override
    public BaseCPPString insert(int beginIndex, int count, String str) {
        String s = new CPPString().assign(count, str).toString();
        StringBuilder builder = new StringBuilder(this.string)
                .insert(beginIndex, s);
        this.string = builder.toString();
        return this;
    }

    @Override
    public BaseCPPString clear() {
        this.assign("");
        return this;
    }

}
