package top.cutexingluo.tools.utils.se.algo.cpp.structure.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.base.IData;
import top.cutexingluo.tools.designtools.method.ClassMaker;
import top.cutexingluo.tools.utils.se.algo.cpp.base.BaseBiNodeSource;
import top.cutexingluo.tools.utils.se.algo.cpp.base.BaseNodeIterator;

import java.util.*;

/**
 * Treap 树
 * <p>平衡二叉树</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/28 12:40
 * @since 1.0.3
 */
public class Treap<T extends Comparable<? super T>> extends AbstractSet<T> {

    /**
     * 根节点
     */
    protected Node<T> root;

    /**
     * 随机数
     */
    protected static Random rd = new Random(System.currentTimeMillis());


    /**
     * 插入节点数量
     */
    protected int num;

    @Data
    @AllArgsConstructor
    public static class Node<T> implements BaseBiNodeSource<Node<T>>, IData<T> {
        /**
         * 数据值
         */
        private T data;
        /**
         * 优先级
         */
        private int priority;
        /**
         * 父节点
         */
        private Node<T> parent;
        /**
         * 左节点
         */
        private Node<T> leftNode;
        /**
         * 右节点
         */
        private Node<T> rightNode;


        public Node(T data, Node<T> parent, Node<T> leftNode, Node<T> rightNode) {
            this(data, rd.nextInt(), parent, leftNode, rightNode);
        }

        @Override
        public Node<T> parentNode() {
            return parent;
        }

        @Override
        public Node<T> leftNode() {
            return leftNode;
        }

        @Override
        public Node<T> rightNode() {
            return rightNode;
        }

        protected T takeData(Node<T> node) {
            return node == null ? null : node.data;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    ", priority=" + priority +
                    ", parent=" + takeData(parent) +
                    ", leftNode=" + takeData(leftNode) +
                    ", rightNode=" + takeData(rightNode) +
                    '}';
        }

        @Override
        public void setParentNode(Node<T> parentNode) {
            this.parent = parentNode;
        }

        @Override
        public T data() {
            return data;
        }
    }

    public Treap() {
        root = null;
        num = 0;
    }

    @Override
    public void clear() {
        root = null;
        num = 0;
    }

    @Override
    public int size() {
        return num;
    }

    @Override
    public boolean isEmpty() {
        return root == null || num == 0;
    }

    @Override
    public boolean add(T t) {
        insert(t);
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        if (o instanceof Node) {
            Node<T> node = ClassMaker.cast(o, obj -> (Node<T>) obj);
            return node != null && getNode(node.data) != null;
        } else {
            T res = ClassMaker.cast(o, obj -> (T) obj);
            return res != null && getNode(res) != null;
        }
    }

    /**
     * 中序遍历
     */
    public LinkedList<T> inOrderTree() {
        return (LinkedList<T>) printTree(root, new LinkedList<>());
    }

    @Override
    public boolean remove(Object o) {
        if (o != null) {
            if (o instanceof Node) {
                return removeTask((T) ((Node<?>) o).data);
            } else {
                T res = ClassMaker.cast(o, obj -> (T) obj);
                return res != null && removeTask(res);
            }
        }
        return false;
    }

    //-----------------------------
    protected void insert(T data) {
        this.insert(data, rd.nextInt());
    }

    /**
     * 添加节点
     */
    protected void insert(T data, int priority) {
        Node<T> newNode = new Node<>(data, priority, null, null, null);
        if (root == null) {
            root = newNode;
            num++; // 增加数量
        } else {
            Node<T> current = root, parent = current;
            int compare = 0;
            while (current != null) {
                parent = current;
                compare = data.compareTo(current.data);
                if (compare > 0) {
                    current = current.rightNode;
                } else { // 默认左子树
                    current = current.leftNode;
                }
            }
            if (compare > 0) {
                parent.rightNode = newNode;
            } else {
                parent.leftNode = newNode;
            }
            newNode.parent = parent;
            reBalance(newNode);
            num++; // 增加数量
        }
    }

