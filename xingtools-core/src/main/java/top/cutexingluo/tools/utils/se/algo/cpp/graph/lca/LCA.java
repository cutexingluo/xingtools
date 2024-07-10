package top.cutexingluo.tools.utils.se.algo.cpp.graph.lca;

import top.cutexingluo.tools.utils.se.algo.cpp.common.AlgoUtil;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.base.GraphNodeHandler;

/**
 * Recent Common Ancestor
 * <p>最近公共祖先</p>
 * <p>倍增法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 13:25
 * @since 1.0.3
 */
public class LCA extends GraphNodeHandler<Integer> {


    /**
     * 祖父节点
     */
    protected int[][] fa;


    /**
     * 深度数组
     */
    protected int[] depth;

    /**
     * 深度
     */
    protected int dep;

    /**
     * @param maxId 从1开始的最大id, 也就是序列化后的节点数量
     * @param dep   父节点深度
     */
    public LCA(int maxId, int dep) {
        super(maxId);
        this.dep = dep;
        this.fa = new int[n + 1][21];
        depth = new int[dep];
    }

    /**
     * @param maxId 从1开始的最大id, 也就是序列化后的节点数量
     */
    public LCA(int maxId) {
        this(maxId, 20);
    }


    @Override
    public void addEdge(int u, int v) {
        if (AlgoUtil.checkOutBounds(u, n) || AlgoUtil.checkOutBounds(v, n)) return;
        super.addEdge(u, v);
        G[u].add(v);
    }

    @Override
    public void clear() {
        super.clear();
        this.fa = new int[n + 1][21];
        depth[0] = -1;
    }

    protected void dfs(int prev, int rt) {
        depth[rt] = depth[prev] + 1;
        fa[rt][0] = prev;
        for (int i = 1; i <= dep; i++)
            fa[rt][i] = fa[fa[rt][i - 1]][i - 1];
//        for (int i = 0; i < son[rt].size(); i++)
//            dfs(rt, son[rt][i]);
        if (G[rt] != null) {
            for (int i = 0; i < G[rt].size(); i++)
                dfs(rt, G[rt].to(i));
        }
    }

    /**
     * 建立图
     */
    public LCA buildDfs(int root) {
        if (AlgoUtil.checkOutBounds(root, n)) return this;
        dfs(0, root);
        return this;
    }

    /**
     * 建立图，根节点默认1
     */
    public LCA buildDfs() {
        return buildDfs(1);
    }

    /**
     * 两个节点的最近公共祖先
     */
    public int LCA(int x, int y) {
        if (depth[x] < depth[y]) {
            int tmp = x;
            x = y;
            y = tmp;
        }
        for (int i = dep; i >= 0; i--)
            if (depth[x] - (1 << i) >= depth[y])
                x = fa[x][i];
        if (x == y)
            return x;
        for (int i = dep; i >= 0; i--)
            if (fa[x][i] != fa[y][i]) {
                x = fa[x][i];
                y = fa[y][i];
            }
        return fa[x][0];
    }

}
