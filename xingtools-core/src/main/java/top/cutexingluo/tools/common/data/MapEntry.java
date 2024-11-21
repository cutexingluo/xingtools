package top.cutexingluo.tools.common.data;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Pair 二元组实体类
 *
 * <p>键值对对象，可以使用 setValue 方法</p>
 * <p>Map.Entry 基础实现类</p>
 * <p>详见 {@link AbstractMap.SimpleEntry}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/13 17:32
 * @see Entry Entry - 不可比较不可set 二元组
 * @see Pair  Pair  - 可比较不可set 二元组
 * @see TupleEntry  TupleEntry  - 不可比较可set 二元组
 * @see TuplePair TuplePair - 可比较可set 二元组
 * @see MapEntry MapEntry - 不可比较可setValue 二元组 (Map.Entry 基础实现类)
 * @since 1.1.6
 */
public class MapEntry<K, V> extends Entry<K, V> implements Map.Entry<K, V> {


    public MapEntry(K key, V value) {
        super(key, value);
    }

    public MapEntry(@NotNull Map.Entry<K, V> entry) {
        super(entry);
    }

    public MapEntry(@NotNull PairEntry<K, V> entry) {
        super(entry);
    }

    @Override
    public V setValue(V value) {
        final V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    /**
     * @see AbstractMap.SimpleEntry
     */
    @Override
    public Map.Entry<K, V> toMapEntry() {
        return new Map.Entry<K, V>() {
            @Override
            public K getKey() {
                return MapEntry.this.getKey();
            }

            @Override
            public V getValue() {
                return MapEntry.this.getValue();
            }

            @Override
            public V setValue(V value) {
                return MapEntry.this.setValue(value);
            }
        };
    }
}
