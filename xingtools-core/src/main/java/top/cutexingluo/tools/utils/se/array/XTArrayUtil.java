package top.cutexingluo.tools.utils.se.array;


import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.se.map.XTMapUtil;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * 简单数组工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 18:11
 */
public class XTArrayUtil {

    /**
     * 创建泛型数组
     *
     * @since 1.0.3
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(Class<T> type, int size) {
        return (T[]) Array.newInstance(type, size);
    }

    /**
     * 数组元素移动
     * <p>超出的部分将被舍弃, 其余空出部分用 null 代替</p>
     * <p>如果想覆盖为0, 使用 toNoBoxed 拆包即可</p>
     *
     * @param array  原数组, 为空则不移动
     * @param offset 偏移量, 正数向右移动, 负数向左移动
     * @since 1.0.3
     */
    public static <T> void movePos(@NotNull T[] array, int offset) {
        Objects.requireNonNull(array, "array should not be null");
        if (array.length == 0 || offset == 0) {
            return;
        }
        if (offset < 0) { // 左移
            offset = -offset;
            int len = array.length - offset;
            int srcPos = Math.min(offset, array.length);
            int copyLen = Math.max(0, len);
            System.arraycopy(array, srcPos, array, 0, copyLen);
            Arrays.fill(array, copyLen, array.length, null);
        } else { // 右移
            int len = array.length - offset;
            int tarPos = Math.min(offset, array.length);
            int copyLen = Math.max(0, len);
            System.arraycopy(array, 0, array, tarPos, copyLen);
            Arrays.fill(array, 0, tarPos, null);
        }
    }


    /**
     * 数组元素移动,用新数组封装
     * <p>超出的部分将被舍弃</p>
     *
     * @param array    原数组, 为空则不赋值到newArray
     * @param offset   偏移量, 正数向右移动, 负数向左移动
     * @param newArray 大小自定
     * @return newArray 和上面的参数一样，返回newArray
     * @since 1.0.3
     */
    public static <T> T[] movePos(@NotNull T[] array, int offset, @NotNull T[] newArray) {
        Objects.requireNonNull(array, "array should not be null");
        Objects.requireNonNull(newArray, "array should not be null");
        if (array.length == 0) {
            return newArray;
        }
        if (offset == 0) {
            // 复制数组
            System.arraycopy(array, 0, newArray, 0, Math.min(array.length, newArray.length));
        } else if (offset < 0) {
            offset = -offset;
            int len = array.length - offset;
            if (len < newArray.length) {
                int srcPos = Math.min(offset, array.length);
                int copyLen = Math.max(0, len);
                System.arraycopy(array, srcPos, newArray, 0, copyLen);
            } else {
                System.arraycopy(array, offset, newArray, 0, newArray.length);
            }
        } else {
            int len = offset + array.length;
            if (len > newArray.length) {
                int tarPos = Math.min(offset, newArray.length);
                int copyLen = Math.max(0, newArray.length - offset);
                System.arraycopy(array, 0, newArray, tarPos, copyLen);
            } else {
                System.arraycopy(array, 0, newArray, offset, array.length);
            }
        }
        return newArray;
    }

    /**
     * 数组元素环形移动
     * <p>两数组大小需要一样</p>
     * <p>环形移动，移出范围的数据会在另一端出现</p>
     *
     * @param array    原数组, 为空则不赋值到newArray
     * @param offset   偏移量, 正数向右循环移动, 负数向左循环移动
     * @param newArray 应该为array大小的空数组
     * @return newArray 和上面的参数一样，返回newArray
     * @since 1.0.3
     */
    public static <T> T[] movePosCycle(@NotNull T[] array, int offset, @NotNull T[] newArray) {
        Objects.requireNonNull(array, "array should not be null");
        Objects.requireNonNull(newArray, "array should not be null");
        if (array.length == 0) {
            return newArray;
        }
        if (offset % array.length == 0) {
            // 复制数组
            System.arraycopy(array, 0, newArray, 0, array.length);
        } else if (offset < 0) {
            offset = -offset % array.length;
            System.arraycopy(array, offset, newArray, 0, array.length - offset);
            System.arraycopy(array, 0, newArray, array.length - offset, offset);
        } else {
            offset = offset % array.length;
            System.arraycopy(array, 0, newArray, offset, array.length - offset);
            System.arraycopy(array, array.length - offset, newArray, 0, offset);
        }
        return newArray;
    }

    /**
     * 逐行输出数据
     */
    public static <T> void printlnArray(T[] array) {
        for (T item : array) {
            System.out.println(item);
        }
    }

    /**
     * 逐行输出数据
     */
    public static <T> void printlnList(List<T> list) {
        for (T item : list) {
            System.out.println(item);
        }
    }

    /**
     * 逐行输出数据
     *
     * @since 1.0.4
     */
    public static <T> void println(Iterable<T> iterable) {
        for (T item : iterable) {
            System.out.println(item);
        }
    }

    /**
     * 逐行输出数据
     *
     * @since 1.0.4
     */
    public static <T> void println(Enumeration<T> enumeration) {
        while (enumeration.hasMoreElements()) {
            System.out.println(enumeration.nextElement());
        }
    }

    /**
     * 去重
     */
    public static <T> List<T> distinct(List<T> array) {
        return array.stream().distinct().collect(Collectors.toList());
    }


    /**
     * 把values按K，V形式放入
     */
    public static <K, V> void putMapFromDValues(@NotNull Map<K, V> map, Object... values) {
        XTMapUtil.putMapEntriesFromDValues(map, values);
    }

    //***********************转化类型***********************

    /**
     * [] 数组转为 List
     */
    public static List<Integer> toList(int[] array) {
        return Arrays.stream(array).boxed().collect(Collectors.toList());
    }

    /**
     * [] 数组转为 List
     */
    public static List<Long> toList(long[] array) {
        return Arrays.stream(array).boxed().collect(Collectors.toList());
    }

    /**
     * [] 数组转为 List
     */
    public static List<Double> toList(double[] array) {
        return Arrays.stream(array).boxed().collect(Collectors.toList());
    }

    //------------------------------------


    /**
     * int[] 转为 包装类
     */
    public static Integer[] toBoxed(int[] array) {
        return Arrays.stream(array).boxed().toArray(Integer[]::new);
    }

    /**
     * long[] 转为 包装类
     */
    public static Long[] toBoxed(long[] array) {
        return Arrays.stream(array).boxed().toArray(Long[]::new);
    }

    /**
     * double[] 转为 包装类
     */
    public static Double[] toBoxed(double[] array) {
        return Arrays.stream(array).boxed().toArray(Double[]::new);
    }
    //------------------------

    /**
     * 拆除 包装类
     */
    public static int[] toNoBoxed(Integer[] array) {
        return Arrays.stream(array).mapToInt(a -> a == null ? 0 : a).toArray();
    }

    /**
     * 拆除 包装类
     */
    public static long[] toNoBoxed(Long[] array) {
        return Arrays.stream(array).mapToLong(a -> a == null ? 0 : a).toArray();
    }

    /**
     * 拆除 包装类
     */
    public static double[] toNoBoxed(Double[] array) {
        return Arrays.stream(array).flatMapToDouble(a -> DoubleStream.of(a == null ? 0 : a)).toArray();
    }


}
