package top.cutexingluo.tools.utils.se.algo.tree;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.utils.se.algo.tree.meta.ListNodeHandler;
import top.cutexingluo.tools.utils.se.algo.tree.meta.TreeListMeta;
import top.cutexingluo.tools.utils.se.algo.tree.meta.TreeMeta;
import top.cutexingluo.tools.utils.se.algo.tree.meta.TreeNodeHandler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Tree 树形工具
 * <p>可以实现列表转树形操作</p>
 * <p>实体类通过实现 TreeNode 接口, 即可使用该工具类</p>
 * <p>对应接口 {@link  TreeNode}</p>
 *
 * <p>1. dfs 通过找子节点方式递归获取</p>
 * <p>2. bfs 通过找父节点方式添加至父节点的孩子列表</p>
 * <p>3. 为支持更多类型, 于1.0.4 集合修改为 Collection 类型</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/15 17:24
 */
public class TreeUtil {

    //--------------------- list to tree -----------------------------

    // ------ bfs ------

    /**
     * 使用 bfs 生成 树形结构
     *
     * @param nodes        所有节点列表
     * @param rootParentId 根节点的parentId
     * @return {@link ArrayList}<{@link T}>, 返回实体类协变
     */
    @NotNull
    public static <IdType, T extends TreeNode<IdType>> ArrayList<T> buildByBfs(@NotNull Collection<T> nodes, @Nullable IdType rootParentId) {
        return buildByBfs(nodes, rootParentId, true, null);
    }

    /**
     * 使用 bfs 生成 树形结构
     *
     * @param nodes            所有节点列表
     * @param rootParentId     根节点的parentId
     * @param fillNullChildren 填充空子节点为空列表，即 null->[]
     * @return {@link ArrayList}<{@link T}>, 返回实体类协变
     */
    @NotNull
    public static <IdType, T extends TreeNode<IdType>> ArrayList<T> buildByBfs(@NotNull Collection<T> nodes, @Nullable IdType rootParentId, boolean fillNullChildren) {
        return buildByBfs(nodes, rootParentId, fillNullChildren, null);
    }

    /**
     * 使用 bfs 生成 树形结构
     *
     * @param nodes            所有节点列表
     * @param rootParentId     根节点的parentId
     * @param fillNullChildren 填充空子节点为空列表，即 null->[]
     * @param handler          处理程序
     * @return {@link ArrayList}<{@link T}>, 返回实体类协变
     */
    @NotNull
    public static <IdType, T extends TreeNode<IdType>> ArrayList<T> buildByBfs(@NotNull Collection<T> nodes, @Nullable IdType rootParentId, boolean fillNullChildren, @Nullable TreeNodeHandler<IdType, T> handler) {
        HashMap<IdType, T> idMap = new HashMap<>(nodes.size());
        ArrayList<T> rootList = new ArrayList<>(nodes.size());
        for (T node : nodes) {
            idMap.put(node.nodeId(), node);
        }
        for (T node : nodes) {
            if (fillNullChildren && node.nodeChildren() == null) {
                node.setNodeChildren(new ArrayList<>());
            }
            IdType parentId = node.nodeParentId(); // parent
            if (Objects.equals(parentId, rootParentId)) {
                rootList.add(node);
            }
            if (idMap.containsKey(parentId)) {
                T parentNode = idMap.get(parentId);
                Collection<T> children = parentNode.nodeChildren();
                if (children == null) {
                    children = new ArrayList<>();
                }
                if (handler != null) { // handler 自定义
                    TreeMeta<IdType, T> treeMeta = new TreeMeta<>(node, nodes, handler);
                    treeMeta.setParentNode(parentNode);
                    treeMeta.setChildrenList(children);
                    // 自定义 操作节点
                    handler.handle(treeMeta);
                    if (treeMeta.isAutoAddThisNodeToParent()) {
                        children.add(node);
                    }
                } else {
                    children.add(node);
                }
                parentNode.setNodeChildren(children); // update
            }
        }
        return rootList;
    }


    // ------ dfs ------

