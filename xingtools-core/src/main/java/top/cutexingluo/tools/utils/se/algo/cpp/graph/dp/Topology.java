package top.cutexingluo.tools.utils.se.algo.cpp.graph.dp;

import top.cutexingluo.tools.utils.se.algo.cpp.common.AlgoUtil;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.GPreNode;
import top.cutexingluo.tools.utils.se.algo.cpp.graph.base.GraphEdgeHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * topo 基类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/27 16:48
 * @since 1.0.3
 */
public class Topology extends GraphEdgeHandler {

    /**
     * 入度
     */
    protected Integer[] in;

    /**
     * 起始队列
     */
    protected LinkedList<Integer> q;

    /**
     * topo 排序后的答案
     */
    protected ArrayList<Integer> sorted;

    /**
     * @return 获取拓扑序
     */
    public ArrayList<Integer> getSorted() {
        return sorted;
    }

    /**
     * @param maxId 从1开始的最大id, 也就是序列化后的节点数量
     */
    public Topology(int maxId) {
        super(maxId);
        in = new Integer[n + 1];
        q = new LinkedList<>();
    }

    @Override
    public void clear() {
        super.clear();
        Arrays.fill(in, 0);
        q.clear();
    }

    /**
     * topo加边
     *
     * @param u 出发点
     * @param v 结束点
     */
    @Override
    public void addEdge(int u, int v) {
        addEdge(u, v, 1);
    }

    /**
     * topo加边
     *
     * @param u 出发点
     * @param v 结束点
     * @param w 权值或者索引等其他数据
     */
    @Override
    public void addEdge(int u, int v, int w) {
        if (AlgoUtil.checkOutBounds(new int[]{u, v}, n)) return;
        else if (AlgoUtil.checkOut(w)) return;
        super.addEdge(u, v, w);
        in[v]++;
    }

    /**
     * 开始列表
     *
     * @return 入度为0的作为开始列表
     */
    public LinkedList<Integer> getBeginList() {
        return q;
    }

    /**
     * 只会执行一次创建，直到clear
     *
     * @return 入度为0的作为开始列表
     */
    public LinkedList<Integer> createBeginList() {
        if (q.size() != 0) return q;
        for (int i = 1; i <= n; i++) {
            if (in[i] == 0) {
                q.add(i);
            }
        }
        return q;
    }

    /**
     * 遍历顶点 ,生成 topo序，存入 sorted ,通过 getSorted 获取
     *
     * @return 是否成环
     */
    protected boolean build() {
        int cnt = 0;
        while (!q.isEmpty()) {
            int k = q.pop();
            cnt += 1;
            sorted.add(k);
//            for (int i = head[k]; i > 0; i = E[i].next()) {
//                int v = E[i].to();
            for (Iterator it = new Iterator(k); it.hasNext(); ) {
                GPreNode node = it.next();
                int v = node.to();
                in[v]--;
                if (in[v] == 0)
                    q.push(v);
            }
        }
        if ((cnt ^ n) != 0)//不相等成环
            return true;
        return false;
    }
}
