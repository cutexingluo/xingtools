package top.cutexingluo.tools.utils.se.algo.tree.meta;

import java.util.Collection;

/**
 * 元数据接口，是否存在孩子节点
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/8/11 19:41
 */
public interface ITreeMeta<T> {

    /**
     * 得到children
     *
     * @return {@link Collection}<{@link T}>
     */
    Collection<T> getChildren();

    /**
     * 是否存在children
     *
     * @return boolean
     */
    boolean hasChildren();
}
