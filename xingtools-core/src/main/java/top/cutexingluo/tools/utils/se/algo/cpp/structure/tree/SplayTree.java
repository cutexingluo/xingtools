package top.cutexingluo.tools.utils.se.algo.cpp.structure.tree;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.base.IData;
import top.cutexingluo.tools.designtools.method.ClassMaker;
import top.cutexingluo.tools.utils.se.algo.cpp.base.BaseBiNodeSource;
import top.cutexingluo.tools.utils.se.algo.cpp.base.BaseNodeIterator;

import java.util.*;

/**
 * Splay 树
 * <p>平衡二叉树</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/28 12:40
 * @since 1.0.3
 */
public class SplayTree<T extends Comparable<T>> extends AbstractSet<T> {

    /**
     * 根节点
     */
    protected Node<T> root;

    /**
     * 数目
     */
    protected int num;


    @Data
    public static class Node<T> implements BaseBiNodeSource<Node<T>>, IData<T> {
        private T data;
        private Node<T> parent;
        private Node<T> left;
        private Node<T> right;

        public Node(T data) {
            this(data, null, null, null);
        }

        public Node(T data, Node<T> parent) {
            this(data, parent, null, null);
        }

        public Node(T data, Node<T> parent, Node<T> left, Node<T> right) {
            this.data = data;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        @Override
        public Node<T> parentNode() {
            return parent;
        }

        @Override
        public Node<T> leftNode() {
            return left;
        }

        @Override
        public Node<T> rightNode() {
            return right;
        }

        protected T takeData(Node<T> node) {
            return node == null ? null : node.data;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    ", parent=" + takeData(parent) +
                    ", left=" + takeData(left) +
                    ", right=" + takeData(right) +
                    '}';
        }

        @Override
        public void setLeftNode(Node<T> leftNode) {
            this.left = leftNode;
        }

        @Override
        public void setRightNode(Node<T> rightNode) {
            this.right = rightNode;
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

    public SplayTree() {
        root = null;
        num = 0;
    }

    @Override
    public void clear() {
        root = null;
        num = 0;
    }

    public Node<T> getRoot() {
        return root;
    }

    @Override
    public int size() {
        return num;
    }

    @Override
    public boolean isEmpty() {
        return root == null || size() == 0;
    }


    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        if (o instanceof Node) {
            Node<T> node = ClassMaker.cast(o, obj -> (Node<T>) obj);
            return node != null && find(node.data) != null;
        } else {
            T res = ClassMaker.cast(o, obj -> (T) obj);
            return res != null && find(res) != null;
        }
    }

    @Override
    public boolean add(T t) {
        return insert(t);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        if (o instanceof Node) {
            Node<T> node = ClassMaker.cast(o, obj -> (Node<T>) obj);
            return node != null && remove(node.data);
        } else {
            T res = ClassMaker.cast(o, obj -> (T) obj);
            return res != null && remove(res);
        }
    }

    //----------------

    /**
     * 添加节点
     * <p>新添加的节点旋转到根节点位置</p>
     *
     * @param data 数据
     */
    protected boolean insert(T data) {
        Node<T> current = root;
        if (current == null) {
            root = new Node<T>(data);
        } else {
            int cmp = 0;
            Node<T> parent = current;
            while (current != null) {
                parent = current;
                cmp = data.compareTo(current.data);
                if (cmp > 0) {
                    current = current.right;
                } else {
                    current = current.left;
                }
            }
            Node<T> newNode = new Node<T>(data, parent);
            if (cmp > 0) {
                parent.right = newNode;
            } else {
                parent.left = newNode;
            }
            splay(newNode, this);
        }
        num++; //数目增加
        return true;
    }


    /**
     * 删除节点
     * <p>该值旋转为右父根节点，该值的右子节点转为左子节点的右子节点</p>
     *
     * @param data 目标数据
     */
    protected boolean remove(T data) {
        Node<T> del = find(data);
        if (del != null) {
            splay(del, this);
            Node<T> frontNodeOfRoot = frontNode(root.data);
            splayLeft(frontNodeOfRoot, this);
            if (root.left == null) { // 如果为空
                if (root.right != null) {
                    root.right.parent = null;
                }
                root = root.right;
            } else {
                root.left.right = root.right;
                if (root.right != null) {
                    root.right.parent = root.left;
                }
                root = root.left;
                root.parent.left = root.parent.right = null;
                root.parent = null;
            }
            num--; //数目减少
            return true;
        }
        return false;
    }


    private void rotate(Node<T> node, SplayTree<T> tree) {
        if (node == node.parent.left) {
            if (node.parent.parent == null) {
                tree.rotateWithLeftChild(node);
            } else if (node.parent == node.parent.parent.left) {
                tree.rotateWithLongLeftChild(node);
            } else if (node.parent == node.parent.parent.right) {
                tree.rotateWithBreakLeftChild(node);
            }
        } else if (node == node.parent.right) {
            if (node.parent.parent == null) {
                tree.rotateWithRightChild(node);
            } else if (node.parent == node.parent.parent.right) {
                tree.rotateWithLongRightChild(node);
            } else if (node.parent == node.parent.parent.left) {
                tree.rotateWithBreakRightChild(node);
            }
        }
    }

    /**
     * 根前驱旋转为根节点的左子节点
     *
     * @param node 当前节点
     * @param tree 树
     */
    protected void splayLeft(Node<T> node, SplayTree<T> tree) {
        if (node == null) return;
        while (node.parent != root && node.parent != null) {
            rotate(node, tree);
        }
    }

    /**
     * 旋转为根节点
     *
     * @param node 当前节点
     * @param tree 树
     */
    protected void splay(Node<T> node, SplayTree<T> tree) {
        if (node == null) return;
        while (node.parent != null) {
            rotate(node, tree);
        }
    }


    /**
     * 左孩子右旋
     *
     * @param now 当前节点
     */
    protected void rotateWithLeftChild(Node<T> now) {
        Node<T> tmp = now.parent;
        now.parent = tmp.parent;
        root = now;
        tmp.left = now.right;
        if (now.right != null) {
            now.right.parent = tmp;
        }
        now.right = tmp;
        tmp.parent = now;
    }

    /**
     * 右孩子左旋
     *
     * @param now 当前节点
     */
    protected void rotateWithRightChild(Node<T> now) {
        Node<T> tmp = now.parent;
        now.parent = tmp.parent;
        root = now;
        tmp.right = now.left;
        if (now.left != null) {
            now.left.parent = tmp;
        }
        now.left = tmp;
        tmp.parent = now;
    }

    /**
     * 长链右旋
     *
     * @param now 当前节点
     */
    protected void rotateWithLongLeftChild(Node<T> now) {
        Node<T> tmp = now.parent;
        Node<T> top = tmp.parent;

        now.parent = top.parent;
        if (top.parent != null) {
            if (top.parent.left == top) {
                top.parent.left = now;
            } else {
                top.parent.right = now;
            }
        }

        tmp.left = now.right;
        if (now.right != null) {
            now.right.parent = tmp;
        }
        top.left = tmp.right;
        if (tmp.right != null) {
            tmp.right.parent = top;
        }

        tmp.right = top;
        top.parent = tmp;

        now.right = tmp;
        tmp.parent = now;

        root = now.parent == null ? now : root;
    }

    /**
     * 长链左旋
     *
     * @param now 当前节点
     */
    protected void rotateWithLongRightChild(Node<T> now) {
        Node<T> tmp = now.parent;
        Node<T> top = tmp.parent;

        now.parent = top.parent;
        if (top.parent != null) {
            if (top.parent.left == top) {
                top.parent.left = now;
            } else {
                top.parent.right = now;
            }
        }

        tmp.right = now.left;
        if (now.left != null) {
            now.left.parent = tmp;
        }
        top.right = tmp.left;
        if (tmp.left != null) {
            tmp.left.parent = top;
        }

        tmp.left = top;
        top.parent = tmp;

        now.left = tmp;
        tmp.parent = now;
        root = now.parent == null ? now : root;
    }

    /**
     * 断点双右旋
     *
     * @param now 当前节点
     */
    protected void rotateWithBreakLeftChild(Node<T> now) {
        Node<T> tmp = now.parent;
        Node<T> top = tmp.parent;

        now.parent = top.parent;
        if (top.parent != null) {
            if (top == top.parent.left) {
                top.parent.left = now;
            } else {
                top.parent.right = now;
            }
        }

        top.right = now.left;
        if (now.left != null) {
            now.left.parent = top;
        }
        tmp.left = now.right;
        if (now.right != null) {
            now.right.parent = tmp;
        }

        now.left = top;
        top.parent = now;
        now.right = tmp;
        tmp.parent = now;

        root = now.parent == null ? now : root;
    }

    /**
     * 断点双左旋
     *
     * @param now 当前节点
     */
    protected void rotateWithBreakRightChild(Node<T> now) {
        Node<T> tmp = now.parent;
        Node<T> top = tmp.parent;

        now.parent = top.parent;
        if (top.parent != null) {
            if (top == top.parent.left) {
                top.parent.left = now;
            } else {
                top.parent.right = now;
            }
        }

        top.left = now.right;
        if (now.right != null) {
            now.right.parent = top;
        }
        tmp.right = now.left;
        if (now.left != null) {
            now.left.parent = tmp;
        }

        now.right = top;
        top.parent = now;
        now.left = tmp;
        tmp.parent = now;

        root = now.parent == null ? now : root;
    }

    /**
     * 两树合并
     * <p>目标树的最小节点必须大于等于当前树的最大节点</p>
     *
     * @param tree 第二棵树
     * @return this or null if tree.minNode().data < this.maxNode().data
     */
    public SplayTree<T> combine(@NotNull SplayTree<T> tree) {
        Node<T> maxNode = maxNode();
        splay(maxNode, this);
        if (tree.minNode().data.compareTo(root.data) < 0) {
            return null;
        } else {
            root.right = tree.root;
            if (tree.root != null) {
                tree.root.parent = root;
            }
        }
        return this;
    }


    /**
     * data 分裂成左(小)右(大)两树
     *
     * @param data 数据
     * @return 树列表
     */
    public List<SplayTree<T>> split(T data) {
        List<SplayTree<T>> trees = new ArrayList<>();
        SplayTree<T> tree1;
        SplayTree<T> tree2;

        Node<T> node = find(data);
        if (node == null) {
            node = frontNode(data);
            splay(node, this);
            tree1 = new SplayTree<>();
            tree2 = new SplayTree<>();
            tree1.root = root;
            tree2.root = root.right;

            if (root.right != null) {
                root.right.parent = null;
            }
            root.right = null;

            trees.add(tree1);
            trees.add(tree2);
        } else {
            splay(node, this);
            tree1 = new SplayTree<>();
            tree2 = new SplayTree<>();
            tree1.root = root.left;
            tree2.root = root.right;

            if (root.left != null) {
                root.left.parent = null;
            }
            if (root.right != null) {
                root.right.parent = null;
            }
            root.left = null;
            root.right = null;

            trees.add(tree1);
            trees.add(tree2);
        }
        return trees;
    }


    /**
     * @return 树的最大节点
     */
    public Node<T> maxNode() {
        if (root == null) return null;
        Node<T> current = root;
        Node<T> maxNode = null;
        while (current != null) {
            maxNode = current;
            current = current.right;
        }
        return maxNode;

    }

    /**
     * @return 该树的最小节点
     */
    public Node<T> minNode() {
        if (root == null) return null;
        Node<T> current = root;
        Node<T> minNode = null;
        while (current != null) {
            minNode = current;
            current = current.left;
        }
        return minNode;
    }


    /**
     * 搜索节点
     *
     * @param data 数据
     */
    public Node<T> find(T data) {
        Node<T> current = root;
        if (current == null) return null;
        while (current != null) {
            int cmp = data.compareTo(current.data);
            if (cmp > 0) {
                current = current.right;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                return current;
            }
        }
        return null;
    }

    /**
     * @param data 数据
     * @return 前驱节点
     */
    public Node<T> frontNode(T data) {
        Node<T> node = find(data);
        if (root == null) return null;
        Node<T> current = node;
        Node<T> frontNode = null;  //前驱节点
        while (current != null) {
            int cmp = node.data.compareTo(current.data);
            if (cmp > 0) {
                frontNode = current;
                current = current.right;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                return current;
            }
        }
        return frontNode;
    }

    /**
     * @param data 数据
     * @return 后继节点
     */
    public Node<T> descendantNode(T data) {
        Node<T> node = find(data);
        if (root == null) return null;
        Node<T> current = node;
        Node<T> descendantNode = null;  //前驱节点
        while (current != null) {
            int cmp = node.data.compareTo(current.data);
            if (cmp > 0) {
                current = current.right;
            } else if (cmp < 0) {
                descendantNode = current;
                current = current.left;
            } else {
                return current;
            }
        }
        return descendantNode;
    }


    /**
     * @return 广度优先遍历树 获取节点
     */
    public ArrayList<Node<T>> bfs() {
        return bfs(root);
    }

    /**
     * @return 广度优先遍历树 获取数据
     */
    public ArrayList<T> bfsData() {
        return printTree(root);
    }


    protected ArrayList<Node<T>> bfs(Node<T> node) {
        ArrayList<Node<T>> nodes = new ArrayList<>();
        if (node != null) {
            Deque<Node<T>> deque = new ArrayDeque<>();
            deque.offer(node);
            while (!deque.isEmpty()) {
                Node<T> first = deque.poll();
                nodes.add(first);
                if (first.left != null) {
                    deque.offer(first.left);
                }
                if (first.right != null) {
                    deque.offer(first.right);
                }
            }
        }
        return nodes;
    }

    /**
     * bfs遍历
     */
    protected ArrayList<T> printTree(Node<T> node) {
        ArrayList<T> list = new ArrayList<>();
        if (node != null) {
            Deque<Node<T>> deque = new ArrayDeque<>();
            deque.offer(node);
            while (!deque.isEmpty()) {
                Node<T> first = deque.poll();
                list.add(first.data);
                if (first.left != null) {
                    deque.offer(first.left);
                }
                if (first.right != null) {
                    deque.offer(first.right);
                }
            }
        }
        return list;
    }


    @Override
    public Iterator<T> iterator() {
        return new SplayTreeIterator(size(), root);
    }

    final class SplayTreeIterator extends BaseNodeIterator<Node<T>, T> {

        public SplayTreeIterator(int length, Node<T> root) {
            super(length, root);
        }

        @Override
        public void remove() {
            if (currentNode != null) {
                SplayTree.this.remove(currentNode.data);
            }
        }
    }
}
