package top.cutexingluo.tools.utils.se.map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.utils.se.core.compare.XTComparator;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * XTHashMap
 * <p>1. 继承HashMap</p>
 * <p>2. 增加了一些方法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 20:16
 */
public class XTHashMap<K, V> extends HashMap<K, V> implements XTBaseMap<K, V>, Serializable {

    public XTHashMap() {
        super();
    }

    public XTHashMap(Map<? extends K, ? extends V> initMap) {
        super(initMap);
    }


    /**
     * 把 keyValueEntries 按K，V形式 成对放入
     * <p>如果为奇数个，最后一个赋值为null</p>
     *
     * @param keyValueEntries key,value 对
     */
    public static <K, V> XTHashMap<K, V> asMap(Object... keyValueEntries) {
        XTHashMap<K, V> map = new XTHashMap<>();
        putMapEntriesFromDValues(map, keyValueEntries);
        return map;
    }

    /**
     * 把values按K，V形式成对放入
     * <p>如果为奇数个，最后一个赋值为null</p>
     *
     * @return 添加成功的对数
     */
    public static <K, V> int putMapEntriesFromDValues(@NotNull Map<K, V> map, Object... values) {
        int tot = putMapEntriesNoResFromDValues(map, values);
        if ((values.length & 1) == 1) {
            if (values[values.length - 1] != null) {
                map.put((K) values[values.length - 1], null);
                tot++;
            }
        }
        return tot;
    }

    /**
     * 把values按K，V形式成对放入
     * <p>如果为奇数个，最后一个舍弃</p>
     *
     * @return 添加成功的对数
     */
    public static <K, V> int putMapEntriesNoResFromDValues(@NotNull Map<K, V> map, Object... values) {
        int tot = 0;
        for (int i = 0; i < values.length - 1; i += 2) {
            if (values[i] == null) continue;
            map.put((K) values[i], (V) values[i + 1]);
            tot++;
        }
        return tot;
    }


    /**
     * 把values按K，V形式成对放入
     *
     * @param keyValueEntries key,value 对
     * @return 添加成功的对数
     */
    @Override
    public int addAll(Object... keyValueEntries) {
        return putMapEntriesFromDValues(this, keyValueEntries);
    }

    /**
     * 删除指定值
     *
     * @param value value值
     * @return 删除的个数
     */
    @Override
    public int removeByValue(V value) {
        if (value == null) return 0;
        int tot = 0;
        Iterator<Entry<K, V>> iterator = this.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();
            if (value.equals(entry.getValue())) {
                iterator.remove();
                tot++;
            }
        }
        return tot;
    }

    /**
     * 删除指定值
     *
     * @param key key值
     * @return value
     */
    @Override
    public V removeByKey(K key) {
        return this.remove(key);
    }

    /**
     * 设置值，并返回设置前的值
     *
     * @param key   key
     * @param value value
     * @return the old value
     */
    @Override
    public V putReturnOld(K key, V value) {
        return this.put(key, value);
    }


    /**
     * 如果存在 则设置值，并返回设置前的值
     * 不存在 则返回null
     *
     * @param key   key
     * @param value value
     * @return the old value
     */
    @Override
    public V putIfPresentReturnOld(K key, V value) {
        V v = get(key);
        if (v != null) {
            return put(key, value);
        }
        return null;
    }

    /**
     * 按 value 去重
     * 省略hash序高的entry
     *
     * @return new map
     */
    @Override
    public XTHashMap<K, V> distinctValue() {
        XTHashMap<K, V> map2 = new XTHashMap<>(this);
        for (K key : this.keySet()) {
            if (!map2.containsValue(this.get(key))) {
                map2.put(key, this.get(key));
            }
        }
        return map2;
    }


    // -----------转为List---------------


    /**
     * 对 key 或者 value 进行 排序
     * null值排到最后
     *
     * @param byValue 通过value还是key排序
     * @param asc     正序还是倒序
     * @return List 对象
     */
    @Override
    public ArrayList<Entry<K, V>> sort(boolean byValue, boolean asc) {
        ArrayList<Entry<K, V>> infoIds = new ArrayList<>(this.entrySet());
        if (infoIds.size() == 0) return infoIds;
        if (byValue) {
            XTComparator<V> comparator = new XTComparator<>(asc);
            infoIds.sort((o1, o2) -> comparator.compare(o1.getValue(), o2.getValue()));
        } else {
            XTComparator<K> comparator = new XTComparator<>(asc);
            infoIds.sort((o1, o2) -> comparator.compare(o1.getKey(), o2.getKey()));
        }
        return infoIds;
    }

    /**
     * 根据 value 排序
     *
     * @return List对象
     */
    public ArrayList<Entry<K, V>> sortValue() {
        return sort(true, true);
    }

    /**
     * 根据 value 排序
     *
     * @return List对象
     */
    public ArrayList<Entry<K, V>> sortValue(boolean isAsc) {
        return sort(true, isAsc);
    }

    /**
     * 排序并获取非空数量
     *
     * @param byValue 是否通过value排序, false为按key排序
     * @param asc     ASC
     * @return {@link XTSortResult}<{@link K}, {@link V}>
     */
    public XTSortResult<K, V> sortAndGetNotNullCount(boolean byValue, boolean asc) {
        ArrayList<Entry<K, V>> list = this.sort(byValue, asc);
        int count = 0;
        if (byValue) {
            for (Entry<K, V> entry : list) {
                if (entry.getValue() != null) {
                    count++;
                }
            }
        } else {
            if (list.get(list.size() - 1).getValue() != null) {
                count = list.size();
            }
        }
        return new XTSortResult<>(list, count);
    }

    /**
     * 排序完返回的封装类
     */
    @Data
    @AllArgsConstructor
    public static class XTSortResult<K, V> {
        /**
         * 返回列表
         */
        public List<Entry<K, V>> list;
        /**
         * 非空数量，或者称为第一个为 null 下标
         */
        public int notNullCount;

    }

    // -----------转为LinkedHashMap---------------
    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SortMeta {
        /**
         * 是否需要排序
         */
        private boolean needSort = false;
        /**
         * 是否按value排序，false为按key
         */
        private boolean sortByValue = false;
        /**
         * 是否正序
         */
        private boolean asc = true;

    }

    /**
     * 转化为LinkedHashMap, 可排序
     *
     * @param sortMeta 对元数据进行排序
     * @return {@link LinkedHashMap}<{@link K}, {@link V}>
     */
    public LinkedHashMap<K, V> toLinkedHashMap(@Nullable SortMeta sortMeta) {
        if (sortMeta == null || !sortMeta.needSort) {
            return new LinkedHashMap<>(this);
        }

        if (sortMeta.sortByValue) {
            XTComparator<V> comparator = new XTComparator<>(sortMeta.asc);
            return this.entrySet().stream()
                    .sorted(comparator.compareBy(true))
                    .collect(
                            Collectors.toMap(Entry::getKey, Entry::getValue,
                                    (oldVal, newVal) -> oldVal, LinkedHashMap::new
                            )
                    );
        } else {
            XTComparator<K> comparator = new XTComparator<>(sortMeta.asc);
            return this.entrySet().stream()
                    .sorted(comparator.compareBy(false))
                    .collect(
                            Collectors.toMap(Entry::getKey, Entry::getValue,
                                    (oldVal, newVal) -> oldVal, LinkedHashMap::new
                            )
                    );
        }
    }
}
