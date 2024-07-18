package top.cutexingluo.tools.utils.se.algo.cpp.common;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.se.algo.cpp.math.XTMath;
import top.cutexingluo.tools.utils.se.array.XTArrayUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * 排序工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/31 11:43
 * @since 1.0.3
 */
public class Sorts {
    /**
     * 交换位置
     */
    public static <T> void swap(@NotNull T[] a, int i, int j) {
        T t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void swap(@NotNull long[] a, int i, int j) {
        long t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void swap(@NotNull int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void swap(@NotNull double[] a, int i, int j) {
        double t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void swap(@NotNull float[] a, int i, int j) {
        float t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    //---------------------selectSort-------------

    /**
     * 选择排序 (稳定)
     * <p>从小到大</p>
     * <p> O(n²)</p>
     *
     * @param arr 数组
     */
    public static void selectSort(int[] arr) {
        selectSort(XTArrayUtil.toBoxed(arr));
    }

    /**
     * 选择排序 (稳定)
     * <p>从小到大</p>
     * <p> O(n²)</p>
     *
     * @param arr 数组
     */
    public static <T extends Comparable<? super T>> void selectSort(T[] arr) {
        selectSort(arr, T::compareTo);
    }

    /**
     * 选择排序 (稳定)
     * <p>从小到大</p>
     * <p> O(n²)</p>
     *
     * @param arr 数组
     */
    public static <T> void selectSort(@NotNull T[] arr, @NotNull Comparator<? super T> c) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (c.compare(arr[j], arr[j + 1]) < 0) {
                    swap(arr, i, j);
                }
            }
        }
    }

    //---------------------bubbleSort-------------

    /**
     * 冒泡排序(稳定)
     * <p>从小到大</p>
     * <p> O(n²)</p>
     *
     * @param arr 数组
     */
    public static void bubbleSort(int[] arr) {
        bubbleSort(XTArrayUtil.toBoxed(arr));
    }

    /**
     * 冒泡排序(稳定)
     * <p>从小到大</p>
     * <p> O(n²)</p>
     *
     * @param arr 数组
     */
    public static <T extends Comparable<? super T>> void bubbleSort(T[] arr) {
        bubbleSort(arr, T::compareTo);
    }

    /**
     * 冒泡排序(稳定)
     * <p>从小到大</p>
     * <p> O(n²)</p>
     *
     * @param arr 数组
     */
    public static <T> void bubbleSort(@NotNull T[] arr, @NotNull Comparator<? super T> c) {
        int n = arr.length;
        boolean f;
        int pos = 0;
        int l = 0, r = n - 1;
        for (int i = 0; i < n - 1; i++) {
            f = false;
            for (int j = l; j < r; j++) {
                if (c.compare(arr[j], arr[j + 1]) > 0) { //arr[j] > arr[j + 1]
                    swap(arr, j, j + 1);
                    f = true;
                    pos = j;
                }
            }
            if (!f) break;
            r = pos;
            for (int j = r; j > l; j--) { // 反向最小
                if (c.compare(arr[j], arr[j - 1]) < 0) { //arr[j] < arr[j - 1]
                    swap(arr, j, j - 1);
                    f = true;
                }
            }
            l++;
            if (!f) break;
        }
    }


    /**
     * 冒泡排序(稳定)
     * <p>从小到大</p>
     * <p>普通版本</p>
     * <p> O(n²)</p>
     *
     * @param arr 数组
     */
    public static void bubbleSortOld(@NotNull int[] arr) {
        int n = arr.length;
        boolean f;
        for (int i = 0; i < n - 1; i++) {
            f = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    f = true;
                }
            }
            if (!f) break;
        }
    }
    //---------------------insertSort-------------

    /**
     * 插入排序(稳定)
     * <p>从小到大</p>
     * <p> O(n²)</p>
     *
     * @param arr 数组
     */
    public static void insertSort(int[] arr) {
        insertSort(XTArrayUtil.toBoxed(arr));
    }

    /**
     * 插入排序(稳定)
     * <p>从小到大</p>
     * <p> O(n²)</p>
     *
     * @param arr 数组
     */
    public static <T extends Comparable<T>> void insertSort(T[] arr) {
        insertSort(arr, T::compareTo);
    }

