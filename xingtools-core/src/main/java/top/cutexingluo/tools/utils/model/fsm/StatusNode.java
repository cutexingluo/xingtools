package top.cutexingluo.tools.utils.model.fsm;

import lombok.Data;
import top.cutexingluo.tools.common.data.Tuple;
import top.cutexingluo.tools.utils.model.fsm.base.BaseStatusNode;

import java.util.Collection;

/**
 * 状态节点
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/3 14:44
 * @since 1.1.4
 */
@Data
public class StatusNode<T, S> implements BaseStatusNode<T, S>, Tuple<T, Collection<S>> {

    protected T node;

    protected Collection<S> nextNodes;

    public StatusNode() {
    }

    public StatusNode(T node, Collection<S> nextNodes) {
        this.node = node;
        this.nextNodes = nextNodes;
    }

    @Override
    public T getKey() {
        return node;
    }

    @Override
    public Collection<S> getValue() {
        return nextNodes;
    }


    @Override
    public T setKey(T key) {
        final T oldValue = node;
        node = key;
        return oldValue;
    }

    @Override
    public Collection<S> setValue(Collection<S> value) {
        final Collection<S> oldValue = nextNodes;
        nextNodes = value;
        return oldValue;
    }

    @Override
    public Collection<S> getChildren() {
        return nextNodes;
    }

    @Override
    public boolean hasChildren() {
        return nextNodes != null && !nextNodes.isEmpty();
    }
}
