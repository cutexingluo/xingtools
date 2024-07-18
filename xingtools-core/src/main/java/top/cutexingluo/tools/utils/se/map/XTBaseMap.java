package top.cutexingluo.tools.utils.se.map;

import java.util.List;
import java.util.Map;

/**
 * 基本实现Map
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 21:01
 */
public interface XTBaseMap<K, V> extends Map<K, V> {

    /**
     * 添加值
     */
    int addAll(Object... values);


    /**
     * 按 value 删除值
     */
    int removeByValue(V value);

    /**
     * 按 key 删除值
     */
    V removeByKey(K key);

    /**
     * put
     *
     * @return old value
     */
    default V putReturnOld(K key, V value) {
        return put(key, value);
    }

    /**
     * IfPresent  -> put
     *
     * @return old value
     * @update 1.0.3
     * @since 1.0.3
     */
    default V putIfPresentReturnOld(K key, V value) {
        V v = get(key);
        if (v != null) {
            return put(key, value);
        }
        return v;
    }

    /**
     * distinct (Value)
     *
     * @return new map
     */
    XTBaseMap<K, V> distinctValue();

    /**
     * sort()
     *
     * @param byValue 通过value还是key排序
     * @param asc     正序还是倒序
     * @return List<Entry> 对象
     */
    List<Entry<K, V>> sort(boolean byValue, boolean asc);
}
