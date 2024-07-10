package top.cutexingluo.tools.utils.se.map;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 简单Set工具类
 * <p>关于 Set 工具类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 13:50
 */
public class XTSetUtil {


    @SafeVarargs
    public static <E> HashSet<E> hashSet(E... items) {
        return items == null ? null : new HashSet(Arrays.asList(items));
    }

    /**
     * 新建 HashSet
     *
     * @param keyValues key 值
     */
    @SafeVarargs
    public static <K> HashSet<K> asHashSet(K... keyValues) {
        return new HashSet<>(Arrays.asList(keyValues));
    }


    /**
     * 新建 HashSet
     *
     * @param keyValues key 值
     */
    public static <K> Set<K> toSet(K[] keyValues) {
        return new HashSet<>(Arrays.asList(keyValues));
    }

    //***********************转化类型***********************

    /**
     * [] 数组转为 Set
     */
    public static Set<Integer> toSet(int[] array) {
        return Arrays.stream(array).boxed().collect(Collectors.toSet());
    }

    /**
     * [] 数组转为 List
     */
    public static Set<Long> toSet(long[] array) {
        return Arrays.stream(array).boxed().collect(Collectors.toSet());
    }

    /**
     * [] 数组转为 List
     */
    public static Set<Double> toSet(double[] array) {
        return Arrays.stream(array).boxed().collect(Collectors.toSet());
    }


}
