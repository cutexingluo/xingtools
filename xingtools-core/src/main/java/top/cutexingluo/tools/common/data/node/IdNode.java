package top.cutexingluo.tools.common.data.node;

/**
 * 基于 id 的节点接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/12 14:32
 * @since 1.1.6
 */
public interface IdNode<IdType> {

    /**
     * 获取节点 id
     *
     * @return the id
     */
    IdType getId();
}
