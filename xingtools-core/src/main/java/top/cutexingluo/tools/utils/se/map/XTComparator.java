package top.cutexingluo.tools.utils.se.map;


import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;

/**
 * Comparator 比较器 工具
 * <p> 可以比较 null </p>
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
     * 是否正序
     */
    private boolean isAsc = true;

    public XTComparator(boolean isAsc) {
        this.isAsc = isAsc;
    }

    public XTComparator<T> ascOrder(boolean isAsc) {
        this.isAsc = isAsc;
        return this;
    }

    /**
     * 通过比较，返回 Comparator 对象
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
     * 比较空值，空的对象排在后面
     *
     * @return 返回0则都为null，返回1则o1为null，返回-1则o2为null，若返回2则都不为空
     */
    public static <T> int tryCompareNull(@Nullable T o1, @Nullable T o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return 1;
        } else if (o2 == null) {
            return -1;
        } else {
            return BOTH_NOT_NULL; // 1.0.5
        }
    }

    //                infoIds.sort(((o1, o2) -> asc ?
//                        ((String) o1.getValue()).compareTo((String) o2.getValue()) :
//                        ((String) o2.getValue()).compareTo((String) o1.getValue())));
//                infoIds.sort(Comparator.comparing(o -> ((String) o.getValue())));
    //                infoIds.sort(Comparator.comparing(o -> ((Comparable) o.getValue())));

    /**
     * 排序主方法，可以为空，空的对象排在后面
     */
    @Override
    public int compare(@Nullable T o1, @Nullable T o2) {
        if (o1 instanceof String) {
            return compareString((String) o1, (String) o2);
        } else if (o1 instanceof Comparable) {
            return compareComparable((Comparable) o1, (Comparable) o2);
        } else {
            return compareEnd(o1, o2);
        }
    }

    public int compareString(@Nullable String o1, @Nullable String o2) {
        int ret = tryCompareNull(o1, o2);
        if (ret != BOTH_NOT_NULL) return ret;
        ret = o1.compareTo(o2);
        return isAsc ? ret : -ret;
    }

    public int compareComparable(@Nullable Comparable<T> o1, @Nullable Comparable<T> o2) {
        int ret = tryCompareNull(o1, o2);
        if (ret != BOTH_NOT_NULL) return ret;
        ret = o1.compareTo((T) o2);
        return isAsc ? ret : -ret;
    }

    public int compareEnd(@Nullable T o1, @Nullable T o2) {
        int ret = tryCompareNull(o1, o2);
        if (ret != BOTH_NOT_NULL) return ret;
        ret = o1.toString().compareTo(o2.toString());
        return isAsc ? ret : -ret;
    }


    @Override
    public boolean equals(Object obj) {
        return false;
    }


}
