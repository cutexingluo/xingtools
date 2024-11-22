package top.cutexingluo.tools.utils.se.algo.cpp.structure.tree.rbtree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.base.IData;
import top.cutexingluo.tools.common.base.IValue;
import top.cutexingluo.tools.designtools.method.ClassMaker;
import top.cutexingluo.tools.utils.se.algo.cpp.base.BaseBiNodeSource;
import top.cutexingluo.tools.utils.se.algo.cpp.base.BaseEntryBiNode;
import top.cutexingluo.tools.utils.se.algo.cpp.base.BaseEntryBiNodeIterator;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

/**
 * 红黑树
 * <p>Red-Black Tree</p>
 *
 * <p>like {@link TreeMap}</p>
 * <p>like c++ std::map</p>
 * <p>{@link NavigableMap}接口 的实现方法不建议使用</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/27 20:54
 * @since 1.0.3
 */
public class RBTree<K extends Comparable<K>, V> extends AbstractMap<K, V> implements NavigableMap<K, V>, Iterable<Map.Entry<K, V>>, Serializable {
    public static final boolean RED = true;
    public static final boolean BLACK = false;

    /**
     * 当前节点父节点
     */
    protected RBNode<K, V> parentOf(RBNode<K, V> node) {
        if (node == null) return null;
        return node.parentNode;
    }

    /**
     * 当前节点颜色
     *
     * @param node 节点
     */
    protected boolean colorOf(RBNode<K, V> node) {
        if (node == null) return BLACK;
        return node.color;
    }

    /**
     * 判断节点是否是红色
     */
    protected boolean isRed(RBNode<K, V> node) {
        return node != null && node.isRed();
    }

    /**
     * 判断节点是否是黑色
     */
    protected boolean isBlack(RBNode<K, V> node) {
        return node != null && node.isBlack();
    }

    /**
     * 设置颜色
     * <p>true  - red , false - black</p>
     */
    protected boolean setColor(RBNode<K, V> node, boolean color) {
        if (node != null) {
            node.setColor(color);
            return true;
        }
        return false;
    }


    /**
     * 节点
     *
     * @param <K> the type of key
     * @param <V> the type of value
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class RBNode<K extends Comparable<K>, V> implements BaseBiNodeSource<RBNode<K, V>>, BaseEntryBiNode<RBNode<K, V>, K, V>, IData<Entry<K, V>>, IValue<V> {
        private RBNode<K, V> parentNode;
        private RBNode<K, V> leftNode;
        private RBNode<K, V> rightNode;
        private K key;
        private V value;
        private boolean color;

        @Override
        public V setValue(V value) {
            return this.value = value;
        }


        public RBNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.color = RED; // red
        }

        public RBNode(Entry<K, V> entry) {
            this(entry.getKey(), entry.getValue());
        }

        /**
         * @since 1.0.4
         */
        @Override
        public boolean flag() {
            return color;
        }

        /**
         * @since 1.0.4
         */
        @Override
        public void setFlag(boolean flag) {
            this.color = flag;
        }

        @Override
        public RBNode<K, V> parentNode() {
            return parentNode;
        }

        @Override
        public RBNode<K, V> leftNode() {
            return leftNode;
        }

        @Override
        public RBNode<K, V> rightNode() {
            return rightNode;
        }

        public boolean isRed() {
            return color == RED;
        }

        public boolean isBlack() {
            return color == BLACK;
        }

        @Override
        public String toString() {
            return "RBNode{" +
                    "key=" + key +
                    ", value=" + value +
                    ", color=" + (color == RED ? "R" : "B") +
                    '}';
        }

