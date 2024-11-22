package top.cutexingluo.tools.common.data;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * 二元组
 *
 * <p>键值对对象，只能在构造时传入键值</p>
 *
 * <p>由于过于依赖 hutool 的 Pair 类, 故单独创建一个类进行移植</p>
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/4 14:47
 * @see cn.hutool.core.lang.Pair hutool 的 Pair
 * @see Entry Entry - 不可比较不可set 二元组
 * @see top.cutexingluo.tools.common.data.Pair  Pair  - 可比较不可set 二元组
 * @see TupleEntry  TupleEntry  - 不可比较可set 二元组
 * @see TuplePair TuplePair - 可比较可set 二元组
 * @see MapEntry MapEntry - 不可比较可setValue 二元组 (Map.Entry 基础实现类)
 * @since 1.1.4
 */
public class Entry<K, V> implements PairEntry<K, V>, Serializable {
    private static final long serialVersionUID = 1L;

    protected K key;
    protected V value;

    /**
     * 构建{@code Pair}对象
     *
     * @param <K>   键类型
     * @param <V>   值类型
     * @param key   键
     * @param value 值
     */
    public static <K, V> Entry<K, V> of(K key, V value) {
        return new Entry<>(key, value);
    }

    /**
     * 构造
     *
     * @param key   键
     * @param value 值
     */
    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Entry(@NotNull Map.Entry<K, V> entry) {
        this(entry.getKey(), entry.getValue());
    }


    public Entry(@NotNull PairEntry<K, V> entry) {
        this(entry.getKey(), entry.getValue());
    }

    /**
     * 获取键
     *
     * @return 键
     */
    public K getKey() {
        return this.key;
    }

    /**
     * 获取值
     *
     * @return 值
     */
    public V getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "Entry [key=" + key + ", value=" + value + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof PairEntry) {
            PairEntry<?, ?> pair = (PairEntry<?, ?>) o;
            return Objects.equals(getKey(), pair.getKey()) &&
                    Objects.equals(getValue(), pair.getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        //copy from 1.8 HashMap.Node
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }
}