    /**
     * 使用 dfs 生成 树形结构
     *
     * @param nodes        所有节点列表
     * @param rootParentId 根节点的parentId
     * @return {@link List}<{@link T}>
     */
    public static <IdType, T extends TreeNode<IdType>> List<T> buildByDfs(@NotNull Collection<T> nodes, IdType rootParentId) {
        return nodes.stream().filter(node -> Objects.equals(node.nodeParentId(), rootParentId))
                .peek(node -> node.setNodeChildren(getChildrenByDfs(node, nodes))).collect(Collectors.toList());
    }

    /**
     * 使用 dfs 生成 树形结构
     * <p> 可以实现 handler 进行对每个节点进行精细操作 </p>
     *
     * @param nodes        所有节点列表
     * @param rootParentId 根节点的parentId
     * @param handler      处理程序
     * @return {@link List}<{@link T}>
     */
    public static <IdType, T extends TreeNode<IdType>> List<T> buildByDfs(@NotNull Collection<T> nodes, IdType rootParentId, TreeNodeHandler<IdType, T> handler) {
        return nodes.stream().filter(node -> Objects.equals(node.nodeParentId(), rootParentId))
                .peek(node -> runTreeNodeHandler(node, nodes, handler)).collect(Collectors.toList());
    }

    /**
     * 通过dfs
     * 返回当前节点的所有子结点
     *
     * @param currentNode 当前节点
     * @param allNodes    所有节点
     * @return {@link List}<{@link T}>
     */
    public static <IdType, T extends TreeNode<IdType>> List<T> getChildrenByDfs(@NotNull T currentNode, @NotNull Collection<T> allNodes) {
        return allNodes.stream()
                .filter(node -> currentNode.nodeId().equals(node.nodeParentId()))
                .peek(node -> node.setNodeChildren(getChildrenByDfs(node, allNodes)))
                .collect(Collectors.toList());
    }


    /**
     * 通过dfs
     * 返回当前节点的所有子结点
     * <p>handler 操作每一个节点 </p>
     *
     * @param currentNode 当前节点
     * @param allNodes    所有节点
     * @param handler     处理程序
     * @return {@link List}<{@link T}>
     */
    public static <IdType, T extends TreeNode<IdType>> List<T> getChildrenByDfs(@NotNull T currentNode, @NotNull Collection<T> allNodes, TreeNodeHandler<IdType, T> handler) {
        return allNodes.stream()
                .filter(node -> currentNode.nodeId().equals(node.nodeParentId()))
                .peek(node -> runTreeNodeHandler(node, allNodes, handler)).collect(Collectors.toList());
    }

    /**
     * 树节点处理程序运行
     * <p>handler 操作每一个节点 </p>
     *
     * @param node     节点
     * @param allNodes 所有节点
     * @param handler  处理程序
     */
    public static <IdType, T extends TreeNode<IdType>> void runTreeNodeHandler(T node, @NotNull Collection<T> allNodes, @Nullable TreeNodeHandler<IdType, T> handler) {
        if (node == null) {
            return;
        }
        if (handler == null) { // handler为空
            node.setNodeChildren(getChildrenByDfs(node, allNodes));
        } else {
            TreeMeta<IdType, T> treeMeta = new TreeMeta<>(node, allNodes, handler);
            // 自定义 操作节点
            handler.handle(treeMeta);
            if (treeMeta.isAutoSetChildren()) {
                if (treeMeta.hasChildren()) {
                    node.setNodeChildren(treeMeta.getChildrenList());
                } else {
                    node.setNodeChildren(getChildrenByDfs(node, allNodes, handler));
                }
            }
        }
    }


    //---------------------tree to list -----------------------------
    //  ------ bfs ------

    /**
     * 通过bfs
     * 将树形转为平面列表形
     * <p>不限定rootParentId, 把属性结构列表中所有都转为列表形 </p>
     *
     * @param treeNodes 树形结构列表
     * @return newList
     * @since 1.0.4
     */
    @NotNull
    public static <IdType, T extends TreeNode<IdType>> ArrayList<T> flatListByBfs(@NotNull Collection<T> treeNodes) {
        ArrayList<T> newList = new ArrayList<>();
        return (ArrayList<T>) flatListByBfs(treeNodes, newList, null);
    }


