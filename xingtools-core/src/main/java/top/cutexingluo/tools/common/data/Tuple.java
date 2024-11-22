package top.cutexingluo.tools.common.data;

import java.util.AbstractMap;
import java.util.Map;

/**
 * 二元组接口
 * <p>继承 {@link Map.Entry} 和 {@link PairEntry} </p>
 * <p>A map entry (key-value pair).  like Map.Entry</p>
 * <p>拥有 set 和 get 方法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/23 17:50
 * @since 1.1.2
 */
public interface Tuple<K, V> extends Map.Entry<K, V>, PairEntry<K, V> {


    /**
     * @return the key corresponding to this entry
     */
    K getKey();

    /**
     * @return the value corresponding to this entry
     */
    V getValue();


    /**
     * @param key new key to be stored in this entry
     * @return {@link K } old key corresponding to the entry
     */
    K setKey(K key);

    /**
     * @param value new value to be stored in this entry
     * @return {@link V } old value corresponding to the entry
     */
    V setValue(V value);

    /**
     * modify the value corresponding to this main entry
     *
     * @see AbstractMap.SimpleEntry
     * @since 1.1.4
     */
    @Override
    default Map.Entry<K, V> toMapEntry() {
        return new Map.Entry<K, V>() {
            @Override
            public K getKey() {
                return Tuple.this.getKey();
            }

            @Override
            public V getValue() {
                return Tuple.this.getValue();
            }

            @Override
            public V setValue(V value) {
                return Tuple.this.setValue(value);
            }
        };
    }
}
