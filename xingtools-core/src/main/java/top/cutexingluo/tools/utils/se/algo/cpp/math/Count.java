package top.cutexingluo.tools.utils.se.algo.cpp.math;

/**
 * 排列组合计数工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/24 15:21
 * @since 1.0.3
 */
public class Count {
    //二项式定理:pow(a+b,n)=sum(C(n,i)*pow(a,i)*pow(b,n-i)),其中i取[0,n]
    //卢卡斯定理:若p为质数,则对于任意整数1<=m<=n,C(n,m)=C(n%p,m%p)*C(n/p,m/p) (%p)
    //求sum(xi)=n的正整数解的组数:C(n-1,k-1); 求sum(xi)=n的自然数解的组数:C(n+k-1,k-1)


    /**
     * 组合数
     *
     * @param n   分子
     * @param m   分母
     * @param mod 模数
     * @return C(n, m)
     */
    //组合数
    public static long C(long n, long m, int mod) {
        if (n >= m && m >= 0) {
            if (n == m) return 1;
            m = Math.min(m, n - m);
            long ans = 1;//分子分母
            for (int i = 1; i <= m; i++) {
                ans *= (n - m + i);
                ans = ans / i % mod;
            }
            return ans;
        } else return 0;
    }

    /**
     * 组合数 (lucas)
     *
     * @param n   分子
     * @param m   分母
     * @param mod 模数
     * @return C(n, m)
     */
    //lucas定理求组合数
    public static long lucas(long n, long m, int mod) {
        if (n < mod && m < mod) return C(n, m, mod);
        return C(n % mod, m % mod, mod) * lucas(n / mod, m / mod, mod) % mod;
    }

    /**
     * 组合数 (预处理)
     *
     * @param n   分子
     * @param mod 模数
     * @return C(0, 0) -> C(n,n)
     */
    //C(0,0)到C(n,n)的组合数
    public static long[][] preC(int n, int mod) {
        long[][] ans = new long[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            ans[i][0] = 1;
            for (int j = 1; j <= i; j++) {
                ans[i][j] = (ans[i - 1][j] + ans[i - 1][j - 1]) % mod;
            }
        }
        return ans;
    }

    /**
     * 排列数
     *
     * @param n   分子
     * @param m   分母
     * @param mod 模数
     * @return A(n, m)
     */
    //排列数
    public static long A(long n, long m, int mod) {
        if (n >= m && m >= 0) {
            long ans = 1;
            for (int i = 0; i < m; i++, n--) {
                ans *= n;
                ans %= mod;
            }
            return ans;
        } else return 0;
    }

    /**
     * 斐波那契数列
     *
     * @return 第n位置的数
     */
    //斐波那契数列
    public static long fib(long n, int mod) {
        if (n < 1)
            return -1;
        if (n == 1 || n == 2)
            return 1;
        long a = 1, b = 1, c = 0;
        for (int i = 0; i < n - 2; i++) {
            c = (a + b) % mod;
            a = b % mod;
            b = c % mod;
        }
        return c;
    }
}
