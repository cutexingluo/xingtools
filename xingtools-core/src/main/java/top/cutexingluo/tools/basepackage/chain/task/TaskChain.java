package top.cutexingluo.tools.basepackage.chain.task;

/**
 * task execution chain
 * <p>任务执行链接口</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/6/6 16:15
 * @since 1.1.7
 */
public interface TaskChain<V> {

    /**
     * accept task node
     * <p>接受任务节点</p>
     *
     * @param node task node
     * @return true if accept
     */
    boolean accept(TaskNode<V> node);

    /**
     * execute task
     * <p>执行任务</p>
     *
     * @param node task node
     */
    void doTask(TaskNode<V> node);
}
