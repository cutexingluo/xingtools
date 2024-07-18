package top.cutexingluo.tools.utils.se.algo.cpp.structure.tree;

import top.cutexingluo.tools.utils.se.algo.cpp.common.AlgoUtil;

/**
 * Binary Indexed Tree
 * <p>树状数组</p>
 * <p>可修区间前缀和</p>
 * <p>下标从1开始</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 12:23
 * @since 1.0.3
 */
public class BITree {
    private long[] tree;
    private int n;

    public BITree(int size) {
        n = size;
        tree = new long[n + 1];
    }

    protected boolean checkOutBounds(int index) {
        if (index < 1 || index > n) return true;
        return false;
    }

    /**
     * @param num 数字
     * @return 返回值为二进制下num从左往右第一个1的位置
     */
    public static int lowbit(int num) {
        return num & -num;//返回值为二进制下num从左往右第一个1的位置
    }

    /**
     * 添加数据
     * <p>单点修改</p>
     * <p>需要从index=1开始</p>
     */
    public void add(int s, long num) {
        if (AlgoUtil.checkOutBounds(s, n)) return;
        for (int i = s; i <= n; i += lowbit(i)) tree[i] += num;//差分的思想
    }

    /**
     * 查询数据
     * <p>单点查询</p>
     * <p>需要从index=1开始</p>
     */
    public long query(int s) {
        if (AlgoUtil.checkOutBounds(s, n)) return 0;
        long ans = 0;
        for (int i = s; i >= 1; i -= lowbit(i)) ans += tree[i];//寻找差分的标记
        return ans;
    }

    /**
     * 区间修改
     * <p>需要从index=1开始</p>
     *
     * @param l   左边界, inclusive
     * @param r   右边界, inclusive
     * @param val 数据
     */
    public void add(int l, int r, long val) {
        if (AlgoUtil.checkOutBounds(l, n) || AlgoUtil.checkOutBounds(r, n)) return;
        add(l, val);
        add(r + 1, -val);
    }


}
