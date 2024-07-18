package top.cutexingluo.tools.utils.se.algo.cpp.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 图的节点
 * <p>能连接的所有节点</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 13:32
 * @since 1.0.3
 */
@Data
@AllArgsConstructor
public class GraphNode<T> implements Iterable<T> {
    /**
     * 节点编号集
     */
    private ArrayList<T> list;


    public GraphNode() {
        this.list = new ArrayList<>();
    }

    public int size() {
        return list.size();
    }

    public void add(T to) {
        list.add(to);
    }

    public void add(Collection<T> toCollection) {
        list.addAll(toCollection);
    }

    /**
     * 返回第几个索引的节点编号
     *
     * @param toIndex 索引
     * @return 节点编号
     */
    public T to(int toIndex) {
        return list.get(toIndex);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    public void clear() {
        list.clear();
    }
}
