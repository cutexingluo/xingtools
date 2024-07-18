package top.cutexingluo.tools.utils.se.algo.tree.meta;

import top.cutexingluo.tools.utils.se.algo.tree.TreeNode;

/**
 * 树节点处理程序
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/8/11 19:44
 */
@FunctionalInterface
public interface TreeNodeHandler<IdType, T extends TreeNode<IdType>> {

    /**
     * 消费者，对每一次单节点的操作
     * <p>可以手动初始化赋值</p>
     *
     * @param meta 单节点信息元
     */
    void handle(TreeMeta<IdType, T> meta);
}
