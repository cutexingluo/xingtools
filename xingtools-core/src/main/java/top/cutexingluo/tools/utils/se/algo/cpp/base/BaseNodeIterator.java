package top.cutexingluo.tools.utils.se.algo.cpp.base;


import top.cutexingluo.tools.common.base.IData;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

/**
 * 万能树节点迭代器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/10 13:41
 * @since 1.0.3
 */
public abstract class BaseNodeIterator<T extends BaseNode<T> & IData<V>, V> implements Iterator<V> {


    protected int length;
    protected final LinkedList<T> linkedList;
    protected int current;
    protected T root;
    protected T currentNode;
    /**
     * 仅后序迭代使用
     */
    protected T previousNode;

    public BaseNodeIterator(LinkedList<T> list, T root) {
        this(list, -1, root);
    }

    public BaseNodeIterator(int length, T root) {
        this(null, length, root);
    }


    public BaseNodeIterator(LinkedList<T> list, int length, T root) {
        this.linkedList = list == null ? new LinkedList<>() : list;
        this.root = root;
        this.length = length;
        init();
    }

    protected void init() {
        if (root != null) {
            linkedList.offer(root);
        }
    }


    @Override
    public boolean hasNext() {
        return this.length == -1 ? !linkedList.isEmpty() : current < length;
    }

    /**
     * 默认先序遍历
     */
    @Override
    public V next() {
        return preOrderNextDefault().data();
    }

    /**
     * 这个需要自行重写
     */
    @Override
    public void remove() {
        Iterator.super.remove();
    }

    /**
     * 先序遍历默认方法
     */
    protected T preOrderNextDefault() {
        if (!hasNext()) {
            throw new IllegalStateException("No more elements in the tree.");
        }

        currentNode = linkedList.poll();
        if (currentNode == null) {
            throw new IllegalStateException("No more elements in the tree.");
        }
        ++current;

        // 将右儿子、左儿子压入栈中，以便下一次访问
        if (currentNode.leftNode() != null) {
            linkedList.offer(currentNode.leftNode());
        }
        if (currentNode.rightNode() != null) {
            linkedList.offer(currentNode.rightNode());
        }
        return currentNode;
    }


    /**
     * 先序遍历
     */
    protected T preOrderNext() {
        T nextNode = null;
        if (currentNode != null) {
            linkedList.add(currentNode);
        }
        if (!linkedList.isEmpty()) {
            nextNode = (T) linkedList.pop();
            if (nextNode.rightNode() != null) {
                linkedList.push(nextNode.rightNode());
            }
            currentNode = nextNode.leftNode();
        }
        ++current;
        return nextNode;
    }

    /**
     * 中序遍历
     */
    protected T inOrderNext() {
        T nextNode = null;
        while (currentNode != null) {
            linkedList.push(currentNode);
            currentNode = currentNode.leftNode();
        }
        if (!linkedList.isEmpty()) {
            nextNode = (T) linkedList.pop();
            currentNode = nextNode.rightNode();
        }
        ++current;
        return nextNode;
    }

    /**
     * 后序遍历
     */
    protected T postOrderNext() {
        T nextNode = null;
        while (true) {
            if (currentNode != null) {
                linkedList.push(currentNode);
                currentNode = currentNode.leftNode();
            } else {
                currentNode = (T) linkedList.peek();
                if (currentNode != null &&
                        currentNode.rightNode() != null &&
                        currentNode.rightNode() != previousNode) {
                    currentNode = currentNode.rightNode();
                } else {
                    break;
                }
            }
        }
        currentNode = previousNode = (T) linkedList.pop();
        nextNode = currentNode;
        currentNode = null;
        ++current;
        return nextNode;
    }

    /**
     * 先序遍历
     */
    protected void preOrderTraversal(BaseNode<T> node, Consumer<BaseNode<T>> consumer) {
        if (node != null) {
            if (consumer != null) consumer.accept(node);
            preOrderTraversal(node.leftNode(), consumer);
            preOrderTraversal(node.rightNode(), consumer);
        }
    }

    /**
     * 中序遍历
     */
    protected void inOrderTraversal(BaseNode<T> node, Consumer<BaseNode<T>> consumer) {
        if (node != null) {
            inOrderTraversal(node.leftNode(), consumer);
            if (consumer != null) consumer.accept(node);
            inOrderTraversal(node.rightNode(), consumer);
        }
    }

    /**
     * 后序遍历
     */
    protected void postOrderTraversal(BaseNode<T> node, Consumer<BaseNode<T>> consumer) {
        if (node != null) {
            postOrderTraversal(node.leftNode(), consumer);
            postOrderTraversal(node.rightNode(), consumer);
            if (consumer != null) consumer.accept(node);
        }
    }
}
