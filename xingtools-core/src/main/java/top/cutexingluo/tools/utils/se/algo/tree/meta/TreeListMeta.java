package top.cutexingluo.tools.utils.se.algo.tree.meta;

import lombok.Data;
import top.cutexingluo.tools.utils.se.algo.tree.TreeNode;

import java.util.Collection;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/16 18:43
 * @since 1.0.2
 */
@Data
public class TreeListMeta<IdType, T extends TreeNode<IdType>> implements ITreeMeta<T> {
    public TreeListMeta(T currentNode, Collection<T> newList) {
        this.currentNode = currentNode;
        this.newList = newList;
    }

    /**
     * 自动清除孩子节点 为 null
     */
    private boolean autoSetChildrenToNull = true;

    /**
     * 自动添加到列表
     */
    private boolean autoAddThisNode = true;

    /**
     * 当前节点
     */
    private T currentNode;

    /**
     * 所有节点
     */
    private Collection<T> newList;


    /**
     * 添加这个节点
     */
    public void addThisNode() {
        newList.add(currentNode);
    }


    @Override
    public Collection<T> getChildren() {
        return currentNode.nodeChildren();
    }

    @Override
    public boolean hasChildren() {
        return currentNode.nodeChildren() != null;
    }
}
