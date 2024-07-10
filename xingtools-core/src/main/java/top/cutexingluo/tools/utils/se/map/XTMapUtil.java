package top.cutexingluo.tools.utils.se.map;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.se.collection.XTCollUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;


/**
 * 简单Map工具类
 * <p>关于 map 工具类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 17:47
 */
public class XTMapUtil {

    /**
     * 判断 map 是否为空
     *
     * @since 1.0.4
     */
    public static <K, V> boolean isNullOrEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 输出map，一行一个 entry
     *
     * @param map 源 map
     * @param sep key value 分隔符
     */
    public static <K, V> void printlnMap(Map<K, V> map, String sep) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry.getKey() + sep + entry.getValue());
        }
    }

    /**
     * 输出map，一行一个 entry , 默认 : 分隔
     *
     * @param map 源 map
     */
    public static <K, V> void printlnMap(Map<K, V> map) {
        printlnMap(map, " : ");
    }


    /**
     * 把values按K，V形式放入
     * <p>如果为奇数个，最后一个赋值为null</p>
     *
     * @return 添加成功的对数
     */
    public static <K, V> int putMapEntriesFromDValues(@NotNull Map<K, V> map, Object... values) {
        return XTHashMap.putMapEntriesFromDValues(map, values);
    }

    /**
     * 把values按K，V形式放入
     * <p>如果为奇数个，最后一个舍弃</p>
     *
     * @return 添加成功的对数
     */
    public static <K, V> int putMapEntriesNoResFromDValues(@NotNull Map<K, V> map, Object... values) {
        return XTHashMap.putMapEntriesNoResFromDValues(map, values);
    }

    /**
     * 把values按K，V形式放入
     *
     * @return XTHashMap 对象 , 可协变(转化)为 HashMap
     */
    public static <K, V> XTHashMap<K, V> createMapEntriesFromDValues(Object... values) {
        XTHashMap<K, V> map = new XTHashMap<>();
        XTHashMap.putMapEntriesFromDValues(map, values);
        return map;
    }


    /**
     * 删除指定值, 利用迭代器
     *
     * @param value value值
     * @return 删除的个数
     */
    public static <K, V> int deleteByValue(Map<K, V> initMap, V value) {
        if (value == null) return 0;
        int tot = 0;
        Iterator<Map.Entry<K, V>> iterator = initMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, V> entry = iterator.next();
            if (value.equals(entry.getValue())) {
                iterator.remove();
                tot++;
            }
        }
        return tot;
    }

    /**
     * Map根据value进行删除
     * <br>使用Stream
     *
     * @param initMap map
     * @param value   值
     * @return 删除的数量
     */
    public static <K, V> int deleteByValueWithStream(Map<K, V> initMap, @NotNull V value) {
        AtomicInteger tot = new AtomicInteger();
        initMap.entrySet().removeIf(entry -> {
            if (value.equals(entry.getValue())) {
                tot.getAndIncrement();
                return true;
            }
            return false;
        });
        return tot.get();
    }


    /**
     * Map根据value进行删除
     * <br>使用CopyOnWriteArraySet
     *
     * @param initMap map
     * @param value   值
     * @return 删除的数量
     */
    public static <K, V> int deleteByValueWithSet(Map<K, V> initMap, @NotNull V value) {
        int tot = 0;
        Set<Map.Entry<K, V>> entries = new CopyOnWriteArraySet<>(initMap.entrySet());
        for (Map.Entry<K, V> entry : entries) {
            if (value.equals(entry.getValue())) {
                initMap.remove(entry.getKey());
                tot++;
            }
        }
        return tot;
    }

    /**
     * Map根据value进行删除
     * <br>使用ConcurrentHashMap
     *
     * @param initMap map
     * @param value   值
     * @return 删除的数量
     */
    public static <K, V> int deleteByValueWithConcurrent(Map<K, V> initMap, @NotNull V value) {
        AtomicInteger tot = new AtomicInteger();
        ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>(initMap);
        map.forEach((k, v) -> {
            if (value.equals(v)) {
                map.remove(k);
                tot.getAndIncrement();
            }
        });
        return tot.get();
    }

    /**
     * map转化方法
     * <p>key为原来的key</p>
     * <p>value 为valueMapper的返回值</p>
     *
     * @param map           原 map
     * @param valueMapper   通过key,value 转化value
     * @param mergeFunction 合并函数
     * @return new Map
     * @since 1.0.4
     */
    public static <K, V, C> Map<K, C> convertMapValue(Map<K, V> map,
                                                      BiFunction<K, V, C> valueMapper,
                                                      BinaryOperator<C> mergeFunction) {
        if (isNullOrEmpty(map)) {
            return new HashMap<>();
        }
        return map.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> valueMapper.apply(e.getKey(), e.getValue()),
                mergeFunction
        ));
    }

    /**
     * map转化方法
     * <p>key为原来的key</p>
     * <p>value 为valueMapper的返回值</p>
     * <p>合并方法采用 {@link XTCollUtil} XTCollUtil.pickSecond()</p>
     *
     * @param originMap   原 map
     * @param valueMapper 通过key,value 转化value
     * @return new Map
     * @since 1.0.4
     */
    public static <K, V, C> Map<K, C> convertMapValue(Map<K, V> originMap, BiFunction<K, V, C> valueMapper) {
        return convertMapValue(originMap, valueMapper, XTCollUtil.pickSecond());
    }


}