    protected void reBalance(Node<T> node) {
        //1.递归
        if (node.parent != null) {
            Integer priority = node.priority;
            int compare = priority.compareTo(node.parent.priority);
            if (compare < 0) {
                if (node == node.parent.leftNode) {
                    rotateWithLeftChild(node);
                    reBalance(node);
                } else if (node == node.parent.rightNode) {
                    rotateWithRightChild(node);
                    reBalance(node);
                }
            }
        }
    }


    /**
     * 右旋
     */
    protected void rotateWithLeftChild(Node<T> lt) {
        Node<T> p = pushup(lt);
        p.leftNode = lt.rightNode;
        if (lt.rightNode != null) {
            lt.rightNode.parent = p;
        }
        p.parent = lt;
        lt.rightNode = p;
        root = root == p ? lt : root;
    }


    /**
     * 左旋
     */
    protected void rotateWithRightChild(Node<T> rt) {
        Node<T> p = pushup(rt);
        p.rightNode = rt.leftNode;
        if (rt.leftNode != null) {
            rt.leftNode.parent = p;
        }
        p.parent = rt;
        rt.leftNode = p;
        root = root == p ? rt : root;
    }

    @NotNull
    private Node<T> pushup(Node<T> node) {
        Node<T> p = node.parent;
        node.parent = p.parent;
        if (p.parent != null) {
            if (p == p.parent.leftNode) {
                p.parent.leftNode = node;
            } else {
                p.parent.rightNode = node;
            }
        }
        return p;
    }


    /**
     * 获取元素
     */
    protected Node<T> getNode(T data) {
        Node<T> current = root;
        if (current == null) {
            return null;
        } else {
            int cmp = 0;
            while (current != null) {
                cmp = data.compareTo(current.data);
                if (cmp > 0) {
                    current = current.rightNode;
                } else if (cmp < 0) {
                    current = current.leftNode;
                } else {
                    return current;
                }
            }
            return null;
        }
    }

    /**
     * 获取元素
     */
    protected Node<T> likeNode(Node<T> node) {
        return getNode(node.data);
    }

    protected boolean removeTask(T data) {
        Node<T> node = getNode(data);
        if (node != null) {
            if (node.leftNode() == null && node.rightNode() == null) { //如果是叶子节点
                removeNeitherLeftAndRight(node);
            } else if (node.leftNode() != null && node.rightNode() == null) { //只有左子节点
                removeOnlyHasLeft(node);
            } else if (node.rightNode() != null && node.leftNode() == null) { //只有右子节点
                removeOnlyHasRight(node);
            } else { //既有左子树又有右子树
                removeHaveLeftAndRight(node);
            }
            num--; //减少数量
        }
        return node != null;
    }

    protected void removeNeitherLeftAndRight(Node<T> node) {
        if (root == node) {
            root = null;
        } else {
            if (node == node.parent.leftNode()) {
                node.parent.leftNode = null;
                node.parent = null;
            } else {
                node.parent.rightNode = null;
                node.parent = null;
            }
        }
    }

    protected void removeOnlyHasLeft(Node<T> node) {
        node.leftNode().parent = node.parent;
        if (node.parent != null) {
            if (node == node.parent.leftNode()) {
                node.parent.leftNode = node.leftNode();
            } else {
                node.parent.rightNode = node.leftNode();
            }
        }

        if (root == node) {
            root = node.leftNode();
        }
        node.parent = node.leftNode = node.rightNode = null;
    }

    protected void removeOnlyHasRight(Node<T> node) {
        node.rightNode().parent = node.parent;
        if (node.parent != null) {
            if (node == node.parent.leftNode()) {
                node.parent.leftNode = node.rightNode();
            } else {
                node.parent.rightNode = node.rightNode();
            }
        }

        if (root == node) {
            root = node.rightNode();
        }
        node.parent = node.leftNode = node.rightNode = null;
    }

