package top.cutexingluo.tools.utils.se.algo.cpp.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 质数工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/24 14:18
 * @since 1.0.3
 */
public class XTPrime {

    /**
     * 质数判断
     */
    public static boolean isPrime(long n) {
        if (n == 0 || n == 1)
            return false;
        if (n == 2 || n == 3)
            return true;
        if (n % 6 != 1 && n % 6 != 5)
            return false;
        long end = (long) Math.sqrt(n);
        for (long i = 5; i <= end; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
        return true;
    }

    /**
     * 欧拉线性筛求[1,n]的质数
     */
    public static List<Integer> sievePrimes(int n) {
        boolean[] noPrime = new boolean[n + 1];
        List<Integer> primes = new ArrayList<>();//存储质数
        for (int i = 2; i <= n; ++i) {
            if (!noPrime[i])
                primes.add(i);
            for (int j = 0; i * primes.get(j) <= n; j++) {
                noPrime[i * primes.get(j)] = true;
                if (i % primes.get(j) == 0) break;
            }
        }
        return primes;
    }

    /**
     * 筛法求[l,r]的质数
     */
    public static List<Integer> sievePrimes(int l, int r) {
        boolean[] noPrime = new boolean[r - l + 1];
        List<Integer> primes = sievePrimes(50000);
        if (l == 1) noPrime[0] = true;//l==1
        for (int p : primes) {
            if (p > Math.sqrt(r)) break;
            int begin = (int) Math.ceil(l * 1.0 / p);
            if (begin == 1) begin++;//p>l
            for (int i = begin; (long) i * p <= r; i++) {
                noPrime[i * p - l] = true;
            }
        }
        primes.clear();
        for (int i = 0; i < noPrime.length; i++) {
            if (!noPrime[i]) primes.add(i + l);
        }
        return primes;
    }


    /**
     * 线性筛求欧拉函数,即[1,n]中与n互质的个数
     */
    public static int[] euler(int n) {
        boolean[] noPrime = new boolean[n + 1];
        List<Integer> primes = new ArrayList<>();//存储[1,n]的质数
        int[] ans = new int[n + 1];
        ans[1] = 1;
        for (int i = 2; i <= n; ++i) {
            if (!noPrime[i]) {
                primes.add(i);
                ans[i] = i - 1;
            }
            for (int j = 0; i * primes.get(j) <= n; j++) {
                int tmp = i * primes.get(j);
                noPrime[tmp] = true;
                if (i % primes.get(j) == 0) {
                    ans[tmp] = ans[i] * primes.get(j);
                    break;
                }
                ans[tmp] = ans[i] * (primes.get(j) - 1);
            }
        }
        return ans;
    }

    /**
     * 分解质因数,及其对应数量
     */
    public static Map<Long, Long> primeFac(long a, long b) {
        Map<Long, Long> ans = new HashMap<>();
        for (long i = 2; i * i <= a; i++) {
            if (a % i != 0) continue;
            long num = 0;
            while (a % i == 0) {
                a /= i;
                num += b;
            }
            ans.put(i, num);
        }
        if (a > 1) ans.put(a, b);//最后一个质因数
        return ans;
    }

    /**
     * 预处理[1,n]约数个数
     */
    public static int[] preNumFac(int n) {
        boolean[] noPrime = new boolean[n + 1];
        List<Integer> primes = new ArrayList<>();//存储质数
        int[] ans = new int[n + 1];//约数个数
        int[] num = new int[n + 1];//最小质因子个数
        ans[1] = 1;
        for (int i = 2; i <= n; ++i) {
            if (!noPrime[i]) {
                primes.add(i);
                ans[i] = 2;
                num[i] = 1;
            }
            for (int j = 0; j < primes.size() && i * primes.get(j) <= n; j++) {
                int tmp = i * primes.get(j);
                noPrime[tmp] = true;
                if (i % primes.get(j) == 0) {
                    num[tmp] = num[i] + 1;
                    ans[tmp] = ans[i] / (tmp) * (num[tmp] + 1);
                    break;
                }
                ans[tmp] = ans[i] * 2;
                num[tmp] = 1;
            }
        }
        return ans;
    }

    /**
     * 预处理[1,n]约数和
     */
    public static long[] preSumFac(int n) {
        boolean[] noPrime = new boolean[n + 1];
        List<Integer> primes = new ArrayList<>();//存储质数
        long[] ans = new long[n + 1];
        long[] sum = new long[n + 1];//sum(p^0+...+p^ci),其中p为最小质因子
        ans[1] = 1;
        sum[1] = 1;
        for (int i = 2; i <= n; ++i) {
            if (!noPrime[i]) {
                primes.add(i);
                ans[i] = i + 1;
                sum[i] = i + 1;
            }
            for (int j = 0; j < primes.size() && i * primes.get(j) <= n; j++) {
                int tmp = i * primes.get(j);
                noPrime[tmp] = true;
                if (i % primes.get(j) == 0) {
                    sum[tmp] = sum[i] * primes.get(j) + 1;
                    ans[tmp] = ans[i] / sum[i] * sum[tmp];
                    break;
                }
                ans[tmp] = ans[i] * ans[primes.get(j)];
                sum[tmp] = primes.get(j) + 1;
            }
        }
        return ans;
    }


}
