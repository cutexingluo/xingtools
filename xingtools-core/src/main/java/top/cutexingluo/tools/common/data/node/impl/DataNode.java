package top.cutexingluo.tools.common.data.node.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.cutexingluo.tools.common.base.IDataValue;

/**
 * 节点类/ 结点类
 * <p>基础数据类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/9 11:10
 * @since 1.1.6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataNode<T> implements IDataValue<T> {

    /**
     * 值
     */
    protected T value;
}
