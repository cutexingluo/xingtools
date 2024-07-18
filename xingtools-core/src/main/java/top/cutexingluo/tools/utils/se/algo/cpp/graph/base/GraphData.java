package top.cutexingluo.tools.utils.se.algo.cpp.graph.base;

import top.cutexingluo.tools.utils.se.algo.cpp.graph.BaseGraph;

/**
 * 图基本数据
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 17:33
 * @since 1.0.3
 */
public class GraphData implements BaseGraph {
    /**
     * 节点数
     */
    protected int n;

    /**
     * 边数量
     */
    protected int edge;

    /**
     * @param maxId 从1开始的最大id, 也就是序列化后的节点数量
     */
    public GraphData(int maxId) {
        this.n = maxId;
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public int edgeCount() {
        return edge;
    }

    @Override
    public void clear() {
        edge = 0;
    }

    @Override
    public void addEdge(int u, int v) {
        edge++;
    }


}
