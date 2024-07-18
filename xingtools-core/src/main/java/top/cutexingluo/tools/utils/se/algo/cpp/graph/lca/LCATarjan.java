package top.cutexingluo.tools.utils.se.algo.cpp.graph.lca;

import top.cutexingluo.tools.utils.se.algo.cpp.common.AlgoUtil;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.GNode;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.GraphNode;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.base.DisjointSetUnion;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.base.GraphNodeHandler;

import java.util.Arrays;

/**
 * Recent Common Ancestor
 * <p>最近公共祖先</p>
 * <p>tarjan离线并查集法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 17:05
 * @since 1.0.3
 */
public class LCATarjan extends GraphNodeHandler<Integer> implements DisjointSetUnion {
    /**
     * 离线队列
     */
    protected GraphNode<GNode>[] q;
    /**
     * vis
     */
    protected boolean[] vis;
    /**
     * 并查集
     */
    protected int[] f;
    /**
     * 父亲
     */
    protected int[] fa;
    /**
     * 结果
     */
    protected int[] ans;

    /**
     * @param maxId @param maxId 从1开始的最大id, 也就是序列化后的节点数量
     */
    public LCATarjan(int maxId) {
        super(maxId);
        this.q = (GraphNode<GNode>[]) new GraphNode[n + 1];
        Arrays.fill(q, new GraphNode<>()); // 全部赋初值
        this.vis = new boolean[n + 1];
        this.f = new int[n + 1];
        this.fa = new int[n + 1];
        this.ans = new int[n + 1];
    }

    @Override
    public void clear() {
        super.clear();
        Arrays.fill(q, new GraphNode<>()); // 全部赋初值
    }

    @Override
    public void addEdge(int u, int v) {
        if (AlgoUtil.checkOutBounds(u, n) || AlgoUtil.checkOutBounds(v, n)) return;
        super.addEdge(u, v);
        G[u].add(v);
    }

    /**
     * 添加询问，执行build后可以答案
     * <p>离线存储</p>
     *
     * @param u     一个点
     * @param v     另一个点
     * @param index 问题标号的索引，如果是双向边，索引应该一样
     */
    public void addQuery(int u, int v, int index) {
        q[u].add(new GNode(v, index));
    }


    protected int find(int x) {
        return find(f, x);
    }

    protected void merge(int x, int y) {
        unionMerge(f, x, y);
    }

    protected void init() {
        for (int i = 1; i <= n; i++) {
            f[i] = i;
            fa[i] = i;
        }
    }

    protected void tarjan(int x) {
        for (int v : G[x]) { //G[x]
            if (vis[v]) continue;
            if (v == fa[x]) continue;
            fa[v] = x;
            tarjan(v);
            merge(x, v);
        }
        vis[x] = true;
        for (GNode t : q[x]) {
            int v = t.to(), i = (int) t.w();
            if (vis[v] && ans[i] != 0) {
                ans[i] = find(v);
            }
        }
    }

    /**
     * 开始创建结果
     *
     * @param root 根节点
     * @return 所有query的答案
     */
    public int[] build(int root) {
        init();
        tarjan(root);
        return ans;
    }

    /**
     * 开始创建结果
     * <p>根节点默认为1</p>
     *
     * @return 所有query的答案
     */
    public int[] build() {
        return build(1);
    }

}
