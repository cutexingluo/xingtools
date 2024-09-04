package top.cutexingluo.tools.common.data;

import java.util.Map;

/**
 * 二元组方法接口
 * <p>A map entry (key-value pair).  like Map.Entry</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/7 13:21
 * @since 1.0.5
 */
public interface PairEntry<K, V> {

    /**
     * @return the key corresponding to this entry
     */
    K getKey();

    /**
     * @return the value corresponding to this entry
     */
    V getValue();

    /**
     * @return the map entry
     * @since 1.1.4
     */
    default Map.Entry<K, V> toMapEntry() {
        return new Map.Entry<K, V>() {
            @Override
            public K getKey() {
                return PairEntry.this.getKey();
            }

            @Override
            public V getValue() {
                return PairEntry.this.getValue();
            }

            @Override
            public V setValue(V value) {
                throw new UnsupportedOperationException();
            }
        };
    }
}
