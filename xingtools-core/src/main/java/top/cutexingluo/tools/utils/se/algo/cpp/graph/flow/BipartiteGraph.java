package top.cutexingluo.tools.utils.se.algo.cpp.graph.flow;

import top.cutexingluo.tools.utils.se.algo.cpp.common.AlgoUtil;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.base.GraphNodeHandler;

import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * 二分图/二部图
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 18:16
 * @since 1.0.3
 */
public class BipartiteGraph extends GraphNodeHandler<Integer> {


    /**
     * 染色
     */
    protected boolean[] color;

    /**
     * 标记
     */
    protected boolean[] vis;

    /**
     * 匹配对象
     */
    protected int[] pre;

    /**
     * @param maxId 从1开始的最大id, 也就是序列化后的节点数量
     */
    public BipartiteGraph(int maxId) {
        super(maxId);
        color = new boolean[n + 1];
        vis = new boolean[n + 1];
        pre = new int[n + 1];
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
        Arrays.fill(color, false);
        Arrays.fill(vis, false);
        Arrays.fill(pre, 0);
    }

    protected boolean bfs(int x) {
        Arrays.fill(color, false);
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.push(x);
        color[x] = true;
        while (!q.isEmpty()) {
            int u = q.pop();
            q.pop();
            for (int v : G[u]) {
                if (!color[v]) {
                    color[v] = !color[u];
                    q.push(v);
                } else if (color[v] == color[u]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 是否是二分图
     *
     * @param root rootId
     */
    public boolean isBG(int root) {
        return bfs(root);
    }

    /**
     * 是否是二分图
     * <p>默认根节点为1</p>
     */
    public boolean isBG() {
        return bfs(1);
    }

    /**
     * 单次匹配
     */
    protected boolean dfs(int u) {
        for (int v : G[u]) {
            if (vis[v]) continue;
            vis[v] = true;
            if (pre[v] == 0 || dfs(pre[v])) {
                pre[v] = u;
                return true;
            }
        }
        return false;
    }

    /**
     * @return 最大匹配数
     */
    protected int MCBM() {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            Arrays.fill(color, false);
            if (dfs(i)) sum++;
        }
        return sum;
    }

    /**
     * 获取二分图最大匹配
     *
     * @param root 根节点
     * @return 最大匹配数,  返回-1则不是二分图
     */
    public int getMaxMatch(int root) {
        if (!bfs(1)) return -1;
        return MCBM();
    }

    /**
     * 获取二分图最大匹配
     * <p>根节点默认为1</p>
     *
     * @return 最大匹配数,  返回-1则不是二分图
     */
    public int getMaxMatch() {
        return getMaxMatch(1);
    }

}
