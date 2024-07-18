package top.cutexingluo.tools.utils.se.algo.cpp.base;

/**
 * 基本二叉树节点接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/27 15:03
 * @since 1.0.3
 */
public interface BaseNode<T> {

    /**
     * 左节点
     */
    T leftNode();

    /**
     * 右节点
     */
    T rightNode();

}
