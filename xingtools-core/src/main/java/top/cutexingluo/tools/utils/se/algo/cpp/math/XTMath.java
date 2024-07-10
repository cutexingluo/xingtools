package top.cutexingluo.tools.utils.se.algo.cpp.math;

import java.math.BigInteger;
import java.util.*;

/**
 * 数学工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/24 13:26
 * @since 1.0.3
 */
public class XTMath {
    //公式 9223372036854775783L
    //等差数列:an=a1+(n-1)*d; 前n项和:Sn=a1*n+n*(n-1)*d/2=(a1+an)*n/2
    //等比数列:an=a1*q^(n-1); 前n项和:q!=1 -> Sn=a1*(q^n-1)/(q-1)=(an*q-a1)/(q-1); q=1 -> Sn=n*a1
    //平方和:n*(n+1)*(2*n+1)/6; 立方和:n^2*(n+1)^2/4
    //贝祖定理:若a,b互质,则a*x+b*y=c有无穷组整数解,x=b*k+x0,y=-a*k+y0;  扩展:若a,b互质,则两者无法组成的最大数c=a*b-a-b,可组成数与不可组成数在区间[0,c]对称
    //欧拉定理:若a,b互质,则a^f(b)=1(%b),欧拉函数f(b)=b*mul(1-1/p),其中p为b的质因数
    //费马小定理:若a,b互质,且b为质数,则a^(b-1)=1(%b),即a的逆元为a^(b-2)%b
    //约数定理:根据唯一分解定理,如果x=mul(pi^ci),则约数个数为mul(ci+1),约数之和为mul(pi^0+...+pi^ci)

    public static double eps = 1e-10;
    public static double pi = Math.PI;

    /**
     * @return 小于-1，等于0，大于1
     */
    public static int sgn(double value) {
        return value < -eps ? -1 : (value > eps ? 1 : 0);
    }

    /**
     * 二分查找
     */
    public static <T extends Number> int binarySearch(T[] a, int fromIndex, int toIndex, T key) {
        return Arrays.binarySearch(a, fromIndex, toIndex, key);
    }

