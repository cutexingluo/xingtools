package top.cutexingluo.tools.utils.se.algo.cpp.graph.base;

import top.cutexingluo.tools.utils.se.algo.cpp.graph.GEdge;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 图-流处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/27 17:25
 * @since 1.0.3
 */
public class GraphGEdgeHandler extends GraphData {

    /**
     * 图(存边)
     */
    protected ArrayList<GEdge> E;

    /**
     * 边的索引列表
     */
    protected ArrayList<Integer>[] Id;


    /**
     * @param maxId 从1开始的最大id, 也就是序列化后的节点数量
     */
    public GraphGEdgeHandler(int maxId) {
        super(maxId);
        Id = (ArrayList<Integer>[]) new ArrayList[n + 1];
        Arrays.fill(Id, new ArrayList<>()); // 全部赋初值
        E = new ArrayList<>();
    }


    @Override
    public void clear() {
        super.clear();
        Arrays.fill(Id, new ArrayList<>()); // 全部赋初值
        E.clear();
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
     * @param w 权值或者索引等其他数据
     */
    public void addEdge(int u, int v, int w) {
        super.addEdge(u, v); // edge ++;
        E.add(new GEdge(u, v, w));
        E.add(new GEdge(v, u, 0));
        int m = E.size();//2
        Id[u].add(m - 2);//0
        Id[v].add(m - 1);//1
    }

}
