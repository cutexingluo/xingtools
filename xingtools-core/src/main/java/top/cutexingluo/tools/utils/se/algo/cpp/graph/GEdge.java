package top.cutexingluo.tools.utils.se.algo.cpp.graph;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.se.map.XTComparator;

/**
 * 图边
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 19:16
 * @since 1.0.3
 */
@AllArgsConstructor
public class GEdge implements Comparable<GNode> {
    /**
     * 出发点
     */
    private int from;
    /**
     * 去点
     */
    private int to;
    /**
     * 权值或者索引
     * <p>等任意第三方数据</p>
     */
    private long w;

    public int u() {
        return from;
    }

    public int to() {
        return to;
    }

    public long w() {
        return w;
    }

    public void setW(long w) {
        this.w = w;
    }

    @Override
    public int compareTo(@NotNull GNode o) {
        XTComparator<Long> comparator = new XTComparator<>(true);
        return comparator.compare(w, o.w());
    }
}
