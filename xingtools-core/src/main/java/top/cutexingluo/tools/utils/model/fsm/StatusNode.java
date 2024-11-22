package top.cutexingluo.tools.utils.model.fsm;

import lombok.Data;
import top.cutexingluo.tools.common.data.Tuple;
import top.cutexingluo.tools.utils.model.fsm.base.BaseStatusNode;

import java.util.Set;

/**
 * 状态节点
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/3 14:44
 * @since 1.1.4
 */
@Data
public class StatusNode<T, S> implements BaseStatusNode<T, S>, Tuple<T, Set<S>> {

    protected T node;

    protected Set<S> nextNodes;

    public StatusNode() {
    }

    public StatusNode(T node, Set<S> nextNodes) {
        this.node = node;
        this.nextNodes = nextNodes;
    }

    @Override
    public T getKey() {
        return node;
    }

    @Override
    public Set<S> getValue() {
        return nextNodes;
    }


    @Override
    public T setKey(T key) {
        final T oldValue = node;
        node = key;
        return oldValue;
    }

    @Override
    public Set<S> setValue(Set<S> value) {
        final Set<S> oldValue = nextNodes;
        nextNodes = value;
        return oldValue;
    }

    @Override
    public Set<S> getChildren() {
        return nextNodes;
    }

    @Override
    public boolean hasChildren() {
        return nextNodes != null && !nextNodes.isEmpty();
    }
}