    /**
     * 二分查找
     */
    public static <T extends Number> int binarySearch(T[] a, T key) {
        return Arrays.binarySearch(a, key);
    }


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
        int l = start, r = end;
        while (l < r) {
            int mid = l + (r - l >> 1);
            if (arr[mid] >= tar) r = mid;
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
     * @return index 下标
     */
    public static <T extends Comparable<T>> int lowerBound(T[] arr, int start, int end, T tar, Comparator<T> c) {
        int l = start, r = end;
        while (l < r) {
            int mid = l + (r - l >> 1);
            if (c.compare(arr[mid], tar) >= 0) r = mid;
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
        int l = start, r = end;
        while (l < r) {
            int mid = l + (r - l >> 1);
            if (arr[mid] > tar) r = mid;
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
        int l = start, r = end;
        while (l < r) {
            int mid = l + (r - l >> 1);
            if (c.compare(arr[mid], tar) > 0) r = mid;
            else l = mid + 1;
        }
        return l;
    }

    //寻找满足>tar的最小值或<=tar的最大值的参数

    /**
     * 寻找满足>tar的最小值或<=tar的最大值的参数
     * <p>大于tar的最小位置</p>
     *
     * @return 大于tar的最小位置
     */
    public static long minGreater(long[] arr, long tar) {
        int l = -1, r = arr.length;
        //l与r相邻时,l指向<=tar的最大值,r指向>tar的最小值
        while (l + 1 < r) {
            int mid = l + (r - l >> 1);
            //如果mid增大,f(mid)减小,需要交换l和r
            if (arr[mid] <= tar) l = mid;//如果f(mid)<=tar,则可能为<tar的最大值,或者=tar
            else r = mid;//如果f(mid)>tar,则可能为>tar的最小值
        }
        return r;
    }

    //寻找满足<tar的最大值或>=tar的最小值的参数

    /**
     * 寻找满足&lt;tar的最大值或>=tar的最小值的参数
     * <p>小于tar的最大位置</p>
     *
     * @return 小于tar的最大位置
     */
    public static long maxLess(long[] arr, long tar) {
        int l = -1, r = arr.length;
        //l与r相邻时,l指向<tar的最大值,r指向>=tar的最小值
        while (l + 1 < r) {
            int mid = l + (r - l >> 1);
            if (arr[mid] < tar) l = mid;//如果f(mid)<tar,则可能为<tar的最大值
            else r = mid;//如果f(mid)>=tar,则可能为>tar的最小值,或者=tar
        }
        return l;
    }

    /**
     * 快速幂
     *
     * @param a   数据
     * @param n   指数
     * @param mod 模数
     * @return {@link T} 结果
     */
    public static <T extends Number> T quickPow(T a, int n, long mod) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(n);
        if (a instanceof Float || a instanceof Double) {
            Double ans = 1.0, base = a.doubleValue();
            while (n > 0) {
                if ((n & 1) == 1)
                    ans = ans * base % mod;
                base = base * base % mod;
                n >>= 1;
            }
            return (T) ans;
        } else {
            Long ans = 1L, base = a.longValue();
            while (n > 0) {
                if ((n & 1) == 1)
                    ans = ans * base % mod;
                base = base * base % mod;
                n >>= 1;
            }
            return (T) ans;
        }
    }

    /**
     * 快速幂, 模数为 1_000_000_007
     *
     * @param a 数据
     * @param n 指数
     */
    public static long quickPow(long a, int n) {
        return quickPow(a, n, 1_000_000_007L);
    }

    /**
     * 阶乘 n!
     *
     * @param n   目标
     * @param mod 模数
     * @return n!
     */
    public static long fac(long n, int mod) {
        if (n == 0)
            return 1;
        return n % mod * fac(n - 1, mod) % mod;
    }

    /**
     * [0,n]的阶乘及其逆元
     *
     * @param n   目标
     * @param mod 模数
     * @return long [2][n+1]
     */
    public static long[][] preFac(int n, int mod) {
        long[][] ans = new long[2][n + 1];
        ans[0][0] = ans[1][0] = 1;
        for (int i = 1; i <= n; i++) {
            ans[0][i] = ans[0][i - 1] * i % mod;
//            ans[1][i] = ans[1][i - 1] * inv(i, mod) % mod;
        }
        ans[1][n] = quickPow(n, mod - 2, mod);
        for (int i = n; i > 1; i--) {
            ans[1][i - 1] = ans[1][i] * i % mod;
        }
        return ans;
    }

    /**
     * 获取sg 数组
     *
     * @param f 原数组(需要从index 为1开始)
     * @param n 数组大小
     * @return {@link int[]}
     */
    public static int[] getSG(int[] f, int n) {
        Arrays.sort(f, 1, n + 1);
        boolean[] vis = new boolean[n + 1];
        int[] sg = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            Arrays.fill(vis, false);
            for (int j = 1; f[j] <= i; j++)//f排序是为了让每一种取法都循环到
                vis[sg[i - f[j]]] = true;
            for (int j = 0; j <= n; j++) {
                if (!vis[j]) {
                    sg[i] = j;
                    break;
                }
            }
        }
        return sg;
    }

    /**
     * gcd ， 最大公约数
     *
     * @param a 数据
     * @param b 数据
     * @return {@link BigInteger}
     */
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        return a.gcd(b);
    }

    /**
     * lcm ， 最小公倍数
     *
     * @param a 数据
     * @param b 数据
     * @return {@link BigInteger}
     */
    public static BigInteger lcm(BigInteger a, BigInteger b) {
        return a.divide(a.gcd(b).multiply(b));
    }

    /**
     * gcd ， 最大公约数
     *
     * @param a 数据
     * @param b 数据
     * @return {@link BigInteger}
     */
    public static long gcd(long a, long b) {
        if (b == 0) return a > 0 ? a : -a;
        return gcd(b, a % b);
    }

    /**
     * lcm ， 最小公倍数
     *
     * @param a 数据
     * @param b 数据
     * @return {@link BigInteger}
     */
    public static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    /**
     * lcm ， 最小公倍数
     *
     * @param a   数据
     * @param b   数据
     * @param mod 模数
     * @return {@link BigInteger}
     */
    public static long lcm(long a, long b, int mod) {
        a /= gcd(a, b);
        return a % mod * (b % mod) % mod;
    }

    /**
     * log2向下取整
     */
    public static int log2(long n) {
        return Long.bitCount(Long.highestOneBit(n) - 1);
    }

