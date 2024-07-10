package top.cutexingluo.tools.utils.se.algo.cpp.string;

import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * String 算法类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/24 17:27
 * @since 1.0.3
 */
public class XTStringAlgo {
    /**
     * 字符串哈希值
     * <p>某个区间的哈希值 </p>
     *
     * @param s the string
     * @param l the left/ the beginning
     * @param r the right/ the end
     * @return 某个区间的哈希值
     */
    public static int hash(String s, int l, int r) {
        int n = s.length();
        int[][] pre = new int[n + 1][2];//前缀哈希值,双重hash
        long base = 131;//取一个质数作基数
        int[] mod = {19260817, 19660813};//分别对两个大质数取模
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < 2; j++) {
                pre[i][j] = (int) ((pre[i - 1][j] * base + s.charAt(i - 1) - 'a' + 1) % mod[j]);
            }
        }
        return pre[r][0] - pre[l - 1][0] * (int) Math.pow(131, r - l + 1);//返回某个区间的哈希值
    }

    //---------------------find------------------------

    // 获取 next 数组

    /**
     * 获取next数组, 获取最长前后缀
     *
     * @since 1.0.2
     */
    public static int[] getNext(String sub) {
        int[] next = new int[sub.length() + 1];
        int i = 0, j = -1;
        next[0] = -1;
        while (i < sub.length()) {
            if (j == -1 || sub.charAt(i) == sub.charAt(j)) {
                next[++i] = ++j;
            } else {
                j = next[j];
            }
        }
        return next;
    }

    private static int checkBounds(String origin, int startIndex, String substring) {
        if (StrUtil.isBlank(origin) || StrUtil.isBlank(substring)) {
            return -2;
        }
        if (startIndex < 0 || startIndex >= origin.length()) {
            throw new ArrayIndexOutOfBoundsException("startIndex must be  greater than  or equals 0 and less than origin string length");
        } else if (startIndex + substring.length() > origin.length()) {
            return -1;
        }
        return 0;
    }

    /**
     * KMP查找字符串, 返回下标
     *
     * @return -1 means not found , -2 means input is empty
     * @since 1.0.2
     */
    public static int find(String origin, int startIndex, String substring) {
        int ret = checkBounds(origin, startIndex, substring);
        if (ret < 0) return ret;
        int i = startIndex, j = 0;
        int[] next = getNext(substring);
        while (i < origin.length() && j < substring.length()) {
            if (j == -1 || origin.charAt(i) == substring.charAt(j)) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }
        if (j == substring.length())
            return i - j;
        else
            return -1;
    }

    /**
     * KMP查找字符串, 返回下标
     *
     * @return -1 means not found , -2 means input is empty
     * @since 1.0.2
     */
    public static int find(String origin, String substring) {
        return find(origin, 0, substring);
    }

    /**
     * KMP逆向查找字符串, 返回下标
     * <p>反向查找，正向匹配</p>
     * <p>包含这个位置及其后面的字符串, 例如 rFind(“ababa” , 1 ,  "ba") = 1</p>
     *
     * @param startIndexToPre 从指定位置往前查找
     * @return -1 means not found , -2 means input is empty
     * @since 1.0.2
     */
    public static int rFind(String origin, int startIndexToPre, String substring) {
        if (StrUtil.isBlank(origin) || StrUtil.isBlank(substring)) {
            return -2;
        }
        if (startIndexToPre < 0 || startIndexToPre >= origin.length()) {
            throw new ArrayIndexOutOfBoundsException("startIndexToPre must be  greater than  or equals 0 and less than origin string length");
        }
        int rpLen = origin.length() - substring.length();  //反转变量
        int startIndex = Math.max(rpLen - startIndexToPre, 0);
        String s = new StringBuilder(origin).reverse().toString();
        String ns = new StringBuilder(substring).reverse().toString();
        int revPos = find(s, startIndex, ns);
        if (revPos == -1) return -1;
        return rpLen - revPos; //反转回来
    }

    /**
     * KMP逆向查找字符串, 返回下标
     * <p>反向查找，正向匹配</p>
     *
     * @return -1 means not found , -2 means input is empty
     * @since 1.0.2
     */
    public static int rFind(String origin, String substring) {
        return rFind(origin, origin.length() - 1, substring);
    }


    private static int findFirstOfCollection(String origin, int startIndex, String characterCollection, boolean isInCollection, boolean ignoreCase) {
        if (ignoreCase) origin = origin.toLowerCase();
        // chars[]数组转为列表
        HashSet<Character> characters = characterCollection
                .chars()
                .mapToObj(i -> {
                    if (ignoreCase) {
                        return Character.toLowerCase((char) i);
                    } else {
                        return (char) i;
                    }
                }).collect(Collectors.toCollection(HashSet::new));
        if (!isInCollection) {
            for (int i = startIndex; i < origin.length(); i++) {
                if (!characters.contains(origin.charAt(i))) {
                    return i;
                }
            }
        } else {
            for (int i = startIndex; i < origin.length(); i++) {
                if (characters.contains(origin.charAt(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 找到字符串第一个出现指定字符的位置
     *
     * @param characterCollection 字符集合组成的字符串
     * @return -1 means not found , -2 means origin is empty
     * @since 1.0.2
     */
    public static int findFirstOf(String origin, int startIndex, String characterCollection, boolean ignoreCase) {
        if (origin == null) return -2;
        if (startIndex < 0 || startIndex >= origin.length()) {
            throw new ArrayIndexOutOfBoundsException("startIndex must be  greater than  or equals 0 and less than origin string length");
        }
        if (StrUtil.isBlank(characterCollection)) {
            return -1;
        }
        return findFirstOfCollection(origin, startIndex, characterCollection, true, ignoreCase);
    }

    /**
     * 找到字符串第一个不出现指定字符的位置
     *
     * @param characterCollection 字符集合组成的字符串
     * @return -1 means not found , -2 means origin is empty
     * @since 1.0.2
     */
    public static int findFirstNotOf(String origin, int startIndex, String characterCollection, boolean ignoreCase) {
        if (origin == null) return -2;
        if (startIndex < 0 || startIndex >= origin.length()) {
            throw new ArrayIndexOutOfBoundsException("startIndex must be  greater than  or equals 0 and less than origin string length");
        }
        if (StrUtil.isBlank(characterCollection)) {
            return 0;
        }
        return findFirstOfCollection(origin, startIndex, characterCollection, false, ignoreCase);
    }

    /**
     * 找到字符串最后一个出现指定字符的位置
     *
     * @param characterCollection 字符集合组成的字符串
     * @param startIndex          从指定位置往前查找
     * @return -1 means not found , -2 means origin is empty
     * @since 1.0.2
     */
    public static int findLastOf(String origin, int startIndex, String characterCollection, boolean ignoreCase) {
        if (origin == null) return -2;
        if (startIndex < 0 || startIndex >= origin.length()) {
            throw new ArrayIndexOutOfBoundsException("startIndex must be  greater than  or equals 0 and less than origin string length");
        }
        if (StrUtil.isBlank(characterCollection)) {
            return -1;
        }
        String s = new StringBuilder(origin).reverse().toString();
        int revPos = findFirstOfCollection(s, startIndex, characterCollection, true, ignoreCase);
        if (revPos == -1) return -1;
        return s.length() - 1 - revPos;
    }

    /**
     * 找到字符串最后一个不出现指定字符的位置
     *
     * @param characterCollection 字符集合组成的字符串
     * @return -1 means not found , -2 means origin is empty
     * @since 1.0.2
     */
    public static int findLastNotOf(String origin, int startIndex, String characterCollection, boolean ignoreCase) {
        if (origin == null) return -2;
        if (startIndex < 0 || startIndex >= origin.length()) {
            throw new ArrayIndexOutOfBoundsException("startIndex must be  greater than  or equals 0 and less than origin string length");
        }
        if (StrUtil.isBlank(characterCollection)) {
            return origin.length() - 1;
        }
        String s = new StringBuilder(origin).reverse().toString();
        int revPos = findFirstOfCollection(s, startIndex, characterCollection, false, ignoreCase);
        if (revPos == -1) return -1;
        return s.length() - 1 - revPos;
    }
    //--------------------------------

    /**
     * 字符串后缀排序后的索引
     *
     * @param s the string
     * @return 索引集合
     */
    //字符串后缀排序后的索引[1,n]
    public static int[] suffixArr(String s) {
        int n = s.length();
        int[][] sort = new int[n][3];
        //第一关键字
        for (int i = 0; i < n; i++) {
            sort[i][0] = s.charAt(i) - '0' + 1;
        }
        for (int k = 1; k < n; k <<= 1) {
            //第二关键字
            for (int i = 0; i < n; i++) {
                sort[i][1] = i + k < n ? sort[i + k][0] : 0;
                sort[i][2] = i;
            }
            //根据第二关键字和第一关键字排序
            for (int i = 1; i >= 0; i--) {
                List<int[]>[] bucket = new ArrayList[n + 1];
                for (int j = 0; j < n; j++) {
                    int num = sort[j][i];
                    if (bucket[num] == null)
                        bucket[num] = new ArrayList<>();
                    bucket[num].add(sort[j]);
                }
                //放回
                for (int j = 0, next = 0; j < n + 1; j++) {
                    if (bucket[j] == null) continue;
                    for (int l = 0; l < bucket[j].size(); l++) {
                        sort[next++] = bucket[j].get(l);
                    }
                }
            }
            //新的排序结果
            int[] nsort = new int[n];
            int rank = 0;
            for (int i = 0; i < n; i++) {
                if (i == 0)
                    nsort[sort[i][2]] = ++rank;
                else if (sort[i][0] == sort[i - 1][0] && sort[i][1] == sort[i - 1][1])
                    nsort[sort[i][2]] = rank;
                else
                    nsort[sort[i][2]] = ++rank;
            }
            if (k << 1 >= n)
                return nsort;
            //作为下一轮的第一关键字
            for (int i = 0; i < n; i++) {
                sort[i][0] = nsort[i];
            }
        }
        return null;
    }

    /**
     * 马拉车 ，回文串
     *
     * @param s the string
     * @return 回文列表，以某位置为中心长度至少为2的最长回文串
     */
    //寻找回文串
    public static List<int[]> manacher(String s) {
        int n = s.length() * 2 + 1;
        char[] cs = new char[n];
        for (int i = 0; i < n; i++) {
            cs[i] = i % 2 == 0 ? '*' : s.charAt((i - 1) / 2);
        }
        int[] d = new int[n];//以原字符串每个位置为中心的最大回文长度,以修改后字符串每个位置为中心的最大回文半径(不计中心)
        int c = 0, r = 0;//右边界最大的子串中心和右边界
        for (int i = 0; i < n; i++) {
            int j = 2 * c - i;//i的关于c的对称位置
            //如果i在r左边,那么在c的回文范围内i附近的回文情况与j相同,此时需要判断j的回文范围是否超出c的回文范围,取较小值
            if (r > i) d[i] = Math.min(r - i, d[j]);
            else d[i] = 0;
            //确定i的至少回文长度后向两边扩展继续判断
            while (i - d[i] - 1 >= 0 && i + d[i] + 1 < n && cs[i + d[i] + 1] == cs[i - d[i] - 1])
                d[i]++;
            //更新c,r
            if (d[i] + i > r) {
                r = d[i] + i;
                c = i;
            }
        }
        List<int[]> ans = new ArrayList<>();//以某位置为中心长度至少为2的最长回文串
        for (int i = 0; i < n; i++) {
            if (d[i] < 2) continue;
            ans.add(new int[]{(i - d[i]) / 2, (i + d[i] - 2) / 2});
        }
        return ans;
    }
}
