package top.cutexingluo.tools.utils.se.algo.cpp.base;


import java.util.Map;

/**
 * 带 Entry 的 二叉树
 * <p>同时实现自身</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/10 16:10
 * @since 1.0.3
 */
public interface BaseEntryBiNode<T extends BaseBiNode<T> & Map.Entry<K, V>, K, V> extends BaseBiNode<T>, Map.Entry<K, V> {
}
