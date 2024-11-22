package top.cutexingluo.tools.utils.se.core.compare;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

/**
 * Comparator 比较器 工具
 * <p> 可以比较 null  也可比较非 Comparable 实现类</p>
 * <p><b>注意禁止在 Comparable 实现类的 compareTo 里面调用 Comparable 相关的方法如 compare 方法, 防止循环调用</b></p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 22:32
 */
public class XTComparator<T> implements Comparator<T> {

    /**
     * 均不为 null
     *
     * @since 1.0.5
     */
    public static final int BOTH_NOT_NULL = 2;

    /**
     * 比较器默认矫正函数
     * <p>由于存在 null , 值为2 ,所以提供 compare 矫正函数</p>
     *
     * @since 1.1.6
     */
    public static final Function<Integer, Integer> DEFAULT_CORRECT_FILTER = (ret) -> ret == 0 ? ret : (ret > 0 ? 1 : -1);

    /**
     * 是否正序
     */
    private boolean isAsc = true;

    /**
     * 比较时 null 的位置
     *
     * <p>true ->compare(o1,o2) -> 单 o1 为null 时 返回1 , 单 o2 时 为 null 时 返回-1</p>
     * <p>false ->compare(o1,o2) -> 单 o1 为null 时 返回-1 , 单 o2 时 为 null 时 返回1</p>
     * <p>如果是在 sort 里面 , true -> null 值排最后, false -> null 排前面</p>
     *
     * @since 1.1.6
     */
    private boolean nullOnePos = true;

    /**
     * 比较器矫正函数
     * <p>由于存在 null , 值为2 ,所以提供 compare 矫正函数</p>
     */
    private Function<Integer, Integer> correctFilter;

    public XTComparator(boolean isAsc) {
        this.isAsc = isAsc;
        this.correctFilter = DEFAULT_CORRECT_FILTER;
    }

    public XTComparator(boolean isAsc, boolean nullOnePos) {
        this.isAsc = isAsc;
        this.nullOnePos = nullOnePos;
        this.correctFilter = DEFAULT_CORRECT_FILTER;
    }

    public XTComparator(boolean isAsc, boolean nullOnePos, Function<Integer, Integer> correctFunction) {
        this.isAsc = isAsc;
        this.nullOnePos = nullOnePos;
        this.correctFilter = correctFunction;
    }

    public XTComparator<T> ascOrder(boolean isAsc) {
        this.isAsc = isAsc;
        return this;
    }

    public XTComparator<T> nullOnePos(boolean nullOnePos) {
        this.nullOnePos = nullOnePos;
        return this;
    }

    public Comparator<T> correctFunction(Function<Integer, Integer> correctFilter) {
        this.correctFilter = correctFilter;
        return this;
    }


    /**
     * 通过比较，返回 Comparator 对象
     * <p>使用 该方法建议Key Value 类型统一为当前类泛型 ，可以直接统一为 Object</p>
     * <p>接收参数 泛型必须为实现 Map.Entry 的 Comparator对象</p>
     *
     * @param byValue 是否按值比较
     * @return {@link Comparator}<{@link O}>
     */
    public <O extends Map.Entry<K, V>, K, V> Comparator<O> compareBy(boolean byValue) {
        if (byValue) {
            return (p1, p2) -> this.compare((T) p1.getValue(), (T) p2.getValue());
        } else {
            return (p1, p2) -> this.compare((T) p1.getKey(), (T) p2.getKey());
        }
    }

    /**
     * 比较空值
     * <p>仅做空值比较</p>
     *
     * @return 返回0则都为null，若返回2则都不为空,
     * <p>nullPosLast-> true ,返回1则单o1为null，返回-1则单o2为null</p>
     */
    public static <T> int tryCompareNull(@Nullable T o1, @Nullable T o2) {
        return tryCompareNull(o1, o2, true);
    }

    /**
     * 比较空值
     * <p>仅做空值比较</p>
     *
     * @param nullOnePos 空时1的位置, true -> 返回1则o1空, 返回-1则o2空, false -> 反之
     * @return 返回0则都为null，若返回2则都不为空,
     * <p>nullPosLast-> true ,返回1则单o1为null，返回-1则单o2为null</p>
     * <p>nullPosLast-> false ,返回-1则单o1为null，返回1则单o2为null</p>
     * <p>如果是在 sort 里面 , true -> null 值排最后, false -> null 排前面</p>
     */
    public static <T> int tryCompareNull(@Nullable T o1, @Nullable T o2, boolean nullOnePos) {
        int retPos = nullOnePos ? 1 : -1;
        if (o1 == null) {
            if (o2 == null) {
                return 0;
            } else {
                return retPos;
            }
        } else {
            if (o2 == null) {
                return -retPos;
            } else {
                return BOTH_NOT_NULL;
            }
        }
        // nullOnePos -> true 示例如下
//        if (o1 == null && o2 == null) {
//            return 0;
//        } else if (o1 == null) {
//            return 1;
//        } else if (o2 == null) {
//            return -1;
//        } else {
//            return BOTH_NOT_NULL; // 1.0.5
//        }
    }

