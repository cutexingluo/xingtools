package top.cutexingluo.tools.utils.se.algo.cpp.math;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

/**
 * 二分查找工具类
 *
 * <p>涵盖了多个二分查找方法，几乎适合所有情况</p>
 * <p>原来 XTMath 的方法移植到此类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/20 15:52
 * @since 1.1.6
 */
public class XTBinarySearch {


    /**
     * 二分查找
     */
    public static <T> int binarySearch(T[] a, int fromIndex, int toIndex, T key) {
        return Arrays.binarySearch(a, fromIndex, toIndex, key);
    }

    /**
     * 二分查找
     */
    public static <T> int binarySearch(T[] a, T key) {
        return Arrays.binarySearch(a, key);
    }

    /**
     * 二分查找
     */
    public static <T> int binarySearch(T[] a, T key, Comparator<? super T> c) {
        return Arrays.binarySearch(a, key, c);
    }

    /**
     * 二分查找
     */
    public static <T> int binarySearch(T[] a, int fromIndex, int toIndex, T key, Comparator<? super T> c) {
        return Arrays.binarySearch(a, fromIndex, toIndex, key, c);
    }

    /**
     * 二分查找阈值
     *
     * @see Collections
     * @since 1.1.6
     */
    public static int BINARYSEARCH_THRESHOLD = 5000;

    /**
     * 二分查找
     */
    public static <T> int binarySearch(List<? extends T> list, T key, Comparator<? super T> c) {
        return Collections.binarySearch(list, key, c);
    }

