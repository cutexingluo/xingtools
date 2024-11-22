package top.cutexingluo.tools.utils.model.fsm;

import lombok.Data;
import top.cutexingluo.tools.utils.model.fsm.base.BaseStatusNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 节点状态机
 *
 * <p>面向图的状态机，可自定义事件，可无边权即无事件</p>
 *
 * @param <O>    状态索引 泛型
 * @param <Node> 索引对应的节点封装 接口泛型
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/3 14:43
 * @since 1.1.4
 */
@Data
public class NodeStateMachine<O, Node extends BaseStatusNode<?, ?>> {

    protected Map<O, Node> statusMap;

    public NodeStateMachine(Map<O, Node> statusEventMap) {
        this.statusMap = statusEventMap;
    }

    public NodeStateMachine() {
        this.statusMap = new HashMap<>();
    }

    /**
     * 添加状态转换
     */
    public void put(O index, Node node) {
        statusMap.put(index, node);
    }

    /**
     * 得到目标节点
     */
    public Node get(O index) {
        return statusMap.get(index);
    }


    /**
     * 通过索引转换，判断是否能够接受转换
     *
     * <p>node 的子节点必须是 O 泛型 </p>
     *
     * @param sourceIndex 原索引
     * @param targetIndex 目标索引
     * @return boolean 是否能够接受转换
     */
    public boolean canAcceptIndex(O sourceIndex, O targetIndex) {
        Node node = statusMap.get(sourceIndex);
        if (node != null && node.hasChildren()) {
            return node.getChildren().contains(targetIndex);
        }
        return false;
    }

    /**
     * 通过索引转换，判断是否能够接受转换
     *
     * <p>node 的子节点必须是 Node 泛型 </p>
     *
     * @param sourceIndex 原索引
     * @param targetIndex 目标索引
     * @return boolean 是否能够接受转换
     */
    public boolean canAcceptNode(O sourceIndex, O targetIndex) {
        Node node = statusMap.get(sourceIndex);
        if (node != null && node.hasChildren()) {
            Node targetNode = statusMap.get(targetIndex);
            return targetNode != null && node.getChildren().contains(targetNode.getNode());
        }
        return false;
    }

    /**
     * 通过是否含有子节点，判断是否能够接受转换
     *
     * <p>node 的子节点和 child 类型需要对应</p>
     *
     * @param sourceIndex 原索引
     * @param child       目标子节点对象
     * @return boolean 是否能够接受转换
     */
    public boolean canAcceptChild(O sourceIndex, Object child) {
        Node node = statusMap.get(sourceIndex);
        if (node != null && node.hasChildren()) {
            return node.getChildren().contains(child);
        }
        return false;
    }
}
