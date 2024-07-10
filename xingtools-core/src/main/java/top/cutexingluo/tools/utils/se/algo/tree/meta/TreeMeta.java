package top.cutexingluo.tools.utils.se.algo.tree.meta;

import lombok.Data;
import top.cutexingluo.tools.utils.se.algo.tree.TreeNode;
import top.cutexingluo.tools.utils.se.algo.tree.TreeUtil;

import java.util.Collection;

/**
 * 树节点 元信息
 * <p>该类实例用作 {@link TreeUtil} 里面的对象</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/8/11 18:35
 */
@Data
public class TreeMeta<IdType, T extends TreeNode<IdType>> implements ITreeMeta<T> {

    public TreeMeta(T currentNode, Collection<T> allNodes, TreeNodeHandler<IdType, T> handler) {
        this.currentNode = currentNode;
        this.allNodes = allNodes;
        this.handler = handler;
    }

    /**
     * 自动设置孩子节点
     * <p>仅用于dfs </p>
     */
    private boolean autoSetChildren = true;

    /**
     * 自动添加当前节点到父节点
     * <p>仅用于bfs </p>
     */
    private boolean autoAddThisNodeToParent = true;

    /**
     * 当前节点
     */
    private T currentNode;

    /**
     * 父节点
     * <p> 仅用于bfs </p>
     *
     * @since 1.0.4
     */
    private T parentNode;

    /**
     * 所有节点
     */
    private Collection<T> allNodes;

    /**
     * 节点处理器
     * <p>不能在实现类使用，否则死循环</p>
     */
    private TreeNodeHandler<IdType, T> handler;

    /**
     * 保护，防止使用
     */
    protected TreeNodeHandler<IdType, T> getHandler() {
        return handler;
    }

    /**
     * 孩子列表, 使用 getChildren方法获取
     * <p>dfs 模式表示 当前节点的孩子节点</p>
     * <p>bfs 模式表示 父节点的孩子节点  (也就是当前节点的兄弟节点)</p>
     */
    private Collection<T> childrenList;

    /**
     * 得到children并赋给childrenList
     *
     * <p>dfs 模式为 null, 会调用 dfs 获取孩子节点</p>
     * <p>bfs 模式尽量不要去修改 children</p>
     *
     * @return {@link Collection}<{@link T}>
     */
    @Override
    public Collection<T> getChildren() {
        if (childrenList == null) {
            childrenList = TreeUtil.getChildrenByDfs(currentNode, allNodes, handler);
        }
        return childrenList;
    }

    /**
     * 是否通过getChildrenList方法获取了childrenList
     *
     * @return boolean
     */
    @Override
    public boolean hasChildren() {
        return childrenList != null;
    }
}
