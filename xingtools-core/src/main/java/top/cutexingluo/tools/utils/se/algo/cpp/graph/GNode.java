package top.cutexingluo.tools.utils.se.algo.cpp.graph;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.se.core.compare.XTComparator;

/**
 * 图节点
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 17:12
 * @since 1.0.3
 */
@AllArgsConstructor
public class GNode implements Comparable<GNode> {
    /**
     * 去点
     */
    private int to;
    /**
     * 权值或者索引
     * <p>等任意第三方数据</p>
     */
    private long w;

    public int to() {
        return to;
    }

    public long w() {
        return w;
    }

    @Override
    public int compareTo(@NotNull GNode o) {
        XTComparator<Long> comparator = new XTComparator<>(true);
        return comparator.compare(w, o.w());
    }

}
