package top.cutexingluo.tools.utils.se.algo.cpp.base;


import top.cutexingluo.tools.common.base.IData;

import java.util.LinkedList;

/**
 * 二叉树迭代器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/10 15:25
 * @since 1.0.3
 */
public abstract class BaseBiNodeIterator<T extends BaseNode<T> & IData<V>, V> extends BaseNodeIterator<T, V> {

    public BaseBiNodeIterator(T root) {
        this(null, -1, root);
    }

    public BaseBiNodeIterator(int length, T root) {
        this(null, length, root);
    }

    public BaseBiNodeIterator(LinkedList<T> linkedList, T root) {
        this(linkedList, -1, root);
    }

    public BaseBiNodeIterator(LinkedList<T> linkedList, int length, T root) {
        super(linkedList, length, root);
    }

    @Override
    protected void init() {
        if (root != null) {
            this.currentNode = root;
            linkedList.add(root);
        }
    }

    @Override
    public boolean hasNext() {
        return currentNode != null || super.hasNext();
    }

    /**
     * 默认中序遍历
     * <p>常用于二叉搜索树，返回有序的节点</p>
     */
    @Override
    public V next() {
        return inOrderNext().data();
    }


}
