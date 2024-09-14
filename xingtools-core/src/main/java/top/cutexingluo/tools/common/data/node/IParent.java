package top.cutexingluo.tools.common.data.node;

/**
 * 父节点接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/10 18:07
 * @since 1.1.4
 */
public interface IParent<T> {

    /**
     * 获取父节点
     */
    T getParent();
}