    /**
     * 插入排序(稳定)
     * <p>从小到大</p>
     * <p> O(n²)</p>
     *
     * @param arr 数组
     */
    public static <T> void insertSort(@NotNull T[] arr, @NotNull Comparator<? super T> c) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            T temp = arr[i];
            int tar = XTMath.upperBound(arr, 0, i, temp, c);
            if (tar == i) continue; // 跳过
            // 将元素插入到正确的位置
            if (i - tar >= 0) System.arraycopy(arr, tar, arr, tar + 1, i - tar);
            arr[tar] = temp;
        }
    }

    /**
     * 希尔排序(不稳定)
     * <p>从小到大</p>
     * <p> O(n(^1.3))</p>
     *
     * @param arr 数组
     */
    public static void shellSort(@NotNull int[] arr) {
        int n = arr.length;
        // 设置增量序列，这里使用希尔增量
        for (int gap = n >> 1; gap > 0; gap >>= 1) {
            // 对各个分组进行插入排序
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;
                // 在当前分组中进行插入排序
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
        }
    }

    //---------------------countSort-------------

    /**
     * 计数排序(稳定)
     * <p>从小到大</p>
     * <p>O(n+k)</p>
     *
     * @param arr   数组
     * @param maxId 数组最大的数
     */
    public static void countSort(@NotNull int[] arr, int maxId) {
        int n = arr.length;
        int[] cnt = new int[maxId + 1];//记录索引数字出现的次数
        for (int k : arr) {
            cnt[k]++;
        }
        int idx = 0;
        for (int i = 0; i < cnt.length; i++) {
            if (cnt[i] == 0)
                continue;
            for (int j = 0; j < cnt[i]; j++) {
                arr[idx++] = i;
            }
        }
    }

    //---------------------radixSort-------------

    /**
     * 基数排序(稳定)
     * <p>从小到大</p>
     * <p>O(n*k)</p>
     *
     * @param arr 数组
     */
    public static void radixSort(@NotNull int[] arr) {
        int n = arr.length;
        int max = arr[0];//最大值
        for (int val : arr) {
            if (max < val)
                max = val;
        }
        int len = max == 0 ? 1 : 0;//最大值位数
        while (max > 0) {
            len++;
            max /= 10;
        }
        int base = 1;
        for (int i = 0; i < len; i++) {
            int[][] tmp = new int[10][n];
            int[] idx = new int[10];
            for (int value : arr) {
                int bit = value / base % 10;//获取某一位
                tmp[bit][idx[bit]++] = value;
            }
            int next = 0;
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < idx[j]; k++) {
                    arr[next++] = tmp[j][k];
                }
            }
            base *= 10;
        }
    }

    //---------------------quickSort-------------

    /**
     * 快速排序(不稳定),分治算法
     * <p>从小到大</p>
     * <p>O(n log₂n)   最坏O(n²)</p>
     *
     * @param arr 数组
     */
    public static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    /**
     * 快速排序(不稳定),分治算法
     * <p>从小到大</p>
     * <p>O(n log₂n)   最坏O(n²)</p>
     *
     * @param arr  数组
     * @param low  begin inclusive
     * @param high end   inclusive
     */
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    protected static int partition(@NotNull int[] arr, int low, int high) {
        // 选择枢轴元素
        int mid = low + (high - low) / 2;
        int pivot = arr[mid];
        // 将枢轴元素移到数组末尾
        swap(arr, mid, high);
        int i = low;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        // 将枢轴元素放入正确的位置
        swap(arr, i, high);
        return i;
    }

    /**
     * 快速排序(不稳定),分治算法
     * <p>从小到大</p>
     * <p>O(n log₂n)   最坏O(n²)</p>
     *
     * @param arr 数组
     */
    public static void quickSort2(int[] arr) {
        quickSort2(arr, 0, arr.length - 1);
    }

    /**
     * 快速排序(不稳定),分治算法
     * <p>从小到大</p>
     * <p>O(n log₂n)   最坏O(n²)</p>
     *
     * @param arr   数组
     * @param begin inclusive
     * @param end   inclusive
     */
    public static void quickSort2(int[] arr, int begin, int end) {
        if (begin >= end)
            return;
        int p = arr[(begin + end) >> 1];//取基准数
        int idx = begin, l = begin, r = end;
        while (idx <= r) {
            if (arr[idx] < p) {
                swap(arr, idx++, l++);
            } else if (arr[idx] > p) {
                swap(arr, idx, r--);
            } else
                idx++;
        }
        // 递归完成对于两个子序列的快速排序
        quickSort2(arr, begin, l - 1);
        quickSort2(arr, r + 1, end);
    }

    //------------------------- new quickSort ---------------------------------------

    /**
     * 快速排序(不稳定),分治算法
     * <p>从小到大</p>
     * <p>O(n log₂n)   最坏O(n²)</p>
     *
     * @param arr 数组
     */
    public static <T extends Comparable<T>> void quickSort(T[] arr) {
        quickSort(arr, 0, arr.length - 1, T::compareTo);
    }

    /**
     * 快速排序(不稳定),分治算法
     * <p>从小到大</p>
     * <p>O(n log₂n)   最坏O(n²)</p>
     *
     * @param arr 数组
     */
    public static <T> void quickSort(T[] arr, @NotNull Comparator<? super T> c) {
        quickSort(arr, 0, arr.length - 1, c);
    }

    /**
     * 快速排序(不稳定),分治算法
     * <p>从小到大</p>
     * <p>O(n log₂n)   最坏O(n²)</p>
     *
     * @param arr  数组
     * @param low  begin inclusive
     * @param high end   inclusive
     */
    public static <T extends Comparable<T>> void quickSort(T[] arr, int low, int high) {
        quickSort(arr, low, high, T::compareTo);
    }

    /**
     * 快速排序(不稳定),分治算法
     * <p>从小到大</p>
     * <p>O(n log₂n)   最坏O(n²)</p>
     *
     * @param arr  数组
     * @param low  begin inclusive
     * @param high end   inclusive
     */
    public static <T> void quickSort(T[] arr, int low, int high, @NotNull Comparator<? super T> c) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high, c);
            partition(arr, low, pivotIndex - 1, c);
            partition(arr, pivotIndex + 1, high, c);
        }
    }

    protected static <T> int partition(T[] arr, int low, int high, @NotNull Comparator<? super T> c) {
        int p = getPivotIndex(arr, low, high, c);
        // 将枢轴元素移到倒数第二个位置，保持固定不动
        swap(arr, p, high - 1);
        T pivot = arr[high - 1];
        int i = low;
        int j = high - 1;

        while (i <= j) {
            while (i <= j && c.compare(arr[i], pivot) <= 0) {//arr[i] <= pivot
                i++;
            }
            while (i <= j && c.compare(arr[j], pivot) > 0) { // arr[j] > pivot
                j--;
            }
            if (i <= j) {
                swap(arr, i, j);
            } else {
                break;
            }
        }
        // 将枢轴元素放入正确的位置
        swap(arr, i, high - 1);
        return i;
    }

    // 采用三数取中法选择枢轴元素
    protected static <T> int getPivotIndex(@NotNull T[] arr, int low, int high, @NotNull Comparator<? super T> c) {
        int mid = (low + high) >> 1;
        if (c.compare(arr[low], arr[mid]) > 0) { // arr[low] > arr[mid]
            swap(arr, low, mid);
        }
        if (c.compare(arr[low], arr[high]) > 0) {//arr[low] > arr[high]
            swap(arr, low, high);
        }
        if (c.compare(arr[mid], arr[high]) > 0) { // arr[mid] > arr[high]
            swap(arr, mid, high);
        }
        return mid;
    }


    //---------------------mergeSort-------------

    /**
     * 归并排序(稳定),分治算法
     * <p>从小到大</p>
     * <p>O(n log₂n)</p>
     *
     * @param arr 数组
     */
    public static void mergeSort(int[] arr) {
        mergeSort(arr, 0, arr.length - 1);
    }

    public static void mergeSort(int[] arr, int begin, int end) {
        if (begin >= end)
            return;
        int mid = (begin + end) >> 1;
        mergeSort(arr, begin, mid);
        mergeSort(arr, mid + 1, end);
        int[] tmp = new int[end - begin + 1];
        int idx = 0;
        int begin1 = begin;
        int begin2 = mid + 1;
        while (begin1 <= mid && begin2 <= end) {
            if (arr[begin1] < arr[begin2])
                tmp[idx++] = arr[begin1++];
            else
                tmp[idx++] = arr[begin2++];
        }
        while (begin1 <= mid)
            tmp[idx++] = arr[begin1++];
        while (begin2 <= end)
            tmp[idx++] = arr[begin2++];
        idx = 0;
        for (int i = begin; i <= end; i++, idx++) {
            arr[i] = tmp[idx];
        }
    }

    //---------------------bucketSort-------------

    /**
     * 桶排序(稳定)
     * <p>从小到大</p>
     * <p>O(n+k)  最坏O(n²)</p>
     *
     * @param arr 数组
     */
    public static void bucketSort(@NotNull int[] arr) {
        int n = arr.length;
        //桶代表一定范围,将数放入对应桶中,同时插入排序,最后按顺序遍历桶取出
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int k : arr) {
            min = Math.min(min, k);
            max = Math.max(max, k);
        }
        int m = (max - min) / 10 + 1;//桶的数量
        int[][] bucket = new int[m][n];//存储属于某个范围的数
        int[] idx = new int[m];//桶的添加索引
        for (int k : arr) {
            int num = (k - min) / 10;
            int end = idx[num]++ - 1;
            for (; end >= 0 && bucket[num][end] > k; end--) {
                bucket[num][end + 1] = bucket[num][end];
            }
            bucket[num][end + 1] = k;
        }
        int next = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < idx[i]; j++) {
                arr[next++] = bucket[i][j];
            }
        }
    }

    //---------------------heapSort-------------

    /**
     * 堆排序(不稳定)
     * <p>从小到大</p>
     * <p>O(n log₂n)</p>
     *
     * @param arr 数组
     */
    public static void heapSort(@NotNull int[] arr) {
        int n = arr.length;
        //从最后一个节点的父节点开始,将整个数组调整为大堆
        for (int i = (n - 1 - 1) / 2; i >= 0; i--) {
            adjustDown(arr, n - 1, i);
        }
        int end = n - 1;
        //将最大值放在最后,最小值放在堆顶,再进行调整
        while (end > 0) {
            int tmp = arr[0];
            arr[0] = arr[end];
            arr[end--] = tmp;
            adjustDown(arr, end, 0);
        }
    }

    //堆的向下调整
    private static void adjustDown(int[] arr, int n, int root) {
        int parent = root;
        int child = 2 * parent + 1;
        //存在子节点则进行调整
        while (child <= n) {
            //取子节点较大者
            if (child + 1 <= n && arr[child] < arr[child + 1])
                child++;
            //将父子节点中最大者放到根部
            if (arr[child] > arr[parent]) {
                int tmp = arr[child];
                arr[child] = arr[parent];
                arr[parent] = tmp;
                //递归调整
                parent = child;
                child = 2 * parent + 1;
            } else//调整合适
                break;
        }
    }

    /**
     * 获取排序后对应的原数组索引
     *
     * @param arr 原数组数据
     * @return 原数组索引数组
     */
    public static int[] originIndex(@NotNull int[] arr) {
        int n = arr.length;
        int[][] tuple = new int[n][2];
        for (int i = 0; i < n; i++) {
            tuple[i][0] = arr[i];
            tuple[i][1] = i;
        }
        Arrays.sort(tuple, Comparator.comparingInt(a -> a[0]));
        return tuple[1];
    }

    /**
     * 将原数组替换为排序后的索引(离散化,会产生区间合并问题)
     *
     * @param arr 数组
     */
    public static int[][] sortIndex(@NotNull int[] arr) {
        int n = arr.length;
        Set<Integer> s = new TreeSet<>();
        for (int i : arr) {
            s.add(i);
        }
        int[][] ans = {new int[s.size()], new int[n]};
        int cur = 0;
        for (int i : s) {
            ans[0][cur++] = i;
        }
        for (int i = 0; i < n; i++) {
            ans[1][i] = Arrays.binarySearch(ans[0], ans[1][i]);
        }
        return ans;
    }

}
