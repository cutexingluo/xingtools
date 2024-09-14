package top.cutexingluo.tools.utils.se.character;

/**
 * 字符串工具类
 *
 * @author XingTian
 * @version 1.0.1
 * @date 2023/2/2 21:46
 */
public class XTCharUtil {

    /**
     * 目标位置字符大写
     *
     * @param str   字符串
     * @param index 位置
     */
    public static String upperCaseAtOne(String str, int index) {
        if (index < 0 || index >= str.length()) return str;
        char[] ch = str.toCharArray();
        if (ch[index] >= 'a' && ch[index] <= 'z') {
            ch[index] = (char) (ch[index] - 32);
        }
        return new String(ch);
    }

    /**
     * 字符串添加前缀
     * <p>检验是否存在相同前缀，无则添加并且自动转成驼峰命名</p>
     *
     * @param s      目标字符串
     * @param com    前缀
     * @param isDone 是否完成，true 则不添加（循环里面使用）
     */
    public static String addCheckPrefix(String s, String com, boolean isDone) {
        if (isDone) return s;
        int comLen = com.length();
        if (s.length() > comLen) {
            String str = s.substring(0, comLen);
            if (!str.equals(com)) {
                s = upperCaseAtOne(s, 0);
                s = com + s;
            }
        } else { // 小于该大小的一律加com字段前缀 例如set或get
            s = upperCaseAtOne(s, 0);
            s = com + s;
        }
        return s;
    }

    /**
     * 字符串添加后缀
     * <p>检验是否存在相同后缀，无则添加并且自动转成驼峰命名</p>
     *
     * @param s      目标字符串
     * @param com    后缀
     * @param isDone 是否完成，true 则不添加（循环里面使用）
     * @since 1.1.4
     */
    public static String addCheckSuffix(String s, String com, boolean isDone) {
        if (isDone) return s;
        int comLen = com.length();
        if (s.length() > comLen) {
            String str = s.substring(s.length() - comLen);
            if (!str.equals(com)) {
                com = upperCaseAtOne(com, 0);
                s += com;
            }
        } else { // 小于该大小的一律加com字段后缀
            com = upperCaseAtOne(com, 0);
            s += com;
        }
        return s;
    }
}