        @Override
        public Entry<K, V> data() {
            return this;
        }
    }

    /**
     * 元素数量
     */
    protected transient int size;
    /**
     * 根节点
     */
    protected transient RBNode<K, V> root;

    public RBTree() {
        comparator = getComparator();
    }

    public RBTree(boolean isDesc) {
        this.isDesc = isDesc;
        comparator = getComparator();
    }

    public RBTree(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    /**
     * 是否降序
     */
    protected transient boolean isDesc;

    /**
     * 比较器
     */
    protected transient Comparator<? super K> comparator;

    // no set
    public boolean isDesc() {
        return isDesc;
    }


    protected Comparator<? super K> getComparator() {
        if (isDesc) {
            return (Comparator.reverseOrder());
        }
        return (Comparator.naturalOrder());
    }

    @Override
    public Comparator<? super K> comparator() {
        return comparator;
    }

    public RBNode<K, V> getRoot() {
        return root;
    }

    @Override
    public int size() {
        return size;
    }


    protected void init() {
        size = 0;
        root = null;
    }

    @Override
    public void clear() {
        init();
    }

    /**
     * 复制一个 RBTree 的属性
     *
     * @param reversed 是否反转 comparator
     * @return 新的 RBTree
     */
    public RBTree<K, V> copyProperties(boolean reversed) {
        if (reversed) return new RBTree<K, V>(comparator.reversed());
        return new RBTree<K, V>(comparator);
    }


    @Override
    public Entry<K, V> lowerEntry(K key) {
        return predecessor(key);
    }

    @Override
    public K lowerKey(K key) {
        RBNode<K, V> predecessor = predecessor(key);
        if (predecessor == null) return null;
        return predecessor.getKey();
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        RBNode<K, V> node = search(key);
        RBNode<K, V> predecessor = predecessor(node);
        if (predecessor == null) return node;
        return predecessor;
    }

    @Override
    public K floorKey(K key) {
        RBNode<K, V> node = search(key);
        RBNode<K, V> predecessor = predecessor(node);
        if (predecessor == null) return key;
        return predecessor.getKey();
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        RBNode<K, V> node = search(key);
        RBNode<K, V> successor = successor(node);
        if (successor == null) return node;
        return successor;
    }

    @Override
    public K ceilingKey(K key) {
        RBNode<K, V> node = search(key);
        RBNode<K, V> successor = successor(node);
        if (successor == null) return key;
        return successor.getKey();
    }

    @Override
    public Entry<K, V> higherEntry(K key) {
        return successor(key);
    }

    @Override
    public K higherKey(K key) {
        RBNode<K, V> successor = successor(key);
        if (successor == null) return null;
        return successor.getKey();
    }

    @Override
    public Entry<K, V> firstEntry() {
        return minimum();
    }

    @Override
    public Entry<K, V> lastEntry() {
        return maximum();
    }

    @Override
    public Entry<K, V> pollFirstEntry() {
        RBNode<K, V> node = minimum();
        if (node == null) return null;
        removeNode(node);
        return node;
    }

    @Override
    public Entry<K, V> pollLastEntry() {
        RBNode<K, V> node = maximum();
        if (node == null) return null;
        removeNode(node);
        return node;
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        RBTree<K, V> tree = copyProperties(true);
        tree.putAll(this);
        return tree;
    }

    @Override
    public NavigableSet<K> navigableKeySet() {
        TreeSet<K> set = new TreeSet<>(comparator);
        set.addAll(this.keySet());
        return set;
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        TreeSet<K> set = new TreeSet<>(comparator.reversed());
        set.addAll(this.keySet());
        return set;
    }

    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        if (fromKey == null || toKey == null) throw new IllegalArgumentException("fromKey or toKey is null");
        RBTree<K, V> tree = copyProperties(false);
        for (Entry<K, V> entry : this) {
            int cmpFrom = comparator.compare(entry.getKey(), fromKey);
            int cmpTo = comparator.compare(entry.getKey(), toKey);
            if (cmpFrom < 0) continue;
            if (!fromInclusive && cmpFrom == 0) continue;
            if (!toInclusive && cmpTo == 0) break;
            if (cmpTo > 0) break;
            tree.insert(entry);
        }
        return tree;
    }

    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        if (toKey == null) throw new IllegalArgumentException("toKey is null");
        RBTree<K, V> tree = copyProperties(false);
        for (Entry<K, V> entry : this) {
            int cmpTo = comparator.compare(entry.getKey(), toKey);
            if (!inclusive && cmpTo == 0) break;
            if (cmpTo > 0) break;
            tree.insert(entry);
        }
        return tree;
    }

    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        if (fromKey == null) throw new IllegalArgumentException("fromKey is null");
        RBTree<K, V> tree = copyProperties(false);
        for (Entry<K, V> entry : this) {
            int cmpFrom = comparator.compare(entry.getKey(), fromKey);
            if (cmpFrom < 0) continue;
            if (!inclusive && cmpFrom == 0) continue;
            tree.insert(entry);
        }
        return tree;
    }


    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        if (fromKey == null || toKey == null) throw new IllegalArgumentException("fromKey or toKey is null");
        RBTree<K, V> tree = copyProperties(false);
        for (Entry<K, V> entry : this) {
            int cmpFrom = comparator.compare(entry.getKey(), fromKey);
            int cmpTo = comparator.compare(entry.getKey(), toKey);
            if (cmpFrom < 0) continue;
            if (cmpTo > 0) break;
            tree.insert(entry);
        }
        return tree;
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        if (toKey == null) throw new IllegalArgumentException(" toKey is null");
        RBTree<K, V> tree = copyProperties(false);
        for (Entry<K, V> entry : this) {
            int cmpTo = comparator.compare(entry.getKey(), toKey);
            if (cmpTo > 0) break;
            tree.insert(entry);
        }
        return tree;
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        if (fromKey == null) throw new IllegalArgumentException("fromKey  is null");
        RBTree<K, V> tree = copyProperties(false);
        for (Entry<K, V> entry : this) {
            int cmpFrom = comparator.compare(entry.getKey(), fromKey);
            if (cmpFrom < 0) continue;
            tree.insert(entry);
        }
        return tree;
    }

    @Override
    public K firstKey() {
        return minimumKey();
    }

    @Override
    public K lastKey() {
        return maximumKey();
    }

    /**
     * 前序遍历
     */
    public void preOrderTraversal(Consumer<RBNode<K, V>> consumer) {
        preOrderTraversal(root, consumer);
    }

    /**
     * 得到前序遍历列表
     */
    public ArrayList<RBNode<K, V>> preOrderTraversalList() {
        ArrayList<RBNode<K, V>> list = new ArrayList<>(size);
        preOrderTraversal(root, list::add);
        return list;
    }

    protected void preOrderTraversal(RBNode<K, V> node, Consumer<RBNode<K, V>> consumer) {
        if (node != null) {
            if (consumer != null) consumer.accept(node);
            preOrderTraversal(node.leftNode, consumer);
            preOrderTraversal(node.rightNode, consumer);
        }
    }


    /**
     * 中序遍历
     */
    public void inOrderTraversal(Consumer<RBNode<K, V>> consumer) {
        inOrderTraversal(root, consumer);
    }

    /**
     * 得到中序遍历列表
     */
    public ArrayList<RBNode<K, V>> inOrderTraversalList() {
        ArrayList<RBNode<K, V>> list = new ArrayList<>(size);
        inOrderTraversal(root, list::add);
        return list;
    }

    protected void inOrderTraversal(RBNode<K, V> node, Consumer<RBNode<K, V>> consumer) {
        if (node != null) {
            inOrderTraversal(node.leftNode, consumer);
            if (consumer != null) consumer.accept(node);
            inOrderTraversal(node.rightNode, consumer);
        }
    }

    /**
     * 后序遍历
     */
    public void postOrderTraversal(Consumer<RBNode<K, V>> consumer) {
        postOrderTraversal(root, consumer);
    }

    /**
     * 得到后序遍历列表
     */
    public ArrayList<RBNode<K, V>> postOrderTraversalList() {
        ArrayList<RBNode<K, V>> list = new ArrayList<>(size);
        postOrderTraversal(root, list::add);
        return list;
    }

    protected void postOrderTraversal(RBNode<K, V> node, Consumer<RBNode<K, V>> consumer) {
        if (node != null) {
            postOrderTraversal(node.leftNode, consumer);
            postOrderTraversal(node.rightNode, consumer);
            if (consumer != null) consumer.accept(node);
        }
    }


    /**
     * 通过迭代器暴力查找
     */
    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        K res = ClassMaker.cast(key, obj -> (K) obj);
        if (res != null) {
            return search(res) != null;
        }
        return false;
    }

    @Override
    public V get(Object key) {
        K res = ClassMaker.cast(key, obj -> (K) obj);
        if (res != null) {
            RBNode<K, V> node = search(res);
            return node != null ? node.getValue() : null;
        }
        return null;
    }


    protected RBNode<K, V> searchDfs(RBNode<K, V> node, K key) {
        if (node == null) return null;
//        int cmp = key.compareTo(node.key);
        int cmp = comparator.compare(key, node.key);
        if (cmp == 0) {
            return node;
        } else if (cmp < 0) {
            return searchDfs(node.leftNode, key);
        } else {
            return searchDfs(node.rightNode, key);
        }
    }

    protected RBNode<K, V> searchDfs(K key) {
        return searchDfs(root, key);
    }

    protected RBNode<K, V> search(RBNode<K, V> node, K key) {
        while (node != null) {
//            int cmp = key.compareTo(node.key);
            int cmp = comparator.compare(key, node.key);
            if (cmp == 0) {
                return node;
            } else if (cmp < 0) {
                node = node.leftNode;
            } else {
                node = node.rightNode;
            }
        }
        return null;
    }

    protected RBNode<K, V> search(K key) {
        return search(root, key);
    }

    /**
     * 该子树下索引最小的节点
     */
    public RBNode<K, V> minimum(RBNode<K, V> node) {
        if (node == null) return null;
        while (node.leftNode != null) {
            node = node.leftNode;
        }
        return node;
    }

    /**
     * 索引最小的节点
     */
    public RBNode<K, V> minimum() {
        return minimum(root);
    }

    /**
     * 索引最小的节点的 key
     */
    public K minimumKey() {
        RBNode<K, V> node = minimum(root);
        if (node == null) return null;
        else return node.key;
    }

    /**
     * 该子树下索引最大的节点
     */
    public RBNode<K, V> maximum(RBNode<K, V> node) {
        if (node == null) return null;
        while (node.rightNode != null) {
            node = node.rightNode;
        }
        return node;
    }

    /**
     * 索引最大的节点
     */
    public RBNode<K, V> maximum() {
        return maximum(root);
    }

    /**
     * 索引最大的节点的 key
     */
    public K maximumKey() {
        RBNode<K, V> node = maximum(root);
        if (node == null) return null;
        else return node.key;
    }

    /**
     * 目标节点的后继节点， 即大于 该节点 的最小的节点
     */
    public RBNode<K, V> successor(@NotNull RBNode<K, V> node) {
        if (node.rightNode != null) {
            return minimum(node.rightNode);
        } else {
            RBNode<K, V> parent = node.parentNode;
            while (parent != null && node == parent.rightNode) {
                node = parent;
                parent = parent.parentNode;
            }
            return parent;
        }
    }

    /**
     * 目标节点等于key 的后继节点， 即大于 该节点 的最小的节点
     */
    public RBNode<K, V> successor(K key) {
        RBNode<K, V> node = search(key);
        if (node == null) return null;
        return successor(node);
    }

    /**
     * 目标节点的前驱节点， 即小于 该节点 的最大的节点
     */
    public RBNode<K, V> predecessor(@NotNull RBNode<K, V> node) {
        if (node.leftNode != null) {
            return maximum(node.leftNode);
        } else {
            RBNode<K, V> parent = node.parentNode;
            while (parent != null && node == parent.leftNode) {
                node = parent;
                parent = parent.parentNode;
            }
            return parent;
        }
    }

    /**
     * 目标节点等于key 的前驱节点， 即小于 该节点 的最大的节点
     */
    public RBNode<K, V> predecessor(K key) {
        RBNode<K, V> node = search(key);
        if (node == null) return null;
        return predecessor(node);
    }


    /**
     * 左旋
     *
     * @param node 当前节点
     */
    protected void leftRotate(RBNode<K, V> node) {
        RBNode<K, V> right = node.rightNode;
        // node 的右子树指向 right 的左子树, right 的左节点的父节点更新为 node
        node.rightNode = right.leftNode;
        if (right.leftNode != null) {
            right.leftNode.parentNode = node;
        }

        right.parentNode = node.parentNode;
        // right 的父节点指向 node 的父节点, node 的父节点指向 right
        if (node.parentNode != null) {
            if (node.parentNode.leftNode == node) {
                node.parentNode.leftNode = right;
            } else {
                node.parentNode.rightNode = right;
            }
        } else {
            // node 是根节点,更新根节点
            root = right;
        }
        // right 的左节点指向 node, node 的父节点指向 right
        right.leftNode = node;
        node.parentNode = right;
    }

    /**
     * 右旋
     *
     * @param node 当前节点
     */
    protected void rightRotate(RBNode<K, V> node) {
        RBNode<K, V> left = node.leftNode;
        // node 的左子树指向 left 的右子树, left 的右节点的父节点更新为 node
        node.leftNode = left.rightNode;
        if (left.rightNode != null) {
            left.rightNode.parentNode = node;
        }
        left.parentNode = node.parentNode;
        // left 的父节点指向 node 的父节点, node 的父节点指向 left
        if (node.parentNode != null) {
            if (node.parentNode.leftNode == node) {
                node.parentNode.leftNode = left;
            } else {
                node.parentNode.rightNode = left;
            }
        } else {
            // node 是根节点,更新根节点
            root = left;
        }
        // left 的右节点指向 node, node 的父节点指向 left
        left.rightNode = node;
        node.parentNode = left;
    }

    /**
     * 插入节点
     */
    public void insert(Entry<K, V> entry) {
        if (entry != null) {
            insert(new RBNode<>(entry));
        }
    }

    /**
     * 添加
     * <p>不允许key为null</p>
     */
    @Override
    public V put(K key, V value) {
        if (key == null) return null;
        insert(key, value);
        return value;
    }

    /**
     * 插入节点
     */
    public void insert(K key, V value) {
        insert(new RBNode<>(key, value));
    }

    /**
     * 插入节点
     */
    protected void insert(RBNode<K, V> node) {
        // 查找node父节点
        RBNode<K, V> parent = null;
        RBNode<K, V> current = root;
        while (current != null) {
            parent = current;
//            int cmp = node.key.compareTo(current.key);
            int cmp = comparator.compare(node.key, current.key);
            if (cmp < 0) {
                current = current.leftNode;
            } else if (cmp > 0) {
                current = current.rightNode;
            } else {
                current.value = node.value;
                return;
            }
        }
        node.parentNode = parent;
        if (parent != null) {
//            int cmp = node.key.compareTo(parent.key);
            int cmp = comparator.compare(node.key, parent.key);
            if (cmp > 0) {
                parent.rightNode = node;
            } else {
                parent.leftNode = node;
            }
        } else {
            root = node;
        }
        ++size;
        setColor(node, RED);
        insertFixup(node);
    }

    /**
     * 修复操作
     */
    protected void insertFixup(RBNode<K, V> node) {
        this.root.setColor(BLACK);
        RBNode<K, V> parent, gParent;
        while (((parent = parentOf(node)) != null) && parent.isRed()) {
            gParent = parentOf(parent);
            // 如果父节点是红色 那么一定存在爷爷节点，根节点不为红色
            RBNode<K, V> uncle = null;
            if (parent == gParent.leftNode) {
                uncle = gParent.rightNode;
                if (uncle != null && uncle.isRed()) { //叔叔节点是红色
                    setColor(uncle, BLACK);
                    setColor(parent, BLACK);
                    setColor(gParent, RED);
                    node = gParent;
                    continue;
                }
                if (node == gParent.rightNode) { //叔叔是黑色，且当前节点是右孩子
                    leftRotate(parent);
                    RBNode<K, V> tmp = parent;
                    parent = node;
                    node = tmp;
                }
                // 叔叔 is null 或者黑色，且当前节点是左孩子。
                setColor(parent, BLACK);
                setColor(gParent, RED);
                rightRotate(gParent);
            } else { // 右节点
                uncle = gParent.leftNode;
                if (uncle != null && uncle.isRed()) { //叔叔节点是红色
                    setColor(uncle, BLACK);
                    setColor(parent, BLACK);
                    setColor(gParent, RED);
                    node = gParent;
                    continue;
                }
                if (node == gParent.leftNode) {
                    //叔叔是黑色，且当前节点是左孩子
                    rightRotate(parent);
                    RBNode<K, V> tmp = parent;
                    parent = node;
                    node = tmp;
                }
                // 叔叔 is null 或者黑色，且当前节点是右孩子。
                setColor(parent, BLACK);
                setColor(gParent, RED);
                leftRotate(gParent);
            }
        }
        setColor(root, BLACK);
    }

    @Override
    public V remove(Object key) {
        if (key == null) {
            return null;
        }
        K res = ClassMaker.cast(key, obj -> (K) obj);
        if (res == null) {
            return null;
        }
        return removeKey(res);
    }

    public V removeKey(K key) {
        RBNode<K, V> node = search(key);
        if (node == null) return null;
        removeNode(node);
        return node.value;
    }

    /**
     * 删除节点
     */
    protected void removeNode(@NotNull RBNode<K, V> node) {
        RBNode<K, V> child, parent;
        boolean color;
        if ((node.leftNode != null) && (node.rightNode != null)) {
            RBNode<K, V> replace = node;

            replace = replace.rightNode;
            while (replace.leftNode != null) {
                replace = replace.leftNode;
            }

            // 不是根节点
            if (parentOf(node) != null) {
                if (parentOf(node).leftNode == node)
                    parentOf(node).leftNode = replace;
                else
                    parentOf(node).rightNode = replace;
            } else {
                this.root = replace;
            }
            child = replace.rightNode;
            parent = parentOf(replace);
            // 保存"取代节点"的颜色
            color = replace.color;

            if (parent == node) {
                parent = replace;
            } else {
                if (child != null) {
                    child.parentNode = parent;
                }
                parent.leftNode = child;

                replace.rightNode = node.rightNode;
                node.rightNode = replace;
            }

            replace.parentNode = node.parentNode;
            replace.color = node.color;
            replace.leftNode = node.leftNode;
            node.leftNode.parentNode = replace;

            if (color == BLACK) {
                removeFixUp(child, parent);
            }
            node = null;
        } else {
            if (node.leftNode != null) {
                child = node.leftNode;
            } else {
                child = node.rightNode;
            }

            parent = node.parentNode;
            color = node.color;

            if (child != null)
                child.parentNode = parent;

            if (parent != null) {
                if (parent.leftNode == node)
                    parent.leftNode = child;
                else
                    parent.rightNode = child;
            } else {
                this.root = child;
            }

            if (color == BLACK)
                removeFixUp(child, parent);
            node = null;
        }
        --size;
    }

    /**
     * 删除修复操作
     */
    protected void removeFixUp(RBNode<K, V> node, RBNode<K, V> parent) {
        RBNode<K, V> other;

        while ((node == null || isBlack(node)) && (node != this.root)) {
            if (parent.leftNode == node) {
                other = parent.rightNode; // 兄弟节点
                if (isRed(other)) {
                    setColor(other, BLACK);
                    setColor(parent, RED);
                    leftRotate(parent);
                    other = parent.rightNode;
                }

                if ((other.leftNode == null || other.leftNode.isBlack()) &&
                        (other.rightNode == null || other.rightNode.isBlack())) {
                    setColor(other, RED);
                    node = parent;
                    parent = parentOf(node);
                } else {
                    if (other.rightNode == null || isBlack(other.rightNode)) {
                        setColor(other.leftNode, BLACK);
                        setColor(other, RED);
                        rightRotate(other);
                        other = parent.rightNode;
                    }
                    setColor(other, parent.color);
                    setColor(parent, BLACK);
                    setColor(other.rightNode, BLACK);
                    leftRotate(parent);
                    node = this.root;
                    break;
                }
            } else {
                other = parent.leftNode;
                if (isRed(other)) {
                    setColor(other, BLACK);
                    setColor(parent, RED);
                    rightRotate(parent);
                    other = parent.leftNode;
                }

                if ((other.leftNode == null || other.leftNode.isBlack()) &&
                        (other.rightNode == null || other.rightNode.isBlack())) {
                    setColor(other, RED);
                    node = parent;
                    parent = parentOf(node);
                } else {

                    if (other.leftNode == null || isBlack(other.leftNode)) {
                        setColor(other.rightNode, BLACK);
                        setColor(other, RED);
                        leftRotate(other);
                        other = parent.leftNode;
                    }
                    setColor(other, parent.color);
                    setColor(parent, BLACK);
                    setColor(other.leftNode, BLACK);
                    rightRotate(parent);
                    node = this.root;
                    break;
                }
            }
        }
        if (node != null) {
            setColor(node, BLACK);
        }
    }

    /**
     * 迭代器
     */
    @NotNull
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new RBNodeIterator(root, size);
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet(this.root, this.size);
    }


    class RBNodeIterator extends BaseEntryBiNodeIterator<RBNode<K, V>, K, V> {
        public RBNodeIterator(RBNode<K, V> root, int length) {
            super(length, root);
        }

        @Override
        public void remove() {
            if (currentNode == null) return;
            removeNode(currentNode);
        }
    }

    class EntrySet extends AbstractSet<Entry<K, V>> {

        private RBNode<K, V> root;

        public EntrySet(RBNode<K, V> root, int size) {
            this.root = root;
        }

        @NotNull
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new RBNodeIterator(root, -1);
        }

        @Override
        public int size() {
            return size;
        }
    }

}
