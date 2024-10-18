package top.cutexingluo.tools.common.data.node.impl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * String 类型节点类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/10 9:38
 * @since 1.1.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StrDataNode extends DataNode<String> {

    public StrDataNode(String value) {
        super(value);
    }
}
