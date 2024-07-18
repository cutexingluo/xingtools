package top.cutexingluo.tools.utils.se.algo.cpp.graph.flow;

import top.cutexingluo.tools.utils.se.algo.cpp.graph.GEdge;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.base.GraphGEdgeHandler;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 网络流-最大流
 * <p>dinic 法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/27 17:22
 * @since 1.0.3
 */
public class MaxFlowDinic extends GraphGEdgeHandler {

    /**
     * 层级
     */
    protected int[] dep;

    /**
     * @param maxId 从1开始的最大id, 也就是序列化后的节点数量
     */
    public MaxFlowDinic(int maxId) {
        super(maxId);
        dep = new int[n + 1];
    }

    @Override
    public void clear() {
        super.clear();
//        Arrays.fill(dep, 0);
    }

    /**
     * 层级搜索
     *
     * @param s 开始点
     * @return 是否没有搜到终点
     */
    protected boolean bfs(int s, int t) {
        Arrays.fill(dep, 0);
        LinkedList<Integer> q = new LinkedList<>();
        q.push(s);
        dep[s] = 1;
        while (!q.isEmpty()) {
            int now = q.pop();
            for (int id : Id[now]) {
                GEdge edge = E.get(id);
                long w = edge.w();
                int v = edge.to();
                int u = edge.u();
                if (w > 0 && dep[v] == 0) {
                    dep[v] = dep[u] + 1;
                    q.push(v);
                }
            }
        }
        return dep[t] != 0;//搜到终点
    }

    /**
     * 量更新 ,in就是EK的a数组改进流
     *
     * @param u  此时出发点
     * @param t  最终终点
     * @param in 进入流量
     * @return 流出流量
     */
    protected long dfs(int u, int t, long in) {
        if (u == t) return in;//到达汇点是第一个有效return
        long out = 0;
        for (int id : Id[u]) {
            GEdge edge = E.get(id);
            long w = edge.w();
            int v = edge.to();
            if (w > 0 && dep[v] == dep[u] + 1) {//下一层 核心(分层思想)
                long res = dfs(v, t, Math.min(w, in)); //受上一路最小流量，和可改进流一样
                E.get(id).setW(E.get(id).w() - res);//不用w的原因是有异或操作
                E.get(id ^ 1).setW(E.get(id ^ 1).w() + res);
//                E[id].flow -= res;
//                E[id ^ 1].flow += res;
                in -= res;
                out += res;
            }
        }
        if (out == 0)//被断了，和终点不连通
            dep[u] = 0;//封禁
        return out;
    }

    /**
     * 开始执行
     *
     * @param s 起点
     * @param t 终点
     * @return 最大流
     */
    protected long dinic(int s, int t) {
        long ans = 0;
        while (bfs(s, t)) {
            ans += dfs(s, t, Long.MAX_VALUE);
        }
        return ans;
    }

    /**
     * 开始执行算法
     *
     * @param s 起点
     * @param t 终点
     * @return 最大流
     */
    public long build(int s, int t) {
        return dinic(s, t);
    }

    /**
     * 开始寻找最大流
     * <p>起点为1, 终点为n  (maxId)</p>
     *
     * @return 最大流
     */
    public long build() {
        return dinic(1, n);
    }
}