    /**
     * 二分查找
     */
    public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key) {
        return Collections.binarySearch(list, key);
    }

    /**
     * 二分查找
     *
     * @see Collections#binarySearch(List, Object, Comparator)
     * @since 1.1.6
     */
    @SuppressWarnings("unchecked")
    public static <T, E> int binarySearch(List<? extends E> list, @Nullable Function<E, T> mapper, T key, @Nullable Comparator<? super T> c) {
        if (mapper == null) {
            if (c == null) {
                return Collections.binarySearch((List<? extends Comparable<? super E>>) list, (E) key);
            } else {
                return Collections.binarySearch(list, (E) key, (Comparator<? super E>) c);
            }
        } else {
            if (c == null) c = (Comparator<? super T>) Comparator.naturalOrder();
//            if (c == null) c = new XTComparator<>(true); // not use
        }

        if (list instanceof RandomAccess || list.size() < BINARYSEARCH_THRESHOLD)
            return indexedBinarySearch(list, mapper, key, c);
        else
            return iteratorBinarySearch(list, mapper, key, c);
    }

    /**
     * 索引二分查找
     *
     * @param mapper list element mapper
     * @since 1.1.6
     */
    public static <T, E> int indexedBinarySearch(@NotNull List<? extends E> l, @NotNull Function<E, T> mapper, T key, Comparator<? super T> c) {
        int low = 0;
        int high = l.size() - 1;

        while (low <= high) {
            int mid = low + (high - low >>> 1); // prevent overflow
            E midVal = l.get(mid);
            int cmp = c.compare(mapper.apply(midVal), key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found
    }

    /**
     * 迭代器二分查找
     *
     * @param mapper list element mapper
     * @since 1.1.6
     */
    private static <T, E> int iteratorBinarySearch(@NotNull List<? extends E> l, @NotNull Function<E, T> mapper, T key, Comparator<? super T> c) {
        int low = 0;
        int high = l.size() - 1;
        ListIterator<? extends E> i = l.listIterator();

        while (low <= high) {
            int mid = low + (high - low >>> 1); // prevent overflow
            E midVal = get(i, mid);
            int cmp = c.compare(mapper.apply(midVal), key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found
    }

    /**
     * 获取指定位置的元素
     *
     * @since 1.1.6
     */
    public static <T> T get(@NotNull ListIterator<? extends T> i, int index) {
        T obj = null;
        int pos = i.nextIndex();
        if (pos <= index) {
            do {
                obj = i.next();
            } while (pos++ < index);
        } else {
            do {
                obj = i.previous();
            } while (--pos > index);
        }
        return obj;
    }

    // XTMath


    // lower_bound

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound</p>
     *
     * @param arr 数据集
     * @param tar 目标数据
     * @return index 下标
     */
    public static int lowerBound(int[] arr, int tar) {
        return lowerBound(arr, 0, arr.length, tar);
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound</p>
     *
     * @param arr   数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static int lowerBound(int[] arr, int start, int end, int tar) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            int compare = arr[mid] - tar;
            if (compare >= .0) r = mid - 1;
            else l = mid + 1;
        }
        return l;
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound</p>
     *
     * @param arr 数据集
     * @param tar 目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int lowerBound(T[] arr, T tar) {
        return lowerBound(arr, 0, arr.length, tar);
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound</p>
     *
     * @param arr 数据集
     * @param tar 目标数据
     * @return index 下标
     */
    public static <T> int lowerBound(T[] arr, T tar, Comparator<? super T> c) {
        return lowerBound(arr, 0, arr.length, tar, c);
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound</p>
     *
     * @param arr   数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int lowerBound(T[] arr, int start, int end, T tar) {
        return lowerBound(arr, start, end, tar, T::compareTo);
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound</p>
     *
     * @param arr   数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @param c     比较器
     * @return index 下标
     */
    public static <T> int lowerBound(T[] arr, int start, int end, T tar, Comparator<? super T> c) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            int compare = c.compare(arr[mid], tar);
            if (compare >= 0) r = mid - 1;
            else l = mid + 1;
        }
        return l;
    }

    // -list

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound</p>
     *
     * @param list 数据集
     * @param tar  目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int lowerBound(List<? extends T> list, T tar) {
        return lowerBound(list, 0, list.size(), tar, T::compareTo);
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound</p>
     */
    public static <T> int lowerBound(List<? extends T> list, T tar, Comparator<? super T> c) {
        return lowerBound(list, 0, list.size(), tar, c);
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound</p>
     *
     * @param list  数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static <T> int lowerBound(List<? extends T> list, int start, int end, T tar, Comparator<? super T> c) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            T elem = list.get(mid);
            int compare = c.compare(elem, tar);
            if (compare >= 0) r = mid - 1;
            else l = mid + 1;
        }
        return l;
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound</p>
     */
    public static <T, E> int lowerBound(List<? extends E> list, @NotNull Function<E, T> mapper, T tar, Comparator<? super T> c) {
        return lowerBound(list, 0, list.size(), mapper, tar, c);
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound</p>
     *
     * @param list   数据集
     * @param start  开始 ,inclusive
     * @param end    结束, exclusive
     * @param mapper 元素 mapper
     * @param tar    目标数据
     * @param c      比较器
     * @return index 下标
     */
    public static <T, E> int lowerBound(List<? extends E> list, int start, int end, @NotNull Function<E, T> mapper, T tar, Comparator<? super T> c) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            E elem = list.get(mid);
            int compare = c.compare(mapper.apply(elem), tar);
            if (compare >= 0) r = mid - 1;
            else l = mid + 1;
        }
        return l;
    }


    // upper_bound

    /**
     * 找到大于目标的数据的位置
     * <p>类似 c++ std::upper_bound</p>
     *
     * @param arr 数据集
     * @param tar 目标数据
     * @return index 下标
     */
    public static int upperBound(int[] arr, int tar) {
        return upperBound(arr, 0, arr.length, tar);
    }

    /**
     * 找到大于目标的数据的位置
     * <p>类似 c++ std::upper_bound</p>
     *
     * @param arr   数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static int upperBound(int[] arr, int start, int end, int tar) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            if (arr[mid] > tar) r = mid - 1;
            else l = mid + 1;
        }
        return l;
    }

    /**
     * 找到大于目标的数据的位置
     * <p>类似 c++ std::upper_bound</p>
     *
     * @param arr 数据集
     * @param tar 目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int upperBound(T[] arr, T tar) {
        return upperBound(arr, 0, arr.length, tar);
    }

    /**
     * 找到大于目标的数据的位置
     * <p>类似 c++ std::upper_bound</p>
     *
     * @param arr 数据集
     * @param tar 目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int upperBound(T[] arr, T tar, Comparator<? super T> c) {
        return upperBound(arr, 0, arr.length, tar, c);
    }

    /**
     * 找到大于目标的数据的位置
     * <p>类似 c++ std::upper_bound</p>
     *
     * @param arr   数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int upperBound(T[] arr, int start, int end, T tar) {
        return upperBound(arr, start, end, tar, T::compareTo);
    }

    /**
     * 找到大于目标的数据的位置
     * <p>类似 c++ std::upper_bound</p>
     *
     * @param arr   数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static <T> int upperBound(T[] arr, int start, int end, T tar, Comparator<T> c) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            int compare = c.compare(arr[mid], tar);
            if (compare > 0) r = mid - 1;
            else l = mid + 1;
        }
        return l;
    }

    // -list

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::upper_bound</p>
     *
     * @param list 数据集
     * @param tar  目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int upperBound(List<? extends T> list, T tar) {
        return upperBound(list, 0, list.size(), tar, T::compareTo);
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::upper_bound</p>
     */
    public static <T> int upperBound(List<? extends T> list, T tar, Comparator<? super T> c) {
        return upperBound(list, 0, list.size(), tar, c);
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::upper_bound</p>
     *
     * @param list  数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static <T> int upperBound(List<? extends T> list, int start, int end, T tar, Comparator<? super T> c) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            T elem = list.get(mid);
            int compare = c.compare(elem, tar);
            if (compare > 0) r = mid - 1;
            else l = mid + 1;
        }
        return l;
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::upper_bound</p>
     */
    public static <T, E> int upperBound(List<? extends E> list, @NotNull Function<E, T> mapper, T tar, Comparator<? super T> c) {
        return upperBound(list, 0, list.size(), mapper, tar, c);
    }

    /**
     * 找到大于或等于目标的数据的位置
     * <p>类似 c++ std::upper_bound</p>
     *
     * @param list   数据集
     * @param start  开始 ,inclusive
     * @param end    结束, exclusive
     * @param mapper 元素 mapper
     * @param tar    目标数据
     * @param c      比较器
     * @return index 下标
     */
    public static <T, E> int upperBound(List<? extends E> list, int start, int end, @NotNull Function<E, T> mapper, T tar, Comparator<? super T> c) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            E elem = list.get(mid);
            int compare = c.compare(mapper.apply(elem), tar);
            if (compare > 0) r = mid - 1;
            else l = mid + 1;
        }
        return l;
    }


    // reversed


    // lower_bound (greater)

    /**
     * 找到小于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound (greater)</p>
     *
     * @param arr 数据集
     * @param tar 目标数据
     * @return index 下标
     */
    public static int lowerBoundReversed(int[] arr, int tar) {
        return lowerBoundReversed(arr, 0, arr.length, tar);
    }

    /**
     * 找到小于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound (greater) (greater)</p>
     *
     * @param arr   数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static int lowerBoundReversed(int[] arr, int start, int end, int tar) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            int compare = arr[mid] - tar;
            if (compare <= 0) r = mid - 1;
            else l = mid + 1;
        }
        return r;
    }

    /**
     * 找到小于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound (greater)</p>
     *
     * @param arr 数据集
     * @param tar 目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int lowerBoundReversed(T[] arr, T tar) {
        return lowerBoundReversed(arr, 0, arr.length, tar);
    }

    /**
     * 找到小于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound (greater)</p>
     *
     * @param arr 数据集
     * @param tar 目标数据
     * @return index 下标
     */
    public static <T> int lowerBoundReversed(T[] arr, T tar, Comparator<? super T> c) {
        return lowerBoundReversed(arr, 0, arr.length, tar, c);
    }

    /**
     * 找到小于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound (greater)</p>
     *
     * @param arr   数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int lowerBoundReversed(T[] arr, int start, int end, T tar) {
        return lowerBoundReversed(arr, start, end, tar, T::compareTo);
    }

    /**
     * 找到小于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound (greater)</p>
     *
     * @param arr   数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @param c     比较器
     * @return index 下标
     */
    public static <T> int lowerBoundReversed(T[] arr, int start, int end, T tar, Comparator<? super T> c) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            int compare = c.compare(arr[mid], tar);
            if (compare <= 0) l = mid + 1;
            else r = mid - 1;
        }
        return r;
    }

    // -list

    /**
     * 找到小于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound (greater)</p>
     *
     * @param list 数据集
     * @param tar  目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int lowerBoundReversed(List<? extends T> list, T tar) {
        return lowerBoundReversed(list, 0, list.size(), tar, T::compareTo);
    }

    /**
     * 找到小于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound (greater)</p>
     */
    public static <T> int lowerBoundReversed(List<? extends T> list, T tar, Comparator<? super T> c) {
        return lowerBoundReversed(list, 0, list.size(), tar, c);
    }

    /**
     * 找到小于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound (greater)</p>
     *
     * @param list  数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static <T> int lowerBoundReversed(List<? extends T> list, int start, int end, T tar, Comparator<? super T> c) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            T elem = list.get(mid);
            int compare = c.compare(elem, tar);
            if (compare <= 0) l = mid + 1;
            else r = mid - 1;
        }
        return r;
    }

    /**
     * 找到小于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound (greater)</p>
     */
    public static <T, E> int lowerBoundReversed(List<? extends E> list, @NotNull Function<E, T> mapper, T tar, Comparator<? super T> c) {
        return lowerBoundReversed(list, 0, list.size(), mapper, tar, c);
    }

    /**
     * 找到小于或等于目标的数据的位置
     * <p>类似 c++ std::lower_bound (greater)</p>
     *
     * @param list   数据集
     * @param start  开始 ,inclusive
     * @param end    结束, exclusive
     * @param mapper 元素 mapper
     * @param tar    目标数据
     * @param c      比较器
     * @return index 下标
     */
    public static <T, E> int lowerBoundReversed(List<? extends E> list, int start, int end, @NotNull Function<E, T> mapper, T tar, Comparator<? super T> c) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            E elem = list.get(mid);
            int compare = c.compare(mapper.apply(elem), tar);
            if (compare <= 0) l = mid + 1;
            else r = mid - 1;
        }
        return r;
    }


    // upper_bound (greater)

    /**
     * 找到小于目标的数据的位置
     * <p>类似 c++ std::upper_bound (greater)</p>
     *
     * @param arr 数据集
     * @param tar 目标数据
     * @return index 下标
     */
    public static int upperBoundReversed(int[] arr, int tar) {
        return upperBoundReversed(arr, 0, arr.length, tar);
    }

    /**
     * 找到小于目标的数据的位置
     * <p>类似 c++ std::upper_bound (greater)</p>
     *
     * @param arr   数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static int upperBoundReversed(int[] arr, int start, int end, int tar) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            int compare = arr[mid] - tar;
            if (compare < 0) l = mid + 1;
            else r = mid - 1;
        }
        return r;
    }

    /**
     * 找到小于目标的数据的位置
     * <p>类似 c++ std::upper_bound (greater)</p>
     *
     * @param arr 数据集
     * @param tar 目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int upperBoundReversed(T[] arr, T tar) {
        return upperBoundReversed(arr, 0, arr.length, tar);
    }

    /**
     * 找到小于目标的数据的位置
     * <p>类似 c++ std::upper_bound (greater)</p>
     *
     * @param arr 数据集
     * @param tar 目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int upperBoundReversed(T[] arr, T tar, Comparator<? super T> c) {
        return upperBoundReversed(arr, 0, arr.length, tar, c);
    }

    /**
     * 找到小于目标的数据的位置
     * <p>类似 c++ std::upper_bound (greater)</p>
     *
     * @param arr   数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int upperBoundReversed(T[] arr, int start, int end, T tar) {
        return upperBoundReversed(arr, start, end, tar, T::compareTo);
    }

    /**
     * 找到小于目标的数据的位置
     * <p>类似 c++ std::upper_bound (greater)</p>
     *
     * @param arr   数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static <T> int upperBoundReversed(T[] arr, int start, int end, T tar, Comparator<T> c) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            int compare = c.compare(arr[mid], tar);
            if (compare < 0) l = mid + 1;
            else r = mid - 1;
        }
        return r;
    }

    // -list

    /**
     * 找到小于目标的数据的位置
     * <p>类似 c++ std::upper_bound (greater)</p>
     *
     * @param list 数据集
     * @param tar  目标数据
     * @return index 下标
     */
    public static <T extends Comparable<T>> int upperBoundReversed(List<? extends T> list, T tar) {
        return upperBoundReversed(list, 0, list.size(), tar, T::compareTo);
    }

    /**
     * 找到小于目标的数据的位置
     * <p>类似 c++ std::upper_bound (greater)</p>
     */
    public static <T> int upperBoundReversed(List<? extends T> list, T tar, Comparator<? super T> c) {
        return upperBoundReversed(list, 0, list.size(), tar, c);
    }

    /**
     * 找到小于目标的数据的位置
     * <p>类似 c++ std::upper_bound (greater)</p>
     *
     * @param list  数据集
     * @param start 开始 ,inclusive
     * @param end   结束, exclusive
     * @param tar   目标数据
     * @return index 下标
     */
    public static <T> int upperBoundReversed(List<? extends T> list, int start, int end, T tar, Comparator<? super T> c) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            T elem = list.get(mid);
            int compare = c.compare(elem, tar);
            if (compare < 0) l = mid + 1;
            else r = mid - 1;
        }
        return r;
    }

    /**
     * 找到小于目标的数据的位置
     * <p>类似 c++ std::upper_bound (greater)</p>
     */
    public static <T, E> int upperBoundReversed(List<? extends E> list, @NotNull Function<E, T> mapper, T tar, Comparator<? super T> c) {
        return upperBoundReversed(list, 0, list.size(), mapper, tar, c);
    }

    /**
     * 找到小于目标的数据的位置
     * <p>类似 c++ std::upper_bound (greater)</p>
     *
     * @param list   数据集
     * @param start  开始 ,inclusive
     * @param end    结束, exclusive
     * @param mapper 元素 mapper
     * @param tar    目标数据
     * @param c      比较器
     * @return index 下标
     */
    public static <T, E> int upperBoundReversed(List<? extends E> list, int start, int end, @NotNull Function<E, T> mapper, T tar, Comparator<? super T> c) {
        int l = start, r = end - 1;
        while (l <= r) {
            int mid = l + (r - l >> 1);
            E elem = list.get(mid);
            int compare = c.compare(mapper.apply(elem), tar);
            if (compare < 0) l = mid + 1;
            else r = mid - 1;
        }
        return r;
    }


    // test example
//    public static void main(String[] args) {
//        Integer[] a = {1, 2, 3, 4, 5, 6, 8, 9, 10};
//        int index1 = lowerBound(a, 7); // 6 -> 8 的位置
//        System.out.println(index1);
//
//        int index2 = lowerBound(a, 8);// 6 -> 8 的位置
//        System.out.println(index2);
//
//        int index3 = upperBound(a, 7); // 6 -> 8 的位置
//        System.out.println(index3);
//
//        int index4 = upperBound(a, 8);// 7 -> 9 的位置
//        System.out.println(index4);
//
//        int index5 = lowerBoundReversed(a, 7); // 5 -> 6 的位置
//        System.out.println(index5);
//
//        int index6 = lowerBoundReversed(a, 8);// 6 -> 8 的位置
//        System.out.println(index6);
//
//        int index7 = upperBoundReversed(a, 7); // 5 -> 6 的位置
//        System.out.println(index7);
//
//        int index8 = upperBoundReversed(a, 8);// 5 -> 6 的位置
//        System.out.println(index8);
//
//    }
}
