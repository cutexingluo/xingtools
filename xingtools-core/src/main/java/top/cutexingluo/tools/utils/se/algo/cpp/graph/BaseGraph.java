package top.cutexingluo.tools.utils.se.algo.cpp.graph;

/**
 * 图基本接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 13:18
 * @since 1.0.3
 */
public interface BaseGraph {


    /**
     * 顶点个数
     */
    int size();

    /**
     * 存边的数量，如果存两次双向边则需要/2
     */
    int edgeCount();


    /**
     * 清除
     */
    void clear();

    /**
     * 存边
     */
    void addEdge(int u, int v);


}
