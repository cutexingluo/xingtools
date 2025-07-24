package top.cutexingluo.tools.basepackage.chain.task;

/**
 * task chain
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/6/6 16:37
 * @since 1.1.7
 */
public abstract class AbstractTaskChain<V> implements TaskChain<V> {

    @Override
    public boolean accept(TaskNode<V> node) {
        return node != null && !node.isComplete();
    }

}
