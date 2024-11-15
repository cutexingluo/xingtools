package top.cutexingluo.tools.common.data;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.se.core.compare.XTComparator;

import java.util.Map;

/**
 * Pair 二元组 (可比较)
 *
 * <p>键值对对象，只能在构造时传入键值</p>
 * <p>于1.1.4 改为继承 Entry </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 21:45
 * @see Entry Entry - 不可比较不可set 二元组
 * @see Pair  Pair  - 可比较不可set 二元组
 * @see TupleEntry  TupleEntry  - 不可比较可set 二元组
 * @see TuplePair TuplePair - 可比较可set 二元组
 * @see MapEntry MapEntry - 不可比较可setValue 二元组 (Map.Entry 基础实现类)
 * @since 1.0.3
 */
public class Pair<K extends Comparable<K>, V extends Comparable<V>> extends Entry<K, V> implements Comparable<Pair<K, V>> {
    /**
     * 构造
     *
     * @param key   键
     * @param value 值
     */
    public Pair(K key, V value) {
        super(key, value);
    }

    public Pair(@NotNull Map.Entry<K, V> entry) {
        super(entry.getKey(), entry.getValue());
    }

    /**
     * @since 1.1.4
     */
    public Pair(@NotNull PairEntry<K, V> entry) {
        super(entry.getKey(), entry.getValue());
    }

    @Override
    public int compareTo(@NotNull Pair<K, V> o) {
        int ret = XTComparator.tryCompareNull(this, o);
        if (ret == XTComparator.BOTH_NOT_NULL) { //1.0.5
            int retKey = key.compareTo(o.key);
            if (retKey == 0) {
                return value.compareTo(o.value);
            }
            return retKey;
        }
        return ret;
    }

}
