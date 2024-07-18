package top.cutexingluo.tools.utils.se.algo.cpp.common;

import org.jetbrains.annotations.NotNull;

/**
 * 算法工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 16:36
 * @since 1.0.3
 */
public class AlgoUtil {

    public static final int INT_MAX = 0x3f3f3f3f;
    public static final long LONG_MAX = 0x3f3f3f3f3f3f3f3fL;

    public static <T> boolean checkOut(@NotNull T w) {
        if (w instanceof Integer) {
            if ((Integer) w > INT_MAX) {
                // 抛出数据过大异常
                throw new IllegalArgumentException("w is out of  INT_MAX ! 数据过大!");
            }
        } else if (w instanceof Long) {
            if ((Long) w > LONG_MAX) {
                throw new IllegalArgumentException("w is  out of  LONG_MAX ! 数据过大!");
            }
        }
        return false;
    }

    public static boolean checkOutBounds(int x, int n) throws ArrayIndexOutOfBoundsException {
        if (checkOutBoundsNoThrow(x, n)) {
            throw new ArrayIndexOutOfBoundsException("index out of bounds ! It should be between 1 and n (both inclusive) !");
        }
        return false;
    }

    public static boolean checkOutBounds(int[] arr, int n) throws ArrayIndexOutOfBoundsException {
        for (int i : arr) {
            if (checkOutBounds(i, n)) return true;
        }
        return false;
    }

    public static boolean checkOutBoundsNoThrow(int x, int n) {
        return x < 1 || x > n;
    }


}
