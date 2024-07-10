package top.cutexingluo.tools.utils.se.algo.cpp.graph.dp;

import top.cutexingluo.tools.utils.se.algo.cpp.common.AlgoUtil;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.GEdge;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.GPreNode;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.base.DisjointSetUnion;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.base.GraphEdgeHandler;
import top.cutexingluo.tools.utils.se.map.XTComparator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * MinimumSpanningTree
 * <p>最小生成树</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/27 18:40
 * @since 1.0.3
 */
public class MinSpanningTree extends GraphEdgeHandler implements DisjointSetUnion {

    //--------------Kruskal-------------
    /**
     * Kruskal专用图
     * <p>存边</p>
     */
    protected ArrayList<GEdge> edges;
    /**
     * 并查集
     */
    protected int[] f;

    //--------------Prim-------------
    /**
     * 路径距离
     */
    protected int[] dis;//路径大小
    /**
     * 标记
     */
    protected boolean[] vis;//标记

    /**
     * 父节点
     */
    protected int[] p;

    /**
     * 获取父亲节点，需要运行prim算法
     */
    public int[] getParent() {
        return p;
    }

    /**
     * @param maxId 从1开始的最大id, 也就是序列化后的节点数量
     */
    public MinSpanningTree(int maxId) {
        super(maxId);
        edges = new ArrayList<>();
        f = new int[n + 1];
        dis = new int[n + 1];
        Arrays.fill(dis, AlgoUtil.INT_MAX);
        vis = new boolean[n + 1];
        p = new int[n + 1];
    }

    @Override
    public void clear() {
        super.clear();
        edges.clear();
        Arrays.fill(f, 0);
        Arrays.fill(dis, AlgoUtil.INT_MAX);
        Arrays.fill(vis, false);
        Arrays.fill(p, 0);
    }

    @Override
    public void addEdge(int u, int v) {
        addEdge(u, v, 1);
    }

    @Override
    public void addEdge(int u, int v, int w) {
        if (AlgoUtil.checkOutBounds(new int[]{u, v}, n)) return;
        else if (AlgoUtil.checkOut(w)) return;
        if (w < 0) return;
        super.addEdge(u, v, w);
        edges.add(new GEdge(u, v, w));
    }

    /**
     * kruskal算法，注意存边需要存单向边
     *
     * @return 返回最小生成树的总权值, -1则未连通
     */
    protected long kruskal() {
        long ans = 0;
        int cnt = 0;
        edges.sort(new XTComparator<>(true));
        //将边的权值排序
        for (GEdge e : edges) {
            int eu = find(f, e.u()), ev = find(f, e.to());
            if (eu == ev) {
                continue;
            }
            //若出现两个点已经联通了，则说明这一条边不需要了
            ans += e.w();
            //将此边权计入答案
            unionMerge(f, eu, ev);
            //将eu、ev合并
            if (++cnt == n - 1) {
                break;
            }
            //循环结束条件，及边数为点数减一时
        }
        if (cnt != n - 1) {
            return -1;
        }
        return ans;
    }

    /**
     * prim算法，支持双向边（无向图）
     *
     * @param x 起始点
     * @return 返回最小生成树的总权值, -1则未连通
     */
    protected long prim(int x) {//x为顶点
        int tot = 0, now = x;
        long ans = 0;
        while (++tot < n) {
            vis[now] = true;//add
//            for (int i = head[now]; i > 0; i = E[now].next()) {//update
//                int v = E[now].to(), w = (int) E[now].w();
            for (Iterator it = new Iterator(now); it.hasNext(); ) {
                GPreNode node = it.next();
                int v = node.to(), w = (int) node.w();
                if (!vis[v] && dis[v] > w) {//少了!vis[v]也能AC，只不过第一个p[1]!=-1
                    dis[v] = w;
                    p[v] = now;
                }
            }
            int min = AlgoUtil.INT_MAX;
            for (int i = 1; i <= n; i++) {//scan
                if (!vis[i] && min > dis[i]) {
                    min = dis[i];
                    now = i;
                }
            }
            if (min == AlgoUtil.INT_MAX) {//无结果代表未连通
                return -1;
            }
            ans += min;
        }
        return ans;
    }

    /**
     * kruskal算法，注意存边需要存单向边
     * <p>两个算法最好只执行一个</p>
     *
     * @return 返回最小生成树的总权值, -1则未连通
     */
    public long runKruskal() {
        return kruskal();
    }

    /**
     * prim算法，支持双向边（无向图）
     * <p>调用后可以使用 getParent 获取父节点列表，得到路径</p>
     * <p>两个算法最好只执行一个</p>
     * <p>默认源点为1</p>
     *
     * @return 返回最小生成树的总权值, -1则未连通
     */
    public long runPrim() {
        return prim(1);
    }

    /**
     * prim算法，支持双向边（无向图）
     * <p>调用后可以使用 getParent 获取父节点列表，得到路径</p>
     * <p>两个算法最好只执行一个</p>
     *
     * @param beginIndex 源点，起始点
     * @return 返回最小生成树的总权值, -1则未连通
     */
    public long runPrim(int beginIndex) {
        return prim(beginIndex);
    }
}
