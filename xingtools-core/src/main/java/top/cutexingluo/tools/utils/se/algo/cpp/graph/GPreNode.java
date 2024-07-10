package top.cutexingluo.tools.utils.se.algo.cpp.graph;

/**
 * 前向星边
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/26 19:48
 * @since 1.0.3
 */
public class GPreNode extends GNode {
    /**
     * 链表链接的索引
     */
    private final int next;

    public GPreNode(int to, long w, int next) {
        super(to, w);
        this.next = next;
    }

    public int next() {
        return next;
    }
}
