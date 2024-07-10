package top.cutexingluo.tools.utils.se.algo.cpp.base;


import top.cutexingluo.tools.common.base.IData;

import java.util.LinkedList;
import java.util.Map;

/**
 * 兼容Node 和Entry 的迭代器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/10 16:15
 * @since 1.0.3
 */
public abstract class BaseEntryBiNodeIterator<T extends BaseEntryBiNode<T, K, V> & IData<Map.Entry<K, V>>, K, V> extends BaseBiNodeIterator<T, Map.Entry<K, V>> {


    public BaseEntryBiNodeIterator(T root) {
        super(null, -1, root);
    }

    public BaseEntryBiNodeIterator(int length, T root) {
        super(length, root);
    }

    public BaseEntryBiNodeIterator(LinkedList<T> linkedList, T root) {
        super(linkedList, -1, root);
    }

    public BaseEntryBiNodeIterator(LinkedList<T> linkedList, int length, T root) {
        super(linkedList, length, root);
    }

    /**
     * 默认中序遍历
     * <p>常用于二叉搜索树，返回有序的节点</p>
     */
    @Override
    public Map.Entry<K, V> next() {
        return inOrderNext();
    }

}