    protected void removeHaveLeftAndRight(Node<T> node) {
        int result = 0;
        while (node.leftNode() != null && node.rightNode() != null) {
            result = node.leftNode().priority - node.rightNode().priority;
            if (result <= 0) { //左子节点的修改值小于等于右子节点，则进行右旋
                rotateWithLeftChild(node.leftNode());
            } else {
                rotateWithRightChild(node.rightNode());
            }
        }
        if (node.leftNode() == null && node.rightNode() == null) { //如果是叶子节点
            removeNeitherLeftAndRight(node);
        } else if (node.leftNode() != null && node.rightNode() == null) { //只有左子节点
            removeOnlyHasLeft(node);
        } else if (node.rightNode() != null && node.leftNode() == null) { //只有右子节点
            removeOnlyHasRight(node);
        }
    }


    /**
     * 广度优先遍历
     */
    public List<Node<T>> breadthFirstSearch() {
        return cBreadthFirstSearch(root);
    }

    private List<Node<T>> cBreadthFirstSearch(Node<T> node) {
        List<Node<T>> nodes = new ArrayList<>();
        Deque<Node<T>> deque = new ArrayDeque<>();
        if (node != null) {
            deque.offer(node);
        }
        while (!deque.isEmpty()) {
            Node<T> first = deque.poll();
            nodes.add(first);
            if (first.leftNode() != null) {
                deque.offer(first.leftNode());
            }
            if (first.rightNode() != null) {
                deque.offer(first.rightNode());
            }
        }
        return nodes;
    }


    /**
     * 中序遍历
     */
    protected Queue<T> printTree(Node<T> node, Queue<T> newQueue) {
        if (node != null) {
            printTree(node.leftNode, newQueue);
            newQueue.add(node.data);
            printTree(node.rightNode, newQueue);
        }
        return newQueue;
    }


    /**
     * 最大值
     */
    public T maxData() {
        Node<T> current = root;
        Node<T> maxNode = current;
        while (current != null) {
            maxNode = current;
            current = current.rightNode;
        }
        if (maxNode != null) {
            return maxNode.data;
        } else {
            return null;
        }
    }

    /**
     * 最小值
     */
    public T minData() {
        Node<T> current = root;
        Node<T> minNode = current;
        while (current != null) {
            minNode = current;
            current = current.leftNode;
        }
        if (minNode != null) {
            return minNode.data;
        } else {
            return null;
        }
    }

    /**
     * 前驱
     */
    public T frontData(T data) {
        Node<T> current = root;
        Node<T> frontNode = null;
        int result = 0;
        while (current != null) {
            result = data.compareTo(current.data);
            if (result == 0) {
                return current.data;
            } else if (result > 0) {
                frontNode = current;
                current = current.rightNode;
            } else {
                current = current.leftNode;
            }
        }
        if (frontNode != null) {
            return frontNode.data;
        }
        return null;
    }

    /**
     * 后继
     */
    public T descendantData(T data) {
        Node<T> current = root;
        Node<T> frontNode = null;
        int result = 0;
        while (current != null) {
            result = data.compareTo(current.data);
            if (result == 0) {
                return current.data;
            } else if (result < 0) {
                frontNode = current;
                current = current.leftNode;
            } else {
                current = current.rightNode;
            }
        }
        if (frontNode != null) {
            return frontNode.data;
        }
        return null;
    }


    @Override
    public Iterator<T> iterator() {
        return new BBTreeIterator(size(), root);
    }


    final class BBTreeIterator extends BaseNodeIterator<Node<T>, T> {
        public BBTreeIterator(int length, Node<T> root) {
            super(length, root);
        }

        @Override
        public void remove() {
            if (currentNode != null) {
                removeTask(currentNode.data);
            }
        }
    }
}
