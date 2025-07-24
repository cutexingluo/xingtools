package top.cutexingluo.tools.basepackage.chain.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Basic Task Node
 * <p>TaskNode 基础实现</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/6/6 16:33
 * @since 1.1.7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicTaskNode<V> implements TaskNode<V> {

    private V value;

    private boolean complete;

    private boolean change;

    public BasicTaskNode(V value) {
        this.value = value;
    }

    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }

}
