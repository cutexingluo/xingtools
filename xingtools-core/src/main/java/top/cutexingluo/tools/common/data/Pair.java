package top.cutexingluo.tools.common.data;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.se.map.XTComparator;

import java.util.Map;

/**
 * Pair 二元组
 * <p>键值对对象，只能在构造时传入键值</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 21:45
 * @since 1.0.3
 */
public class Pair<K extends Comparable<K>, V extends Comparable<V>> extends cn.hutool.core.lang.Pair<K, V> implements Comparable<Pair<K, V>>, PairEntry<K, V> {
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