    /**
     * 求a*x+b*y=c或者同余方程a*x=c(%b)的特解(a,b互质)
     */
    public static long[] exgcd(long a, long b, long c) {
        if (b == 0)
            return a > 0 ? new long[]{c, 0} : new long[]{-c, 0};
        long[] ans = exgcd(b, a % b, c);
        long tmp = ans[0];
        ans[0] = ans[1];
        ans[1] = (tmp - a / b * ans[1]);
        return ans;
    }

    /**
     * 同余方程组x=a(%b)的通解,通解的模为lcm(b1,...,bn)
     */
    public static long[] crt(long[] a, long[] b) {
        int n = a.length;
        long[] ans = new long[]{a[0] % b[0], b[0]};
        for (int i = 1; i < n; i++) {
            long A = ans[1];
            long B = b[i];
            long C = (a[i] - ans[0]) % B;
            long gcd = gcd(A, B);
            if (C % gcd != 0)
                return null;//无解
            A /= gcd;
            B /= gcd;
            C /= gcd;
            long x = exgcd(A, B, C)[0] % B;
            ans[0] += x * ans[1];
            ans[1] *= B;
            ans[0] %= ans[1];
        }
        return ans;
    }

    /**
     * 逆元(mod为质数,n<mod)
     *
     * @param n   目标数
     * @param mod 模
     */
    public static long inv(long n, int mod) {
        n %= mod;
        if (n == 1)
            return 1;
        return (mod - mod / n) * inv(mod % n, mod) % mod;
    }

    /**
     * 预处理[1,n]的逆元(mod为质数,n<mod)
     *
     * @param n   目标数
     * @param mod 模
     */
    public static long[] preInv(int n, int mod) {
        int len = Math.min(n + 1, mod);
        long[] ans = new long[len];
        ans[1] = 1;
        for (int i = 2; i < len; i++) {
            ans[i] = (mod - mod / i) * ans[mod % i] % mod;
        }
        return ans;
    }

    /**
     * 分解因数
     */
    public static List<long[]> fac(long n) {
        List<long[]> ans = new ArrayList<>();
        for (long i = 1; i * i <= n; i++) {
            if (n % i == 0) ans.add(new long[]{i, n / i});
        }
        return ans;
    }


    /**
     * 整数分块,区间内的商相等
     */
    public static List<long[]> partition(long n) {
        List<long[]> ans = new ArrayList<>();
        for (long l = 1, r = 0; l < n; l = r + 1) {
            r = n / (n / l);
            ans.add(new long[]{l, r});
        }
        return ans;
    }

    /**
     * 日期有效判断
     *
     * @param y year
     * @param m month
     * @param d day
     */
    public static boolean dateValid(int y, int m, int d) {
        int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (y % 400 == 0 || y % 100 != 0 && y % 4 == 0)
            days[1]++;
        return m >= 1 && m <= 12 && d >= 1 && d <= days[m - 1];
    }

    /**
     * 一年中的第几天
     *
     * @param y year
     * @param m month
     * @param d day
     * @return 第几天
     */
    public static int dayOfYear(int y, int m, int d) {
        int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (y % 400 == 0 || y % 100 != 0 && y % 4 == 0)
            days[1]++;
        int ans = 0;
        for (int i = 0; i < m - 1; ++i) {
            ans += days[i];
        }
        return ans + d;
    }

    /**
     * 分数计算
     *
     * @param a  x分子
     * @param b  x分母
     * @param c  y分子
     * @param d  y分母
     * @param op 操作符 1+ 2- 3* 4/
     * @return 结果 [0 分子, 1 分母]
     */
    public static long[] fraction(long a, long b, long c, long d, int op) {
        long[] ans = new long[2];
        if (op <= 2) {//加减法
            if (op == 2) c = -c;
            ans[0] = a * d + c * b;
            ans[1] = b * d;
        } else if (op == 3) {//乘法
            ans[0] = a * c;
            ans[1] = b * d;
        } else {//除法
            ans[0] = a * d;
            ans[1] = c * b;
        }
        long gcd = gcd(ans[0], ans[1]);
        ans[0] /= gcd;
        ans[1] /= gcd;
        return ans;
    }

    /**
     * 叉乘
     */
    public static long cross(long[] p, long[] q, long[] r) {
        return (q[0] - p[0]) * (r[1] - q[1]) - (q[1] - p[1]) * (r[0] - q[0]);
    }


}
