package top.cutexingluo.tools.utils.ee.web.front.vue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.utils.se.algo.tree.TreeNode;

import java.util.Collection;

/**
 * RouteRecordRaw 带 Id 版本
 * <p>继承 RouteRecordRaw ，同时实现 TreeNode </p>
 * <p>推荐继承该类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/2/12 13:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteRecordRawWithId<IdType, Meta> extends RouteRecordRaw<Meta> implements TreeNode<IdType> {

    protected IdType id;

    protected IdType parentId;

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
        this.children = (Collection<? extends RouteRecordRawWithId<IdType, Meta>>) nodeChildren;
    }

    @Override
    public <T extends TreeNode<IdType>> Collection<T> nodeChildren() {
        return (Collection<T>) this.children;
    }

}
