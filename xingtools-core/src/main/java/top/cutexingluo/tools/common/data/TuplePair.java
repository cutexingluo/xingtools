package top.cutexingluo.tools.common.data;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Pair 二元组实体类 (可比较)
 *
 * <p>键值对对象，可以使用 set 方法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/4 14:28
 * @see Entry Entry - 不可比较不可set 二元组
 * @see Pair  Pair  - 可比较不可set 二元组
 * @see TupleEntry  TupleEntry  - 不可比较可set 二元组
 * @see TuplePair TuplePair - 可比较可set 二元组
 * @see MapEntry MapEntry - 不可比较可setValue 二元组 (Map.Entry 基础实现类)
 * @since 1.1.4
 */
public class TuplePair<K extends Comparable<K>, V extends Comparable<V>> extends Pair<K, V> implements Tuple<K, V> {

    /**
     * 构造
     *
     * @param key   键
     * @param value 值
     */
    public TuplePair(K key, V value) {
        super(key, value);
    }

    public TuplePair(@NotNull Map.Entry<K, V> entry) {
        super(entry);
    }

    public TuplePair(@NotNull PairEntry<K, V> entry) {
        super(entry);
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
