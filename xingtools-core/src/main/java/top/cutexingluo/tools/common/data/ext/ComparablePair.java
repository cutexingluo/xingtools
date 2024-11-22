package top.cutexingluo.tools.common.data.ext;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.data.Entry;
import top.cutexingluo.tools.common.data.PairEntry;

import java.util.Comparator;
import java.util.Map;

/**
 * Pair 二元组 (可比较) (使用比较器)
 *
 * <p>键值对对象，只能在构造时传入键值</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/22 9:44
 * @see top.cutexingluo.tools.common.data.Pair  Pair  - 可比较不可set 二元组
 * @since 1.1.6
 */
public class ComparablePair<K, V> extends Entry<K, V> implements Comparable<ComparablePair<K, V>> {

    /**
     * 比较器
     *
     * <p>禁止赋值 XTComparator </p>
     */
    protected Comparator<? super ComparablePair<K, V>> comparator;

    /**
     * 构造
     *
     * @param key   键
     * @param value 值
     */
    public ComparablePair(K key, V value) {
        super(key, value);
    }

    public ComparablePair(@NotNull Map.Entry<K, V> entry) {
        super(entry.getKey(), entry.getValue());
    }

    public ComparablePair(@NotNull PairEntry<K, V> entry) {
        super(entry.getKey(), entry.getValue());
    }

    /**
     * 构造
     *
     * @param key        键
     * @param value      值
     * @param comparator 比较器
     */
    public ComparablePair(K key, V value, Comparator<? super ComparablePair<K, V>> comparator) {
        super(key, value);
        this.comparator = comparator;
    }


    @Override
    public int compareTo(@NotNull ComparablePair<K, V> o) {
        return comparator.compare(this, o);
    }

    public Comparator<? super ComparablePair<K, V>> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<? super ComparablePair<K, V>> comparator) {
        this.comparator = comparator;
    }
}