    /**
     * 通过bfs
     * 将树形转为平面列表形
     *
     * @param treeNodes    树形结构列表
     * @param rootParentId 根节点的parentId
     * @return newList
     * @since 1.0.4
     */
    @NotNull
    public static <IdType, T extends TreeNode<IdType>> ArrayList<T> flatListByBfs(@NotNull Collection<T> treeNodes, IdType rootParentId) {
        ArrayList<T> newList = new ArrayList<>();
        return (ArrayList<T>) flatListByBfs(treeNodes, rootParentId, newList, null);
    }

    /**
     * 通过bfs
     * 将树形转为平面列表形
     *
     * @param treeNodes    树形结构列表
     * @param rootParentId 根节点的parentId
     * @param newList      新列表
     * @param handler      处理程序
     * @return newList
     * @since 1.0.4
     */
    public static <IdType, T extends TreeNode<IdType>> Collection<T> flatListByBfs(@NotNull Collection<T> treeNodes, IdType rootParentId, @NotNull Collection<T> newList, @Nullable ListNodeHandler<IdType, T> handler) {
        List<T> collect = treeNodes.stream().filter(node -> Objects.equals(node.nodeParentId(), rootParentId)).collect(Collectors.toList());
        flatListBfsHandler(collect, newList, handler);
        return newList;
    }


    /**
     * 通过bfs
     * 将树形转为平面列表形
     * <p>不限定rootParentId, 把属性结构列表中所有都转为列表形 </p>
     *
     * @param treeNodes 树形结构列表
     * @param newList   新列表
     * @param handler   处理程序
     * @return newList
     * @since 1.0.4
     */
    public static <IdType, T extends TreeNode<IdType>> Collection<T> flatListByBfs(@NotNull Collection<T> treeNodes, @NotNull Collection<T> newList, @Nullable ListNodeHandler<IdType, T> handler) {
        flatListBfsHandler(treeNodes, newList, handler);
        return newList;
    }


    /**
     * 平铺列表处理程序
     * <p>将当前节点及其所有子节点转为列表</p>
     * <p>将 Tree 形结构转化为 List 形</p>
     *
     * @param rootTreeNodes 树顶级节点集合
     * @param newList       新列表 ( 用于装新数据 )
     * @param handler       处理程序（可以自行设置是否将孩子节点设置为空 / 操作节点）
     * @since 1.0.4
     */

    public static <IdType, T extends TreeNode<IdType>> void flatListBfsHandler(@NotNull Collection<T> rootTreeNodes, @Nullable Collection<T> newList, ListNodeHandler<IdType, T> handler) {
        LinkedList<T> queue = new LinkedList<>(rootTreeNodes);
        while (!queue.isEmpty()) {
            T node = queue.poll();
            if (node == null) {
                continue;
            }
            if (handler == null) {
                Collection<TreeNode<IdType>> children = node.nodeChildren(); // 获取孩子
                node.setNodeChildren(null);
                if (newList != null) newList.add(node);
                if (children != null) {
                    children.forEach(child -> queue.add((T) child));
                }
            } else {
                Collection<TreeNode<IdType>> children = node.nodeChildren(); // 获取孩子
                TreeListMeta<IdType, T> meta = new TreeListMeta<>(node, newList);
                handler.handle(meta);
                if (meta.isAutoSetChildrenToNull()) node.setNodeChildren(null);
                if (newList != null && meta.isAutoAddThisNode()) newList.add(node);
                if (children != null) {
                    children.forEach(child -> queue.add((T) child));
                }
            }
        }
    }

    //  ------ dfs ------

    /**
     * 通过dfs
     * 将树形转为平面列表形
     *
     * @param treeNodes    树形节点
     * @param rootParentId 根父ID
     * @return {@link List}<{@link T}>
     */
    @NotNull
    public static <IdType, T extends TreeNode<IdType>> ArrayList<T> flatListByDfs(@NotNull Collection<T> treeNodes, IdType rootParentId) {
        return flatListByDfs(treeNodes, rootParentId, null);
    }

