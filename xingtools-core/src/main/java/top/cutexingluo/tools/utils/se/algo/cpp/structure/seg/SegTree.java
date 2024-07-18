package top.cutexingluo.tools.utils.se.algo.cpp.structure.seg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线段树-标准版
 * <p>需要自行调用 build() 创建</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/25 21:09
 * @since 1.0.3
 */
public class SegTree {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Node {
        /**
         * 左右端点
         */
        private int l, r;
        /**
         * 懒标记
         */
        private long lazy;
        /**
         * 权值，计数
         */
        private long w, cnt;//cnt计数法,pushdown可以不用 计算区间m-l+1,r-m
    }


    /**
     * 数据
     */
    private long[] a;

    private int n;

    public int getSize() {
        return n;
    }

    /**
     * 线段树
     */
    private Node[] tree;

    /**
     * 创建线段树对象
     *
     * @param data 数据，需要从index=1开始
     */
    public SegTree(long[] data) {
        this(data, data.length - 1);
    }

    /**
     * 创建线段树对象
     *
     * @param data  数据，需要从index=1开始
     * @param total 数据量大小
     */
    public SegTree(long[] data, int total) {
        n = total;
        a = data;
        tree = new Node[a.length << 2 + 1];
    }

    protected int ls(int u) {
        return u << 1;
    }

    protected int rs(int u) {
        return u << 1 | 1;
    }

    protected void pushup(int u) {//向上合并子结点值
        tree[u].w = tree[ls(u)].w + tree[rs(u)].w;
    }

    protected void pushdown(int u) {//向下分配懒标记和值
        tree[ls(u)].lazy += tree[u].lazy;
        tree[rs(u)].lazy += tree[u].lazy;
        tree[ls(u)].w += tree[ls(u)].cnt * tree[u].lazy;
        tree[rs(u)].w += tree[rs(u)].cnt * tree[u].lazy;
        tree[u].lazy = 0;
    }

    /**
     * 新建
     *
     * @param u 根节点
     * @param l 左边边界
     * @param r 右边边界
     */
    public SegTree build(int u, int l, int r) {
        tree[u].l = l;
        tree[u].r = r;
        if (tree[u].l == tree[u].r) {
            tree[u].w = a[l];
            tree[u].cnt = 1;
            return this;
        }
        int mid = l + r >> 1;
        build(ls(u), l, mid);
        build(rs(u), mid + 1, r);
        tree[u].cnt = tree[ls(u)].cnt + tree[rs(u)].cnt;//儿子数
        pushup(u);
        return this;
    }

    /**
     * 新建
     *
     * @param l 左边边界
     * @param r 右边边界
     */
    public SegTree build(int l, int r) {
        return build(1, l, r);
    }

    /**
     * 新建
     */
    public SegTree build() {
        return build(1, 1, n);
    }

    /**
     * 区间查询
     */
    public long query(int u, int l, int r) {
        if (tree[u].l >= l && tree[u].r <= r) {
            return tree[u].w;
        }
        long sum = 0;
        if (tree[u].lazy != 0) pushdown(u);
        long mid = (tree[u].l + tree[u].r) >> 1;
        if (l <= mid) sum += query(ls(u), l, r);
        if (r > mid) sum += query(rs(u), l, r);
        return sum;
    }

    public long query(int l, int r) {
        return query(1, l, r);
    }

    /**
     * 区间修改
     */
    public void update(int u, int l, int r, long val) {
        if (tree[u].l >= l && tree[u].r <= r) {
            tree[u].lazy += val;
            tree[u].w += tree[u].cnt * val;
            return;
        }
        if (tree[u].lazy != 0) pushdown(u);
        long mid = tree[u].l + tree[u].r >> 1;
        if (l <= mid) update(ls(u), l, r, val);
        if (r > mid) update(rs(u), l, r, val);
        pushup(u);
    }

    /**
     * 区间修改
     */
    public void update(int l, int r, long val) {
        update(1, l, r, val);
    }
}
