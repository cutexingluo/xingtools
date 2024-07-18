package top.cutexingluo.tools.utils.se.algo.cpp.graph.dp;

import top.cutexingluo.tools.utils.se.algo.cpp.common.AlgoUtil;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.GPreNode;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.base.GraphEdgeHandler;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * BellmanFord / spfa
 * <p>最短路，负环</p>
 * <p>下标从1开始</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/27 15:39
 * @since 1.0.3
 */
public class BellmanFord extends GraphEdgeHandler {


    /**
     * 离原点距离
     */
    protected int[] dis;

    /**
     * 入队情况
     */
    protected boolean[] inq;

    /**
     * 普通队列
     */
    protected LinkedList<Integer> q;

    /**
     * 节点边数计数
     */
    protected int[] cnt;

    /**
     * @param maxId 从1开始的最大id, 也就是序列化后的节点数量
     */
    public BellmanFord(int maxId) {
        super(maxId);
        dis = new int[n + 1];
        Arrays.fill(dis, AlgoUtil.INT_MAX); // 赋值为最大
        inq = new boolean[n + 1];
        q = new LinkedList<>();
        cnt = new int[n + 1];
    }

    @Override
    public void clear() {
        super.clear();
        Arrays.fill(dis, AlgoUtil.INT_MAX);// 赋值为最大
        Arrays.fill(inq, false);
        Arrays.fill(cnt, 0);
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
     * @param w 权值
     */
    @Override
    public void addEdge(int u, int v, int w) {
        if (AlgoUtil.checkOutBounds(new int[]{u, v}, n)) return;
        else if (AlgoUtil.checkOut(w)) return;
        super.addEdge(u, v, w);
    }

    /**
     * @param s 起始点
     * @return true 执行成功，false 执行失败, 存在负环
     */
    protected boolean spfa(int s) {//s开始
        dis[s] = 0;
        q.add(s);
        inq[s] = true; //入队
        while (!q.isEmpty()) {//队列非空
            int u = q.pop(); //获取目标
//            q.pop();
            inq[u] = false; //出队
//            for (int i = head[u]; i > 0; i = E[i].next())//对于每一条边
//            {
//                int v = E[i].to();
//                int w = (int) E[i].w();
            for (Iterator it = new Iterator(u); it.hasNext(); ) {
                GPreNode now = it.next();
                int v = now.to();
                int w = (int) now.w();
                if (dis[v] > dis[u] + w)//当前路径登记的 大于 当前路径加边权 即当前最小就登记
                {
                    dis[v] = dis[u] + w; //松弛
                    if (inq[v]) continue;
                    cnt[v] = cnt[u] + 1;//边数总量 	写成cnt[v]++也可以
                    if (cnt[v] >= n) {
                        return false;
                    }
                    inq[v] = true; //入队
                    q.add(v);
                }
            }
        }
        return true;
    }

    /**
     * 执行算法
     *
     * @param root 源点
     * @return true 执行成功，false 执行失败, 存在负环
     */
    public boolean build(int root) {
        return spfa(root);
    }

    /**
     * 执行算法
     * <p>默认源点为1</p>
     *
     * @return true 执行成功，false 执行失败, 存在负环
     */
    public boolean build() {
        return spfa(1);
    }

}
