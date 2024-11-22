package top.cutexingluo.tools.utils.se.algo.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.base.IData;

import java.util.Collection;

/**
 * 树节点默认结构
 * <p>可以将数据存入该对象</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/15 17:09
 * @since 1.1.6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNodeData<IdType, V> implements TreeNode<IdType>, IData<V> {

    /**
     * id
     */
    private IdType id;

    /**
     * 父节点id
     */
    private IdType parentId;

    /**
     * 数据
     */
    private V data;

    /**
     * 子节点
     */
    private Collection<TreeNodeData<IdType, V>> children;

    @NotNull
    @Override
    public IdType nodeId() {
        return id;
    }

    @Override
    public IdType nodeParentId() {
        return parentId;
    }

    @Override
    public void setNodeChildren(Collection<? extends TreeNode<IdType>> nodeChildren) {
        this.children = (Collection<TreeNodeData<IdType, V>>) nodeChildren;
    }

    @Override
    public Collection<TreeNodeData<IdType, V>> nodeChildren() {
        return children;
    }

    @Override
    public V data() {
        return data;
    }
}
