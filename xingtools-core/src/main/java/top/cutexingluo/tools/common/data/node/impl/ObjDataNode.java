package top.cutexingluo.tools.common.data.node.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Object 类型节点类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/9 11:14
 * @since 1.1.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ObjDataNode extends DataNode<Object> {

    public ObjDataNode(Object value) {
        super(value);
    }
}
