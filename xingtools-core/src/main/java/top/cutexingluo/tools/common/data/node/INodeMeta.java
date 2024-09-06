package top.cutexingluo.tools.common.data.node;

import java.util.Collection;

/**
 * 节点元数据接口
 *
 * @param <T> 节点类型
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/5 11:53
 * @since 1.1.4
 */
public interface INodeMeta<T> {

    /**
     * 得到children
     *
     * @return {@link Collection}<{@link T}> 子节点
     */
    Collection<T> getChildren();

    /**
     * 是否存在children
     *
     * @return boolean 是否有子节点
     */
    boolean hasChildren();

}
