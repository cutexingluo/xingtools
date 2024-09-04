package top.cutexingluo.tools.utils.model.fsm.base;

import top.cutexingluo.tools.utils.se.algo.tree.meta.ITreeMeta;

import java.util.Collection;

/**
 * 状态节点接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/3 14:47
 */
public interface BaseStatusNode<T, S> extends ITreeMeta<S> {
    /**
     * 节点
     */
    T getNode();

    /**
     * 子节点
     */
    Collection<S> getChildren();

    /**
     * 是否有子节点
     */
    boolean hasChildren();
}
