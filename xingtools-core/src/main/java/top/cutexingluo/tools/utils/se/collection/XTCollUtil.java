package top.cutexingluo.tools.utils.se.collection;


import cn.hutool.core.collection.CollUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 集合相关工具类
 * 此工具方法针对 Collection 及其实现类封装的工具。
 * <p>继承 CollUtil</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/31 14:12
 * @since 1.0.4
 */
public class XTCollUtil extends CollUtil {


    @NotNull
    @Contract(pure = true)
    public static <T> BinaryOperator<T> pickFirst() {
        return (k1, k2) -> k1;
    }

    @NotNull
    @Contract(pure = true)
    public static <T> BinaryOperator<T> pickSecond() {
        return (k1, k2) -> k2;
    }

    /**
     * 非空 collection 转为 list
     *
     * @param collection 集合
     * @param mapper     转化方法
     */
    public static <T, R> List<R> mapToList(@NotNull Collection<T> collection, Function<T, R> mapper) {
        return collection.stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * 非空 collection 转为 set
     *
     * @param collection 集合
     * @param mapper     转化方法
     */
    public static <T, R> Set<R> mapToSet(@NotNull Collection<T> collection, Function<T, R> mapper) {
        return collection.stream().map(mapper).collect(Collectors.toSet());
    }

    /**
     * collection 转为 list
     *
     * @param collection 集合
     */
    @NotNull
    @Contract("null -> new")
    public static <T> List<T> toList(Collection<T> collection) {
        if (collection == null) {
            return new ArrayList<>();
        }
        if (collection instanceof List) {
            return (List<T>) collection;
        }
        return new ArrayList<>(collection);
    }

    /**
     * collection 转为 list
     *
     * @param collection 集合
     * @param mapper     转化方法
     */
    public static <T, R> List<R> toList(Collection<T> collection, Function<T, R> mapper) {
        if (collection == null) {
            return new ArrayList<>();
        }
        return mapToList(collection, mapper);
    }


    /**
     * collection 转为 set
     */
    @NotNull
    @Contract("null -> new")
    public static <T> Set<T> toSet(Collection<T> collection) {
        if (collection == null) {
            return new HashSet<>();
        }
        if (collection instanceof Set) {
            return (Set<T>) collection;
        }
        return new HashSet<>(collection);
    }

    /**
     * collection 转为 set
     *
     * @param collection 集合
     * @param mapper     转化方法
     */
    public static <T, R> Set<R> toSet(Collection<T> collection, Function<T, R> mapper) {
        if (collection == null) {
            return new HashSet<>();
        }
        return mapToSet(collection, mapper);
    }


    /**
     * <p>key 为转化方法的值</p>
     * <p>value 为 集合值 本身</p>
     *
     * @param collection 集合
     * @param keyMapper  key 转化
     */
    public static <T, K> Map<K, T> toMap(Collection<T> collection, Function<? super T, ? extends K> keyMapper) {
        return toMap(collection, keyMapper, Function.identity());
    }

    /**
     * 通过转化mapper转化值
     *
     * @param collection  集合
     * @param keyMapper   key 转化
     * @param valueMapper value 转化
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        return toMap(collection, keyMapper, valueMapper, pickSecond());
    }

    /**
     * 通过转化mapper转化值
     *
     * @param collection    集合
     * @param keyMapper     key 转化
     * @param valueMapper   value 转化
     * @param mergeFunction 合并方法 (oldValue,value) -> newValue
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper, BinaryOperator<V> mergeFunction) {
        if (CollUtil.isEmpty(collection)) {
            return new HashMap<>(0);
        }
        return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
    }


}
