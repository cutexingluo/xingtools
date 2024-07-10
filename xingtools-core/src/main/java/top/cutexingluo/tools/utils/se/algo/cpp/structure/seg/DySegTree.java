package top.cutexingluo.tools.utils.se.algo.cpp.structure.seg;

import top.cutexingluo.tools.utils.se.algo.cpp.common.AlgoUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态线段树
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/27 10:41
 * @since 1.0.3
 */
public class DySegTree {
    /**
     * 线段树,表示某节点的值
     */
    protected Map<Integer, Integer> tree;
    /**
     * 懒标记
     */
    protected Map<Integer, Integer> lazy;
    /**
     * 起始点
     */
    protected int begin;
    /**
     * 终止点
     */
    protected int end;

    public DySegTree() {
        tree = new HashMap<>();
        lazy = new HashMap<>();
        begin = 1;
        end = AlgoUtil.INT_MAX;
    }

    /**
     * 区间修改
     */
    public void update(int l, int r, int val) {
        if (l > r) return;
        update(l, r, begin, end, 1, val);
    }

    /**
     * 更新
     */
    protected void update(int l, int r, int curl, int curr, int node, int val) {
        if (curl > r || curr < l)
            return;
        if (l <= curl && curr <= r) {
            tree.put(node, tree.getOrDefault(node, 0) + (curr - curl + 1) * val);
            if (curl < curr) lazy.put(node, lazy.getOrDefault(node, 0) + val);
        } else {
            int mid = (curl + curr) >> 1;
            if (lazy.getOrDefault(node, 0) != 0)
                pushDown(node, curr - curl + 1);
            //更新子区间
            update(l, r, curl, mid, 2 * node, val);
            update(l, r, mid + 1, curr, 2 * node + 1, val);
            pushUp(node);//更新当前节点
        }
    }

    protected void pushUp(int node) {
        tree.put(node, tree.getOrDefault(2 * node, 0) + tree.getOrDefault(2 * node + 1, 0));
    }

    /**
     * 将标记向下一层传递
     */
    protected void pushDown(int node, int len) {
        int _lazy = lazy.get(node);
        lazy.put(node * 2, lazy.getOrDefault(node * 2, 0) + _lazy);
        lazy.put(node * 2 + 1, lazy.getOrDefault(node * 2 + 1, 0) + _lazy);
        tree.put(node * 2, tree.getOrDefault(node * 2, 0) + (len - len / 2) * _lazy);
        tree.put(node * 2 + 1, tree.getOrDefault(node * 2 + 1, 0) + len / 2 * _lazy);
        lazy.put(node, 0);
    }

    /**
     * 区间查询
     */
    public int query(int l, int r) {
        if (l > r) return -1;
        return query(l, r, begin, end, 1);
    }

    /**
     * 询问
     */
    protected int query(int l, int r, int curl, int curr, int node) {
        if (curl > r || curr < l)
            return 0;
        if (l <= curl && curr <= r)
            return tree.getOrDefault(node, 0);
        int mid = (curl + curr) >> 1;
        if (lazy.getOrDefault(node, 0) != 0)
            pushDown(node, curr - curl + 1);
        return query(l, r, curl, mid, node * 2) + query(l, r, mid + 1, curr, node * 2 + 1);
    }
}
