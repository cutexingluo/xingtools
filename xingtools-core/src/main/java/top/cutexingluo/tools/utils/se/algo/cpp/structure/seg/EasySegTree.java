package top.cutexingluo.tools.utils.se.algo.cpp.structure.seg;

/**
 * 简易线段树
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/25 21:32
 * @since 1.0.3
 */
public class EasySegTree {
    private int[] tree;//线段树,表示某节点的值
    private int[] lazy;//懒标记
    private int N;//元素数量


    /**
     * 创建立刻建树
     */
    public EasySegTree(int[] nums) {
        this.N = nums.length;
        this.tree = new int[N * 4 + 1];//最多4N个节点,0位置不用
        this.lazy = new int[N * 4 + 1];
        build(1, N, 1, nums);
    }

    //在[l,r]的区间递归建树,当前节点为node
    private void build(int l, int r, int node, int[] nums) {
        //区间只有一个元素
        if (l == r) {
            tree[node] = nums[l - 1];
        } else {
            int mid = (l + r) >> 1;
            //分别建左右子树
            build(l, mid, node * 2, nums);
            build(mid + 1, r, node * 2 + 1, nums);
            tree[node] = tree[node * 2] + tree[node * 2 + 1];//更新当前节点
        }
    }

    //区间修改(增加)

    /**
     * 区间修改(增加)
     */
    public void update(int l, int r, int val) {
        if (l > r) {
            return;
        }
        update(l + 1, r + 1, 1, N, 1, val);
    }

    private void update(int l, int r, int curl, int curr, int node, int val) {
        //区间无交集
        if (curl > r || curr < l) {
            return;
        }
        //当前区间在目标区间内
        if (l <= curl && curr <= r) {
            tree[node] += (curr - curl + 1) * val;//更新当前节点
            //不为叶子,加上懒标记
            if (curl < curr) {
                lazy[node] += val;
            }
        } else {//当前区间与目标区间存在交集
            int mid = (curr + curl) >> 1;
            if (lazy[node] != 0)//将标记向下传递一层
            {
                pushDown(node, curr - curl + 1);
            }
            //分割区间,分别处理
            update(l, r, curl, mid, node * 2, val);
            update(l, r, mid + 1, curr, node * 2 + 1, val);
            tree[node] = tree[node * 2] + tree[node * 2 + 1];//更新节点
        }
    }

    //将标记向下一层传递
    private void pushDown(int node, int len) {
        lazy[node * 2] += lazy[node];
        lazy[node * 2 + 1] += lazy[node];
        tree[node * 2] += (len - len / 2) * lazy[node];
        tree[node * 2 + 1] += len / 2 * lazy[node];
        lazy[node] = 0;
    }

    //区间查询

    /**
     * 区间查询
     */
    public int query(int l, int r) {
        if (l > r) {
            return -1;
        }
        return query(l + 1, r + 1, 1, N, 1);
    }

    private int query(int l, int r, int curl, int curr, int node) {
        //不在目标区间
        if (curl > r || curr < l) {
            return 0;
        }
        //在目标区间
        if (l <= curl && curr <= r) {
            return tree[node];
        }
        //部分有交集,分割区间处理
        int mid = (curl + curr) >> 1;
        if (lazy[node] != 0) {
            pushDown(node, curr - curl + 1);
        }
        return query(l, r, curl, mid, node * 2) + query(l, r, mid + 1, curr, node * 2 + 1);
    }
}
