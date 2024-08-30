package top.cutexingluo.tools.utils.se.character.symbol;

import lombok.Getter;
import top.cutexingluo.tools.common.base.IResultData;
import top.cutexingluo.tools.common.data.PairEntry;

/**
 * 符号对枚举类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/20 16:23
 * @since 1.1.4
 */
@Getter
public enum SymbolPairEnum implements IResultData<String>, PairEntry<String, String> {

    /**
     * 小括号 ()
     */
    PARENTHESES("parentheses", "()", new Character[]{'(', ')'}, "小括号",
            "\\((.+?)\\)", "(", ")"),

    /**
     * 中括号 []
     */
    BRACKETS("brackets", "[]", new Character[]{'[', ']'}, "中括号",
            "\\[(.+?)\\]", "[", "]"),

    /**
     * 大括号 {}
     */
    BRACES("braces", "{}", new Character[]{'{', '}'}, "大括号",
            "\\{(.+?)\\}", "{", "}"),

    /**
     * 插值符号 ${}
     */
    INTERPOLATION_BRACES("interpolation braces", "${}", new Character[]{'$', '{', '}'}, "插值符号",
            "\\$\\{(.+?)\\}", "${", "}");


    /**
     * 符号名称
     */
    final String name;
    /**
     * 符号
     */
    final String symbols;
    /**
     * 符号字符
     */
    final Character[] charSymbol;
    /**
     * 符号中文名称
     */
    final String cnName;
    /**
     * 正则表达式
     */
    final String regex;
    /**
     * 前驱符号
     */
    final String prefix;
    /**
     * 后驱符号
     */
    final String suffix;


    SymbolPairEnum(String name, String symbols, Character[] charSymbol, String cnName, String regex, String prefix, String suffix) {
        this.name = name;
        this.symbols = symbols;
        this.charSymbol = charSymbol;
        this.cnName = cnName;
        this.regex = regex;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * 默认符号长度向上取整 的长度作为前缀，剩下作为后缀
     */
    SymbolPairEnum(String name, String symbols, Character[] charSymbol, String cnName, String regex) {
        this.name = name;
        this.symbols = symbols;
        this.charSymbol = charSymbol;
        this.cnName = cnName;
        this.regex = regex;
        int ptr = (symbols.length() + 1) / 2;
        this.prefix = symbols.substring(0, ptr);
        this.suffix = symbols.substring(ptr);
    }

    /**
     * 返回名称
     */
    @Override
    public String getMsg() {
        return name;
    }

    /**
     * 返回符号
     */
    @Override
    public String getCode() {
        return symbols;
    }


    /**
     * 返回前缀
     */
    @Override
    public String getKey() {
        return prefix;
    }

    /**
     * 返回后缀
     */
    @Override
    public String getValue() {
        return suffix;
    }
}
