package top.cutexingluo.tools.utils.se.algo.cpp.graph.dp;

import top.cutexingluo.tools.common.data.Pair;
import top.cutexingluo.tools.utils.se.algo.cpp.common.AlgoUtil;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.GPreNode;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.base.GraphEdgeHandler;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Dijkstra 算法
 * <p>最短路</p>
 * <p>下标从1开始</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 19:52
 * @since 1.0.3
 */
public class Dijkstra extends GraphEdgeHandler {


    /**
     * 离原点距离
     */
    protected int[] dis;

    /**
     * 点标记
     */
    protected boolean[] vis;//点标记

    /**
     * 优先队列
     */
    PriorityQueue<Pair<Integer, Integer>> q;

    /**
     * @param maxId 从1开始的最大id, 也就是序列化后的节点数量
     */
    public Dijkstra(int maxId) {
        super(maxId);
        dis = new int[n + 1];
        Arrays.fill(dis, AlgoUtil.INT_MAX); // 赋值为最大
        vis = new boolean[n + 1];
        q = new PriorityQueue<>();
    }

    @Override
    public void clear() {
        super.clear();
        Arrays.fill(dis, AlgoUtil.INT_MAX);// 赋值为最大
        Arrays.fill(vis, false);
        q.clear();
    }

    /**
     * 加边，默认w 权值为1
     * <p>不建议使用</p>
     *
     * @param u 出发点
     * @param v 结束点
     */
    @Override
    public void addEdge(int u, int v) {
        addEdge(u, v, 1);
    }

    /**
     * 加边
     *
     * @param u 出发点
     * @param v 结束点
     * @param w 权值 ， 应该大于0
     */
    @Override
    public void addEdge(int u, int v, int w) {
        if (AlgoUtil.checkOutBounds(new int[]{u, v}, n)) return;
        else if (AlgoUtil.checkOut(w)) return;
        super.addEdge(u, v, w);
    }

    /**
     * @param s 起始点
     */
    protected void dj(int s) {//s开始
        dis[s] = 0;
        q.add(new Pair<>(0, s));
        while (!q.isEmpty()) {//队列非空
            int u = q.poll().getValue(); //获取目标
//            q.pop();
            if (vis[u])//如果标记
                continue;
            vis[u] = true;
//            for (int i = head[u]; i > 0; i = E[i].next())//对于每一条边
            for (Iterator it = new Iterator(u); it.hasNext(); ) {
                GPreNode now = it.next();
//                int v = E[i].to();
//                int w = (int) E[i].w();
                int v = now.to();
                int w = (int) now.w();
                if (dis[v] > dis[u] + w)//当前路径登记的 大于 当前路径加边权 即当前最小就登记
                {
                    dis[v] = dis[u] + w;
                    if (!vis[v]) {
                        q.add(new Pair<>(dis[v], v)); // java 默认小根堆
                        //未标记压入队列，距离取负，大的先出列，实则距离小的先出列
                    }
                }
            }
        }
    }

    /**
     * 获取实时距离
     */
    public int[] getDistance() {
        return dis;
    }

    /**
     * 开始启动算法
     *
     * @return 所有点的最短距离
     */
    public int[] build(int root) {
        dj(root);
        return getDistance();
    }

    /**
     * 开始启动算法
     * <p>默认源点为1</p>
     *
     * @return 所有点的最短距离
     */
    public int[] build() {
        return build(1);
    }
}