    /**
     * 通过dfs
     * 将树形转为平面列表形
     *
     * @param treeNodes    树形节点
     * @param rootParentId 根父ID
     * @param handler      处理程序
     * @return {@link ArrayList}<{@link T}> newList
     */
    @NotNull
    public static <IdType, T extends TreeNode<IdType>> ArrayList<T> flatListByDfs(@NotNull Collection<T> treeNodes, IdType rootParentId, ListNodeHandler<IdType, T> handler) {
        ArrayList<T> newList = new ArrayList<>();
        return (ArrayList<T>) flatListByDfs(treeNodes, rootParentId, newList, handler);
    }


    /**
     * 通过dfs
     * 将树形转为平面列表形
     *
     * @param treeNodes    树形节点
     * @param rootParentId 父节点id
     * @param newList      新列表
     * @param handler      处理程序
     * @return newList
     * @updateFrom 1.0.4
     */
    @NotNull
    public static <IdType, T extends TreeNode<IdType>> Collection<T> flatListByDfs(@NotNull Collection<T> treeNodes, IdType rootParentId, @NotNull Collection<T> newList, ListNodeHandler<IdType, T> handler) {
        treeNodes.stream().filter(node -> Objects.equals(node.nodeParentId(), rootParentId))
                .forEach(node -> flatListHandler(node, newList, handler));
        return newList;
    }

    /**
     * 通过dfs
     * 将树形转为平面列表形
     * <p>无根父节点约束</p>
     *
     * @param treeNodes 树形节点
     * @return {@link ArrayList}<{@link T}> newList
     * @since 1.0.4
     */
    @NotNull
    public static <IdType, T extends TreeNode<IdType>> ArrayList<T> flatListByDfs(@NotNull Collection<T> treeNodes) {
        ArrayList<T> newList = new ArrayList<>();
        return (ArrayList<T>) flatListByDfs(treeNodes, newList);
    }

    /**
     * 通过dfs
     * 将树形转为平面列表形
     * <p>无根父节点约束</p>
     *
     * @param treeNodes 树形节点
     * @param newList   新列表
     * @return newList
     * @since 1.0.4
     */
    @NotNull
    public static <IdType, T extends TreeNode<IdType>> Collection<T> flatListByDfs(@NotNull Collection<T> treeNodes, @NotNull Collection<T> newList) {
        treeNodes.forEach(node -> flatListHandler(node, newList, null));
        return newList;
    }

    /**
     * 通过dfs
     * 将树形转为平面列表形
     * <p>无根父节点约束</p>
     *
     * @param treeNodes 树形节点
     * @param newList   新列表
     * @param handler   处理程序
     * @return newList
     * @since 1.0.4
     */
    @NotNull
    public static <IdType, T extends TreeNode<IdType>> Collection<T> flatListByDfs(@NotNull Collection<T> treeNodes, @NotNull Collection<T> newList, ListNodeHandler<IdType, T> handler) {
        treeNodes.forEach(node -> flatListHandler(node, newList, handler));
        return newList;
    }

    /**
     * 平铺列表处理程序
     * <p>将当前节点及其所有子节点转为列表</p>
     * <p>将 Tree 形结构转化为 List 形</p>
     *
     * @param node    节点
     * @param newList 新列表 ( 用于装新数据 )
     * @param handler 处理程序（可以自行设置是否将孩子节点设置为空 / 操作节点）
     */
    public static <IdType, T extends TreeNode<IdType>> void flatListHandler(T node, @Nullable Collection<T> newList, ListNodeHandler<IdType, T> handler) {
        if (node == null) {
            return;
        }
        if (handler == null) {
            Collection<TreeNode<IdType>> children = node.nodeChildren(); // 获取孩子
            node.setNodeChildren(null);
            if (newList != null) newList.add(node);
            if (children != null) {
                children.forEach(child -> flatListHandler((T) child, newList, null));
            }
        } else {
            Collection<TreeNode<IdType>> children = node.nodeChildren(); // 获取孩子
            TreeListMeta<IdType, T> meta = new TreeListMeta<>(node, newList);
            handler.handle(meta);
            if (meta.isAutoSetChildrenToNull()) node.setNodeChildren(null);
            if (newList != null && meta.isAutoAddThisNode()) newList.add(node);
            if (children != null) {
                children.forEach(child -> flatListHandler((T) child, newList, handler));
            }
        }

    }

}
