package top.cutexingluo.tools.common.data.ext;


import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.data.PairEntry;
import top.cutexingluo.tools.common.data.Tuple;

import java.util.Comparator;
import java.util.Map;

/**
 * Pair 二元组实体类 (可比较) (使用比较器)
 *
 * <p>键值对对象，可以使用 set 方法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/22 9:44
 * @see top.cutexingluo.tools.common.data.TuplePair TuplePair - 可比较可set 二元组
 * @since 1.1.6
 */
public class ComparableTuplePair<K, V> extends ComparablePair<K, V> implements Tuple<K, V> {

    /**
     * 构造
     *
     * @param key   键
     * @param value 值
     */
    public ComparableTuplePair(K key, V value) {
        super(key, value);
    }

    public ComparableTuplePair(@NotNull Map.Entry<K, V> entry) {
        super(entry);
    }

    public ComparableTuplePair(@NotNull PairEntry<K, V> entry) {
        super(entry);
    }

    /**
     * 构造
     *
     * @param key   键
     * @param value 值
     */
    public ComparableTuplePair(K key, V value, Comparator<? super ComparablePair<K, V>> comparator) {
        super(key, value, comparator);
    }


    @Override
    public K setKey(K key) {
        final K oldKey = this.key;
        this.key = key;
        return oldKey;
    }

    @Override
    public V setValue(V value) {
        final V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    @Override
    public Map.Entry<K, V> toMapEntry() {
        return Tuple.super.toMapEntry();
    }


}
