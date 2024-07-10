package top.cutexingluo.tools.utils.se.algo.cpp.graph.base;

import top.cutexingluo.tools.utils.se.algo.cpp.graph.GraphNode;

import java.util.Arrays;

/**
 * 图数据基础存点类
 * <p>存点法</p>
 * <p>建议从下标为1开始</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/27 16:31
 * @since 1.0.3
 */
public class GraphNodeHandler<T> extends GraphData {

    /**
     * 图(存点)
     */
    protected GraphNode<T>[] G;

    /**
     * @param maxId 从1开始的最大id, 也就是序列化后的节点数量
     */
    public GraphNodeHandler(int maxId) {
        super(maxId);
        G = (GraphNode<T>[]) new GraphNode[n + 1];
        Arrays.fill(G, new GraphNode<>()); // 全部赋初值
    }

    @Override
    public void clear() {
        super.clear();
        Arrays.fill(G, new GraphNode<>());
    }

    @Override
    public void addEdge(int u, int v) {
        super.addEdge(u, v); // edge++
    }

}
