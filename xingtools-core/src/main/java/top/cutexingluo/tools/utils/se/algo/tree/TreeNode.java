package top.cutexingluo.tools.utils.se.algo.tree;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * 树节点，可以实现该接口
 * <p>1. 为避免存在框架冲突，故不命名 get 前缀</p>
 * <p>2. 实现 nodeId 不能返回 null</p>
 * <p>3. 对应工具类 {@link  TreeUtil}</p>
 * <p>4. 为支持更多类型, 于1.0.4 集合修改为 Collection 类型</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/15 17:26
 */
public interface TreeNode<IdType> {

    /**
     * 节点id
     *
     * @return {@link IdType}
     */
    @NotNull
    IdType nodeId();

    /**
     * 父节点id
     *
     * @return {@link IdType}
     */
    IdType nodeParentId();

    /**
     * 设置孩子，通过此方法自定义设置children
     * <p>由于是 实现方 接收数据，所以参数不使用逆变</p>
     * <p>在这里可以强转为你实现的类</p>
     * <p>ext.       this.children = (T) nodeChildren </p>
     * <p>于 1.0.4 版本从 T 修改为 ? </p>
     *
     * @param nodeChildren 节点孩子
     */
    void setNodeChildren(Collection<? extends TreeNode<IdType>> nodeChildren);

    /**
     * 获取孩子节点
     * <p>通过此方法设置  返回的孩子节点的值</p>
     *
     * @since 1.0.2
     */
    <T extends TreeNode<IdType>> Collection<T> nodeChildren();

}
