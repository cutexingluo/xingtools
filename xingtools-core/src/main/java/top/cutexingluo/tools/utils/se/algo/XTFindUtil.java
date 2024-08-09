package top.cutexingluo.tools.utils.se.algo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 简单查找工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 21:55
 */
public class XTFindUtil {
    /********************普通查找****************
     /**
     * 查找符合条件的数据,查找第一个下标
     *
     * @param list          要查询的列表
     * @param filterChecker 过滤器
     * @return 下标，-1 没找到
     */
    public static <T> int findFirst(@NotNull List<T> list, @NotNull Predicate<T> filterChecker) {
        for (int i = 0; i < list.size(); i++) {
            if (filterChecker.test(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 查找符合条件的数据,查找第一个下标
     *
     * @param array         要查询的数组
     * @param filterChecker 过滤器
     * @return 下标，-1 没找到
     */
    public static <T> int findFirst(@NotNull T[] array, @NotNull Predicate<T> filterChecker) {
        for (int i = 0; i < array.length; i++) {
            if (filterChecker.test(array[i])) {
                return i;
            }
        }
        return -1;
    }

    //********************基于数据量的查找****************
    //

    /**
     * 查找符合条件的数据,查找第一个下标
     *
     * @param list          要查询的列表
     * @param filterChecker 过滤器
     * @return 下标，-2 参数错误，-1 没找到
     */
    public static <T> int findListFirst(List<T> list, Predicate<T> filterChecker) {
        return findListFirst(list, 4_0000, filterChecker);
    }

    /**
     * 查找符合条件的数据,查找第一个下标
     *
     * @param list          要查询的列表
     * @param filterChecker 过滤器
     * @param boundary      两种方式的分界值
     * @return 下标，-2 参数错误，-1 没找到
     */
    public static <T> int findListFirst(List<T> list, int boundary, Predicate<T> filterChecker) {
        if (list == null || filterChecker == null) return -2; //参数错误
        if (list.size() < boundary) {
            for (int i = 0; i < list.size(); i++) {
                if (filterChecker.test(list.get(i))) {
                    return i;
                }
            }
        } else {
            AtomicInteger ind = new AtomicInteger(-1);
            Optional<T> first1 = list.stream().filter(item -> {
                ind.incrementAndGet();
                return filterChecker.test(item);
            }).findFirst();
            if (!first1.isPresent()) ind.set(-1);
            return ind.get();
        }
        return -1;
    }

    /**
     * 查找所有符合条件的数据
     *
     * @param list          要查询的列表
     * @param filterChecker 过滤器
     * @return List<T> 符合条件的数据列表
     */
    public static <T> List<T> findListByFilter(List<T> list, Predicate<T> filterChecker) {
        return findListByFilter(list, 4_0000, filterChecker);
    }

    /**
     * 查找所有符合条件的数据
     *
     * @param list          要查询的列表
     * @param filterChecker 过滤器
     * @param boundary      两种方式的分界值
     * @return List<T> 符合条件的数据列表
     */
    public static <T> List<T> findListByFilter(List<T> list, int boundary, Predicate<T> filterChecker) {
        if (list == null || filterChecker == null) return null; //参数错误
        List<T> newList;
        if (list.size() < boundary) {
            newList = new ArrayList<>();
            for (T t : list) {
                if (filterChecker.test(t)) {
                    newList.add(t);
                }
            }
        } else {
            newList = list.stream().filter(filterChecker).collect(Collectors.toList());
        }
        return newList;
    }
}
