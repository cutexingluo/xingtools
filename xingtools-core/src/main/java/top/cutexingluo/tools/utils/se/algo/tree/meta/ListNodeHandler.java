package top.cutexingluo.tools.utils.se.algo.tree.meta;

import top.cutexingluo.tools.utils.se.algo.tree.TreeNode;

/**
 * 树转列表-节点处理程序
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/16 18:47
 * @since 1.0.2
 */
@FunctionalInterface
public interface ListNodeHandler<IdType, T extends TreeNode<IdType>> {
    /**
     * 消费者，对每一次单节点的操作
     *
     * @param meta 单节点信息元
     */
    void handle(TreeListMeta<IdType, T> meta);
}
