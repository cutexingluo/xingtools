package top.cutexingluo.tools.utils.se.algo.cpp.math;

/**
 * 位运算
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/24 14:59
 * @since 1.0.3
 */
public class Bit {
    //性质:x^x=0,x^0=x,x&(x-1)消除最后一位1,x&-x保留最后一位1;

    public static int getBit(int a, int b) {
        return (a >> b) & 1;
    }

    public static int set0(int a, int b) {
        return a & ~(1 << b);
    }

    public static int set1(int a, int b) {
        return a | (1 << b);
    }

    public static int invert(int a, int b) {
        return a ^ (1 << b);
    }


    /**
     * 找出出现少于k次的数,其他出现k次
     */
    public static int getLessK(int[] arr, int k) {
        int n = arr.length;
        int[] bit = new int[32];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 32; j++) {
                bit[j] += (arr[i] >> j) & 1;//获取每一位
                bit[j] %= k;
            }
        }
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            if (bit[i] != 0)
                ans += Math.pow(2, i);
        }
        return ans;
    }

    /**
     * 找出两个出现一次的数,其他出现2次
     */
    public static int[] getOnce(int[] arr) {
        int n = arr.length;
        int tmp = 0;
        for (int i = 0; i < n; i++) {
            tmp ^= arr[i];
        }
        int last = (int) (Math.log(tmp & -tmp) / Math.log(2));//最后一位1的索引
        int[] ans = new int[2];
        for (int i = 0; i < n; i++) {
            if (((arr[i] >> last) & 1) == 1) {
                ans[0] ^= arr[i];
            } else
                ans[1] ^= arr[i];
        }
        return ans;
    }
}
