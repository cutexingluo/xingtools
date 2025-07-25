package top.cutexingluo.tools.basepackage.chain.task;

import top.cutexingluo.tools.common.base.IValueSource;

/**
 * task node
 * <p>任务节点</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/6/6 16:15
 * @since 1.1.7
 */
public interface TaskNode<V> extends IValueSource<V> {
    /**
     * is complete
     * <p>是否完成</p>
     */
    boolean isComplete();

    /**
     * set complete
     * <p>设置完成</p>
     */
    void setComplete(boolean complete);

    /**
     * is change
     * <p>是否改变</p>
     */
    boolean isChange();

    /**
     * set change
     * <p>设置改变</p>
     */
    void setChange(boolean change);
}
