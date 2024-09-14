package top.cutexingluo.tools.common.data.node;

/**
 * 基础节点接口
 *
 * @param <T> 节点类型
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/5 11:53
 * @since 1.1.4
 */
public interface INode<T> {

    /**
     * 获取节点
     */
    T getNode();
}