    //                infoIds.sort(((o1, o2) -> asc ?
//                        ((String) o1.getValue()).compareTo((String) o2.getValue()) :
//                        ((String) o2.getValue()).compareTo((String) o1.getValue())));
//                infoIds.sort(Comparator.comparing(o -> ((String) o.getValue())));
    //                infoIds.sort(Comparator.comparing(o -> ((Comparable) o.getValue())));

    /**
     * 排序主方法，可以为空
     */
    @Override
    public int compare(@Nullable T o1, @Nullable T o2) {
        // 先比较 null
        int ret = tryCompareNull(o1, o2, nullOnePos);
        if (ret != BOTH_NOT_NULL) return ret;
        // now ret = 2, both nonNull , 再 比较 Compare
        if (o1 instanceof Comparable) {
            ret = ((Comparable) o1).compareTo(o2);
        } else {
            //  后比较 toString (class@hashcode)
            ret = o1.toString().compareTo(o2.toString());
        }
        // 最后进行矫正
        ret = correctFilter.apply(ret);
        return isAsc ? ret : -ret;
    }


    /**
     * 比较 String
     */
    public int compareStringCheck(@NotNull String o1, @NotNull String o2) {
        int ret = o1.compareTo(o2);
        return isAsc ? ret : -ret;
    }

    /**
     * 比较 toString()
     */
    public int compareToStringCheck(@NotNull T o1, @NotNull T o2) {
        int ret = o1.toString().compareTo(o2.toString());
        return isAsc ? ret : -ret;
    }

    /**
     * 比较 Comparable
     */
    public int compareComparableCheck(@NotNull Comparable<T> o1, @NotNull T o2) {
        int ret = o1.compareTo(o2);
        return isAsc ? ret : -ret;
    }

    /**
     * 比较 hashCode
     */
    public int compareHashCodeCheck(@NotNull T o1, @NotNull T o2) {
        int ret = o1.hashCode() - o2.hashCode();
        return isAsc ? ret : -ret;
    }

    /**
     * 比较 String
     */
    public int compareString(@Nullable String o1, @Nullable String o2) {
        int ret = tryCompareNull(o1, o2, nullOnePos);
        if (ret != BOTH_NOT_NULL) return ret;
        return compareStringCheck(o1, o2);
    }


    /**
     * 先比较 null , 再比较是否是 Comparable 实现类
     *
     * @since 1.1.6
     */
    public int compareComparableBefore(@Nullable T o1, @Nullable T o2) {
        int ret = tryCompareNull(o1, o2, nullOnePos);
        if (ret != BOTH_NOT_NULL) return ret;
        if (o1 instanceof Comparable) {
            ret = ((Comparable) o1).compareTo(o2);
        }
        return isAsc ? ret : -ret;
    }

    /**
     * 先比较是否是 Comparable 实现类 再比较 null
     *
     * <p>不符合 Comparable 接口定义，不建议使用</p>
     */
    public int compareComparableAfter(@Nullable T o1, @Nullable T o2) {
        int ret;
        if (o1 instanceof Comparable) {
            ret = ((Comparable) o1).compareTo(o2);
        } else if (o2 instanceof Comparable) {
            // reversed
            ret = -((Comparable) o2).compareTo(o1);
        } else {
            ret = tryCompareNull(o1, o2, nullOnePos);
            if (ret != BOTH_NOT_NULL) return ret;
        }
        return isAsc ? ret : -ret;
    }

    /**
     * 比较 Comparable
     */
    public int compareComparable(@Nullable Comparable<T> o1, @Nullable T o2) {
        int ret = tryCompareNull(o1, o2, nullOnePos);
        if (ret != BOTH_NOT_NULL) return ret;
        return compareComparableCheck(o1, o2);
    }

    /**
     * 比较 toString()
     */
    public int compareToString(@Nullable T o1, @Nullable T o2) {
        int ret = tryCompareNull(o1, o2, nullOnePos);
        if (ret != BOTH_NOT_NULL) return ret;
        return compareToStringCheck(o1, o2);
    }

    /**
     * 比较 hashCode
     */
    public int compareHashCode(@Nullable T o1, @Nullable T o2) {
        int ret = tryCompareNull(o1, o2, nullOnePos);
        if (ret != BOTH_NOT_NULL) return ret;
        return compareHashCodeCheck(o1, o2);
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }


}
