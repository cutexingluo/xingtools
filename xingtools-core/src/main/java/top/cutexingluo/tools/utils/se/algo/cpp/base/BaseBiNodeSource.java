package top.cutexingluo.tools.utils.se.algo.cpp.base;

/**
 * 节点源接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/24 11:41
 * @since 1.0.3
 */
public interface BaseBiNodeSource<T> extends BaseBiNode<T> {
    void setLeftNode(T leftNode);

    void setRightNode(T rightNode);

    void setParentNode(T parentNode);
}
