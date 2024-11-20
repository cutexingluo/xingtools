package top.cutexingluo.tools.utils.se.algo.cpp.structure.tree.btree;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.common.base.IData;
import top.cutexingluo.tools.common.base.IValue;
import top.cutexingluo.tools.common.data.MapEntry;
import top.cutexingluo.tools.common.data.PairEntry;
import top.cutexingluo.tools.common.data.Tuple;
import top.cutexingluo.tools.common.data.TupleEntry;
import top.cutexingluo.tools.common.data.node.IParent;
import top.cutexingluo.tools.designtools.method.ClassMaker;
import top.cutexingluo.tools.utils.se.core.compare.XTComparator;
import top.cutexingluo.tools.utils.se.core.iterator.BaseNowIterator;
import top.cutexingluo.tools.utils.se.core.iterator.BaseNowNodeIterator;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

/**
 * B 树
 * <p>B-Tree</p>
 * <p>B 树的 map 实现类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/13 10:28
 * @since 1.1.6
 */
public class BTree<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V>, Iterable<Map.Entry<K, V>>, Serializable {


    /**
     * 阶数 (order)
     */
    protected transient int m;

    /**
     * 元素数量
     */
    protected transient int size;
    /**
     * 根节点
     */
    protected transient BNode root;

    /**
     * key 比较器
     */
    protected Comparator<? super K> comparator;

    /**
     * 值集合构造器
     */
    protected Supplier<LinkedList<Map.Entry<K, V>>> valuesSupplier;

    /**
     * 节点集合构造器
     */
    protected Supplier<LinkedList<BNode>> nodesSupplier;


    /**
     * 构造一个 m 阶 B 树
     *
     * @param m              阶数
     * @param valuesSupplier 值集合构造器
     * @param nodesSupplier  节点集合构造器
     */
    public BTree(int m, Comparator<? super K> comparator,
                 Supplier<LinkedList<Entry<K, V>>> valuesSupplier,
                 Supplier<LinkedList<BNode>> nodesSupplier) {
        if (m <= 2) {
            throw new IllegalArgumentException("the order of BTree must be greater than 2");
        }
        Objects.requireNonNull(valuesSupplier);
        Objects.requireNonNull(nodesSupplier);
        this.m = m;
        this.comparator = comparator;
        this.valuesSupplier = valuesSupplier;
        this.nodesSupplier = nodesSupplier;
        init();
    }

    /**
     * 构造一个 m 阶 B 树
     * <p>默认使用 LinkedList 作为构造器</p>
     *
     * @param m 阶数
     */
    public BTree(int m, Comparator<? super K> comparator) {
        this(m, comparator, LinkedList::new, LinkedList::new);
    }

    /**
     * 构造一个 m 阶 B 树
     * <p>默认使用 XTComparator 作为比较器, LinkedList 作为构造器</p>
     *
     * @param m 阶数
     */
    public BTree(int m) {
        this(m, new XTComparator<>(true), LinkedList::new, LinkedList::new);
    }


    /**
     * 复制一个 BTree 的属性
     *
     * @param reversed 是否反转 comparator
     * @return 新的 BTree
     */
    public BTree<K, V> copyProperties(boolean reversed) {
        if (reversed) return new BTree<K, V>(m, comparator.reversed(), valuesSupplier, nodesSupplier);
        return new BTree<K, V>(m, comparator, valuesSupplier, nodesSupplier);
    }

    protected void init() {
        size = 0;
        root = new BNode(null);
    }

    @Override
    public void clear() {
        init();
    }

    @Override
    public Comparator<? super K> comparator() {
        return comparator;
    }

    @Override
    public int size() {
        return this.size;
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
     * 插入一个键值对
     */
    public V insert(K key, V value) {
        Objects.requireNonNull(key);
        BNode temp = root.searchNode(key);
        V res = temp.insert(key, value);
        if (temp.values.size() == m) root = temp.overflow();
        return res;
    }

    /**
     * 插入一个键值对
     */
    public V insert(Entry<K, V> one) {
        if (one != null) {
            return insert(one.getKey(), one.getValue());
        }
        return null;
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
            return search(res);
        }
        return null;
    }

    public V search(K key) {
        if (!root.hasValues()) return null;
        return root.search(key);
    }

    public V removeKey(K key) {
        if (!root.hasValues()) return null;
        V res = search(key);
        if (res == null) return null;
        BNode remoNode = root.searchNode(key);
        root = remoNode.remove(key);
        return res;
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


    // minimum

    /**
     * 该子树下索引最小的节点
     */
    public BNode minimum(final BNode node) {
        if (node == null) return null;
        if (node.isLeaf()) return node;
        BNode now = node;
        BNode minNode = now.getMinNode();
        while (minNode != now) {
            now = minNode;
            minNode = now.getMinNode();
        }
        return minNode;
    }

    /**
     * 索引最小的节点
     */
    @Nullable
    public BNode minimum() {
        return minimum(root);
    }

    /**
     * 索引最小的节点的 entry
     */
    @Nullable
    public Entry<K, V> minimumEntry() {
        BNode node = minimum(root);
        if (node == null) return null;
        return node.values.getFirst();
    }

    /**
     * 索引最小的节点的 key
     */
    @Nullable
    public K minimumKey() {
        Entry<K, V> entry = minimumEntry();
        if (entry == null) return null;
        return entry.getKey();
    }


    // maximum

    /**
     * 该子树下索引最大的节点
     */
    @Nullable
    public BNode maximum(final BNode node) {
        if (node == null) return null;
        if (node.isLeaf()) return node;
        BNode now = node;
        BNode maxNode = now.getMaxNode();
        while (maxNode != now) {
            now = maxNode;
            maxNode = now.getMaxNode();
        }
        return maxNode;
    }

    /**
     * 索引最大的节点
     */
    @Nullable
    public BNode maximum() {
        return maximum(root);
    }

    /**
     * 索引最大的节点的 entry
     */
    @Nullable
    public Entry<K, V> maximumEntry() {
        BNode node = maximum(root);
        if (node == null) return null;
        return node.values.getLast();
    }


    /**
     * 索引最小的节点的 key
     */
    @Nullable
    public K maximumKey() {
        Entry<K, V> entry = maximumEntry();
        if (entry == null) return null;
        return entry.getKey();
    }

    // successor

    /**
     * 目标节点的后继节点， 即完全大于 该节点最大值 的最小的节点
     */
    @Nullable
    public BNode successorOut(@NotNull final BNode node) {
        if (!node.isLeaf()) {
            return minimum(node.getMaxNode());
        } else { // 原节点是叶子节点
            BNode now = node;
            BNode parent = node.parent;
            while (parent != null) {
                int pos = parent.getNodePosition(now);
                if (pos == parent.nodes.size() - 1) { //  最后一个
                    now = parent;
                    parent = parent.parent;
                } else if (pos == 0) { // 第一个
                    return parent;
                } else {
                    BNode brother = parent.nodes.get(pos + 1);
                    return minimum(brother);
                }
            }
            // 叶子节点且全局没有后继节点
            return null;
        }
    }

    /**
     * 目标节点的后继节点， 即存在大于 该节点最大值 的最小的节点
     */
    @Nullable
    public BNode successorIn(@NotNull final BNode node) {
        if (!node.isLeaf()) {
            return minimum(node.getMaxNode());
        } else { // 原节点是叶子节点
            BNode now = node;
            BNode parent = node.parent;
            while (parent != null) {
                int pos = parent.getNodePosition(now);
                if (pos == parent.nodes.size() - 1) { //  最后一个
                    now = parent;
                    parent = parent.parent;
                } else { // 父节点
                    return parent;
                }
            }
            // 叶子节点且全局没有后继节点
            return null;
        }
    }

    /**
     * 目标 key  所在节点的后继节点，即完全大于 所在节点最大值 的最小的节点
     */
    @Nullable
    public BNode successorOut(K key) {
        if (root == null) return null;
        PairEntry<BNode, Integer> entry = root.searchNodeIndex(key);
        final BNode node = entry.getKey();
        return successorOut(node);
    }

    /**
     * 目标 key  所在节点的后继节点，即存在大于 当前 key  的最小的节点
     */
    @Nullable
    public BNode successorIn(K key) {
        if (root == null) return null;
        PairEntry<BNode, Integer> entry = root.searchNodeIndex(key);
        final BNode node = entry.getKey();
        final int index = entry.getValue();
        if (index == node.values.size() - 1) { // 最后一个
            return successorIn(node);
        } else { // 不为最后一个即当前节点
            return node;
        }
    }


    /**
     * 目标 key  所在节点的后继 entry，即存在大于 当前 key  的最小的entry
     */
    @Nullable
    public Entry<K, V> successorEntry(K key) {
        if (root == null) return null;
        PairEntry<BNode, Integer> entry = root.searchNodeIndex(key);
        final BNode node = entry.getKey();
        final int index = entry.getValue();
        BNode successor = successorIn(key);
        if (successor == null) { // 没后面节点
            return null;
        } else if (successor == node) { // 不是最大值
            if (index != node.values.size() - 1) {
                return node.values.get(index + 1);
            }
        } else { // 是最大值
            BNode parent = node.parent;
            if (parent == successor) { // 是父节点
                int nodePosition = parent.getNodePosition(successor);
                if (nodePosition != parent.values.size() - 1) {
                    return parent.values.get(nodePosition + 1);
                }
            } else {
                return successor.values.getFirst(); // 直接取第一个
            }
        }
        return null;
    }

    // predecessor


    /**
     * 目标节点的前驱节点， 即完全小于 该节点最小值 的最大的节点
     */
    @Nullable
    public BNode predecessorOut(@NotNull final BNode node) {
        if (!node.isLeaf()) {
            return maximum(node.getMaxNode());
        } else { // 原节点是叶子节点
            BNode now = node;
            BNode parent = node.parent;
            while (parent != null) {
                int pos = parent.getNodePosition(now);
                if (pos == 0) { // 第一个
                    now = parent;
                    parent = parent.parent;
                } else if (pos == parent.nodes.size() - 1) { //  最后一个
                    return parent;
                } else {
                    BNode brother = parent.nodes.get(pos - 1);
                    return maximum(brother);
                }
            }
            // 叶子节点且全局没有前驱节点
            return null;
        }
    }

    /**
     * 目标节点的前驱节点， 即存在小于 该节点最小值 的最大的节点
     */
    @Nullable
    public BNode predecessorIn(@NotNull final BNode node) {
        if (!node.isLeaf()) {
            return maximum(node.getMinNode());
        } else { // 原节点是叶子节点
            BNode now = node;
            BNode parent = node.parent;
            while (parent != null) {
                int pos = parent.getNodePosition(now);
                if (pos == 0) { //  第一个
                    now = parent;
                    parent = parent.parent;
                } else { // 父节点
                    return parent;
                }
            }
            // 叶子节点且全局没有后继节点
            return null;
        }
    }


    /**
     * 目标 key  所在节点的前驱节点，即完全小于 所在节点最小值 的最大的节点
     */
    @Nullable
    public BNode predecessorOut(K key) {
        if (root == null) return null;
        PairEntry<BNode, Integer> entry = root.searchNodeIndex(key);
        final BNode node = entry.getKey();
        return predecessorOut(node);
    }

    /**
     * 目标 key  所在节点的前驱节点，即存在小于 当前 key  的最大的节点
     */
    @Nullable
    public BNode predecessorIn(K key) {
        if (root == null) return null;
        PairEntry<BNode, Integer> entry = root.searchNodeIndex(key);
        final BNode node = entry.getKey();
        final int index = entry.getValue();
        if (index == 0) { // 第一个
            return predecessorIn(node);
        } else { // 不为第一个即当前节点
            return node;
        }
    }

    /**
     * 目标 key  所在节点的前驱 entry，即存在小于 当前 key  的最大的 entry
     */
    @Nullable
    public Entry<K, V> predecessorEntry(K key) {
        if (root == null) return null;
        PairEntry<BNode, Integer> entry = root.searchNodeIndex(key);
        final BNode node = entry.getKey();
        final int index = entry.getValue();
        BNode predecessor = predecessorIn(key);
        if (predecessor == null) { // 没前面节点
            return null;
        } else if (predecessor == node) { // 不是最小值
            if (index != 0) {
                return node.values.get(index - 1);
            }
        } else { // 是最大值
            BNode parent = node.parent;
            if (parent == predecessor) { // 是父节点
                int nodePosition = parent.getNodePosition(predecessor);
                if (nodePosition != 0) {
                    return parent.values.get(nodePosition - 1);
                }
            } else {
                return predecessor.values.getFirst(); // 直接取第一个
            }
        }
        return null;
    }


    @Override
    public Entry<K, V> lowerEntry(K key) {
        return predecessorEntry(key);
    }

    @Override
    public K lowerKey(K key) {
        Entry<K, V> entry = predecessorEntry(key);
        if (entry == null) return null;
        return entry.getKey();
    }

    @Override
    public Entry<K, V> higherEntry(K key) {
        return successorEntry(key);
    }

    @Override
    public K higherKey(K key) {
        Entry<K, V> entry = successorEntry(key);
        if (entry == null) return null;
        return entry.getKey();
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        if (root == null) return null;
        Entry<K, V> entry = root.searchEntry(key);
        if (entry != null) return entry;
        return predecessorEntry(key);
    }

    @Override
    public K floorKey(K key) {
        Entry<K, V> entry = floorEntry(key);
        if (entry == null) return null;
        return entry.getKey();
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        if (root == null) return null;
        Entry<K, V> entry = root.searchEntry(key);
        if (entry != null) return entry;
        return successorEntry(key);
    }

    @Override
    public K ceilingKey(K key) {
        Entry<K, V> entry = ceilingEntry(key);
        if (entry == null) return null;
        return entry.getKey();
    }

    @Override
    public Entry<K, V> firstEntry() {
        BNode node = minimum();
        return node != null ? node.values.getFirst() : null;
    }


    @Override
    public Entry<K, V> lastEntry() {
        BNode node = maximum();
        return node != null ? node.values.getLast() : null;
    }

    @Override
    public Entry<K, V> pollFirstEntry() {
        BNode node = minimum();
        if (node == null) return null;
        return node.removeIndexRet(0);
    }

    @Override
    public Entry<K, V> pollLastEntry() {
        BNode node = maximum();
        if (node == null) return null;
        return node.removeIndexRet(node.values.size() - 1);
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        BTree<K, V> tree = copyProperties(true);
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
        BTree<K, V> tree = copyProperties(false);
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
        if (toKey == null) throw new IllegalArgumentException(" toKey is null");
        BTree<K, V> tree = copyProperties(false);
        for (Entry<K, V> entry : this) {
            int cmpTo = comparator.compare(entry.getKey(), toKey);
            if (cmpTo > 0) break;
            tree.insert(entry);
        }
        return tree;
    }

    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        if (fromKey == null) throw new IllegalArgumentException("fromKey  is null");
        BTree<K, V> tree = copyProperties(false);
        for (Entry<K, V> entry : this) {
            int cmpFrom = comparator.compare(entry.getKey(), fromKey);
            if (cmpFrom < 0) continue;
            tree.insert(entry);
        }
        return tree;
    }


    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        if (fromKey == null || toKey == null) throw new IllegalArgumentException("fromKey or toKey is null");
        BTree<K, V> tree = copyProperties(false);
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
        BTree<K, V> tree = copyProperties(false);
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
        BTree<K, V> tree = copyProperties(false);
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
     * BNode 节点
     */
    @Data
    public class BNode implements IParent<BNode>, IData<List<BNode>>, IValue<List<Map.Entry<K, V>>> {
        /**
         * 父节点
         */
        protected BNode parent;
        /**
         * 键值对
         */
        @NotNull
        protected LinkedList<Map.Entry<K, V>> values;
        /**
         * 链接的节点
         */
        protected LinkedList<BNode> nodes;

        public BNode(BNode parent) {
            this.parent = parent;
            this.values = valuesSupplier.get();
            this.nodes = null;
        }

        public BNode(BNode parent,
                     @NotNull LinkedList<Entry<K, V>> values,
                     LinkedList<BNode> nodes) {
            this.parent = parent;
            this.values = values;
            this.nodes = nodes;
        }


        @Override
        public LinkedList<BNode> data() {
            return this.nodes;
        }

        @Override
        public LinkedList<Map.Entry<K, V>> getValue() {
            return this.values;
        }


        @Override
        public String toString() {
            return "BNode{" +
                    "values=" + values +
                    '}';
        }

        protected void checkNodes() {
            if (nodes != null && nodes.isEmpty()) nodes = null;
        }


        /**
         * 判断是否有节点
         */
        public boolean hasNodes() {
            checkNodes();
            return nodes != null;
        }

        /**
         * 判断是否有值
         */
        public boolean hasValues() {
            return !values.isEmpty();
        }


        /**
         * 判断是否为叶子节点
         */
        public boolean isLeaf() {
            return !hasNodes();
        }

        /**
         * 获取祖先节点
         *
         * <p>若为空返回自身</p>
         */
        public BNode getAncestor() {
            if (parent == null) return this;
            return parent.getAncestor();
        }

        /**
         * 判断是否合法
         */
        public int isLegal() {
            if (parent == null && (nodes == null || nodes.size() > 1)) {
                return 0;
            }
            return values.size() - ((int) Math.ceil(m / 2.0) - 1);
        }

        public BNode getMaxNode() {
            if (isLeaf()) return this;
            return this.nodes.getLast().getMaxNode();
        }

        public BNode getMinNode() {
            if (isLeaf()) return this;
            return nodes.getFirst().getMinNode();
        }


        /**
         * 更改父节点
         */
        protected void changeParent() {
            if (isLeaf()) return;
            for (BNode n : this.nodes) {
                n.parent = this;
            }
        }

        /**
         * 获取节点位置
         */
        protected int getNodePosition(BNode node) {
            int res = -1;
            int index = -1;
            for (BNode tar : this.nodes) {
                index++;
                if (tar == node) {
                    res = index;
                    break;
                }
            }
            return res;
        }

        /**
         * 获取节点 key 位置
         */
        protected int getKeyPosition(K key) {
            int res = -1;
            int index = -1;
            for (Entry<K, V> tar : this.values) {
                index++;
                int compare = comparator.compare(key, tar.getKey());
                if (compare == 0) {
                    res = index;
                    break;
                }
            }
            return res;
        }

        /**
         * 获取将要插入节点位置
         */
        protected int getInsertNodePosition(BNode node) {
            int position = -1;
            for (BNode tar : this.nodes) {
                position++;
                if (tar == node) break;
            }
            return position;
        }


        /**
         * 节点上溢
         */
        protected BNode overflow() {
            if (values.size() != m) return getAncestor();
            if (parent == null) {
                this.parent = new BNode(null);
                this.parent.nodes = nodesSupplier.get();
            }

            List<Entry<K, V>> vs = this.values;

            this.values = valuesSupplier.get();
            BNode right = new BNode(parent);

            int i = vs.size() / 2;
            Entry<K, V> kv = vs.get(i);

            this.values.addAll(vs.subList(0, i));
            right.values.addAll(vs.subList(i + 1, vs.size()));

            if (!this.isLeaf()) {
                LinkedList<BNode> ns = this.nodes;
                this.nodes = nodesSupplier.get();
                right.nodes = nodesSupplier.get();
                this.nodes.addAll(ns.subList(0, i + 1));
                right.nodes.addAll(ns.subList(i + 1, ns.size()));
                right.changeParent();
            }

            if (this.parent.values.isEmpty()) {
                parent.values.add(kv);
                parent.nodes.add(this);
                parent.nodes.add(right);
            } else {
                int position = parent.getInsertNodePosition(this);
                parent.values.add(position, kv);
                parent.nodes.add(position + 1, right);
            }

            return parent.overflow();
        }

        /**
         * 调整节点
         */
        protected BNode adjustNode() {
            if (isLegal() >= 0) return getAncestor();
            if (isLeaf() && parent == null) return getAncestor();

            if (parent == null) { // 没有父节点，并且树不合法 树降阶
                BNode p = new BNode(null);
                if (!nodes.get(0).isLeaf()) p.nodes = nodesSupplier.get();
                for (BNode node : nodes) {
                    p.values.addAll(node.values);
                    if (!node.isLeaf()) p.nodes.addAll(node.nodes);
                }
                p.changeParent();
                return p;
            }

            int position = parent.getInsertNodePosition(this);
            List<Entry<K, V>> vs = parent.values;
            // 判断向左或右借节点
            if (position > 0 && parent.nodes.get(position - 1).isLegal() > 0) {
                BNode left = parent.nodes.get(position - 1);
                Entry<K, V> remove = left.values.removeLast();
                Entry<K, V> kv = vs.remove(position - 1);
                vs.add(position - 1, remove);
                this.values.addFirst(kv);
                if (!left.isLeaf()) {
                    BNode childNode = left.nodes.removeLast();
                    this.nodes.addFirst(childNode);
                    this.changeParent();
                }
                return getAncestor();
            } else if (parent.nodes.size() != position + 1 && parent.nodes.get(position + 1).isLegal() > 0) {
                BNode right = parent.nodes.get(position + 1);
                Entry<K, V> remove = right.values.removeFirst();
                Entry<K, V> kv = parent.values.remove(position);
                parent.values.add(position, remove);
                this.values.addLast(kv);
                if (!right.isLeaf()) {
                    BNode childNode = right.nodes.removeFirst();
                    this.nodes.addLast(childNode);
                    this.changeParent();
                }
                return getAncestor();
            } else if (position > 0) { // 左右都无法拿出节点 存在左节点则合并左节点
                Entry<K, V> remove = parent.values.remove(position - 1);
                BNode left = parent.nodes.get(position - 1);
                left.values.addLast(remove);
                left.values.addAll(this.values);
                if (!isLeaf()) {
                    left.nodes.addAll(this.nodes);
                    left.changeParent();
                }
                parent.nodes.remove(this);
            } else { // 合并右节点
                Entry<K, V> remove = parent.values.remove(position);
                BNode right = parent.nodes.get(position + 1);
                right.values.addFirst(remove);
                right.values.addAll(0, this.values);
                if (!isLeaf()) {
                    right.nodes.addAll(0, this.nodes);
                    right.changeParent();
                }
                parent.nodes.remove(this);
            }
            return parent.adjustNode();
        }

        /**
         * 调整节点
         */
        protected BNode adjust(Integer position) {
            if (isLeaf()) return adjustNode();
            // 用右节点最小值替换被删除位置
            BNode node = null;
            if (nodes.size() > position + 1) node = nodes.get(position + 1).getMinNode();
            if (node != null && node.isLegal() > 0) {
                Entry<K, V> first = node.values.removeFirst();
                values.add(position, first);
            } else {
                node = nodes.get(position).getMaxNode();
                // 用左节点最大值替换被删除的位置
                Entry<K, V> last = node.values.removeLast();
                values.add(position, last);
            }
            return node.adjustNode();
        }

        /**
         * 删除 索引
         */
        public BNode removeIndex(int index) {
            // 删除数据
            this.values.remove(index);
            size--; // 删除元素 size -1
            // 调整节点
            return adjust(index);
        }


        /**
         * 删除 索引
         */
        public Entry<K, V> removeIndexRet(int index) {
            // 删除数据
            Entry<K, V> entry = this.values.remove(index);
            size--; // 删除元素 size -1
            // 调整节点
            adjust(index);
            return entry;
        }

        /**
         * 删除 key
         */
        public BNode remove(K key) {
            // 删除数据
            Iterator<Entry<K, V>> iterator = this.values.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                Entry<K, V> kv = iterator.next();
                if (comparator.compare(key, kv.getKey()) == 0) {
                    this.values.remove(i);
                    size--; // 删除元素 size -1
                    break;
                }
                i++;
            }
            // 调整节点
            return adjust(i);
        }

        /**
         * 查找 key 的 entry
         */
        @Nullable
        public Map.Entry<K, V> searchEntry(K key) {
            LinkedList<Map.Entry<K, V>> vs = this.values;
            LinkedList<BNode> ns = this.nodes;
            if (!isLeaf()) {
                int compare = comparator.compare(key, vs.getFirst().getKey());
                if (compare < 0) return ns.getFirst().searchEntry(key);
                if (compare > 0) return ns.getLast().searchEntry(key);
            }
            int index = 0;
            for (Map.Entry<K, V> kv : this.values) {
                int compare = comparator.compare(key, kv.getKey());
                if (compare == 0) return kv;
                else if (!isLeaf() && compare < 0) return ns.get(index).searchEntry(key);
                index++;
            }
            return null;
        }

        /**
         * 查找 key 的 node
         * <p>不存在则返回当前节点</p>
         */
        @NotNull
        public BNode searchNode(K key) {
            if (isLeaf()) return this;
            int i = -1;
            for (Map.Entry<K, V> kv : this.values) {
                i++;
                int compare = comparator.compare(key, kv.getKey());
                if (compare == 0) return this;
                if (compare > 0) continue;
                return nodes.get(i).searchNode(key);
            }
            return nodes.getLast().searchNode(key);
        }

        /**
         * 查找 key 的 node
         *
         * @return 返回 node 和 (key 在 node 中的索引, null 为不存在)
         */
        @NotNull
        public PairEntry<BNode, Integer> searchNodeIndex(K key) {
            if (isLeaf()) return new TupleEntry<>(this, null);
            int i = -1;
            for (Map.Entry<K, V> kv : this.values) {
                i++;
                int compare = comparator.compare(key, kv.getKey());
                if (compare == 0) return new TupleEntry<>(this, i);
                if (compare > 0) continue;
                return nodes.get(i).searchNodeIndex(key);
            }
            return nodes.getLast().searchNodeIndex(key);
        }

        /**
         * 查找 key 的 value
         */
        public V search(K key) {
            Map.Entry<K, V> entry = searchEntry(key);
            return entry == null ? null : entry.getValue();
        }

        /**
         * 插入并得到，插入成功后的
         */
        protected Entry<K, V> insertAndGet(K key, V value) {
            Entry<K, V> res = null;
            // 插入数据
            if (values.size() == 0 ||
                    comparator.compare(key, values.getLast().getKey()) > 0) {
                values.add(new MapEntry<>(key, value));
                res = values.getLast();
                size++; // size + 1
            } else if (
                    comparator.compare(key, values.getFirst().getKey()) < 0) {
                values.addFirst(new MapEntry<>(key, value));
                res = values.getFirst();
                size++; // size + 1
            } else {
                for (int i = 0; i < values.size(); i++) {
                    int compare = comparator.compare(key, values.get(i).getKey());
//                    if (compare ==0) return values.get(i).put(key, t);
                    if (compare == 0) return values.set(i, new MapEntry<>(key, value));
                    if (compare > 0) continue;
                    values.add(i, new MapEntry<>(key, value));
                    res = values.get(i);
                    size++; // size + 1
                    break;
                }
            }
            return res;
        }

        /**
         * 插入并得到，返回插入成功前的数据
         */
        protected Map.Entry<K, V> insertAndGetOld(K key, V value) {
            Map.Entry<K, V> res = null;
            // 插入数据
            if (values.size() == 0 ||
                    comparator.compare(key, values.getLast().getKey()) > 0) {
                values.add(new MapEntry<>(key, value));
                size++; // size + 1
            } else if (
                    comparator.compare(key, values.getFirst().getKey()) < 0) {
                values.addFirst(new MapEntry<>(key, value));
                size++; // size + 1
            } else {
                for (int i = 0; i < values.size(); i++) {
                    int compare = comparator.compare(key, values.get(i).getKey());
//                    if (compare ==0) return values.get(i).put(key, t);
                    if (compare == 0) {
                        return values.set(i, new MapEntry<>(key, value));
                    }
                    if (compare > 0) continue;
                    values.add(i, new MapEntry<>(key, value));
                    size++; // size + 1
                    break;
                }
            }
            return res;
        }

        /**
         * 插入
         */
        protected V insert(K key, V value) {
            Entry<K, V> entry = insertAndGetOld(key, value);
            return entry == null ? null : entry.getValue();
        }


        /**
         * 插入
         */
        protected V insert(@NotNull Entry<K, V> one) {
            Entry<K, V> entry = insertAndGetOld(one.getKey(), one.getValue());
            return entry == null ? null : entry.getValue();
        }
    }

    // 迭代器

    /**
     * 迭代器 (默认中序遍历迭代器)
     * <p>比 key 迭代器快一点</p>
     */
    @NotNull
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new BNodeEntryIterator(root);
    }

    /**
     * node 中序遍历 迭代器
     */
    @NotNull
    public Iterator<BNode> nodeIterator() {
        return new BNodeIterator(root);
    }

    /**
     * entry key 大小遍历 set
     */
    @NotNull
    public Iterator<Entry<K, V>> entrySortedIterator() {
        return new BNodeSortedEntryIterator(root);
    }


    // set

    /**
     * entry 中序遍历 set
     *
     * @see #entrySortedSet
     */
    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet(root);
    }

    /**
     * node 中序遍历 set
     */
    @NotNull
    public Set<BNode> nodeSet() {
        return new BNodeSet(root);
    }

    /**
     * entry key 大小遍历 set
     */
    @NotNull
    public Set<Entry<K, V>> entrySortedSet() {
        return new EntrySortedSet(root);
    }


    // 迭代器和 set 类

    /**
     * BNode 迭代器 (中序遍历)
     */
    class BNodeIterator extends BaseNowNodeIterator<BNode> {
        public BNodeIterator() {
        }

        public BNodeIterator(LinkedList<BNode> linkedList) {
            super(linkedList);
        }

        public BNodeIterator(BNode root) {
            linkedList.add(root);
        }

        @Override
        public BNode next() {
            BNode node = super.next();
            if (node != null && node.hasNodes()) {
                linkedList.addAll(0, node.nodes);
            }
            return node;
        }

    }

    /**
     * MapEntry 迭代器 (中序遍历)
     */
    class BNodeEntryIterator extends BaseNowIterator<BNode, Entry<K, V>> {
        protected LinkedList<Entry<K, V>> values;
        protected BNode currentNode;
        protected Entry<K, V> currentEntry;

        public BNodeEntryIterator() {
            values = new LinkedList<>();
        }

        public BNodeEntryIterator(LinkedList<BNode> linkedList) {
            super(linkedList);
            values = new LinkedList<>();
        }

        public BNodeEntryIterator(BNode root) {
            values = new LinkedList<>();
            linkedList.add(root);
        }

        @Override
        public boolean hasNext() {
            return !values.isEmpty() || !linkedList.isEmpty();
        }

        protected Entry<K, V> getCurrentEntry() {
            Entry<K, V> entry = values.pop();
            currentEntry = entry;
            return entry;
        }

        public Entry<K, V> next() {
            if (!values.isEmpty()) {
                return getCurrentEntry();
            } else if (!linkedList.isEmpty()) {
                BNode node = linkedList.pop();
                currentNode = node; // 当前节点赋值
                if (node.hasNodes()) {
                    linkedList.addAll(0, node.nodes);
                }
                if (node.hasValues()) {
                    values.addAll(node.values);
                }
                return getCurrentEntry();
            }
            return null;
        }

        @Override
        public void remove() {
            if (currentEntry == null) return;
            if (currentNode != null) {
                currentNode.remove(currentEntry.getKey());
            } else {
                removeKey(currentEntry.getKey());
            }
        }
    }

    /**
     * EntrySet (Map.Entry 迭代器) (中序遍历)
     */
    class EntrySet extends AbstractSet<Entry<K, V>> {

        /**
         * 根节点
         */
        private final BNode root;


        public EntrySet(BNode root) {
            this.root = root;
        }

        @NotNull
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new BNodeEntryIterator(root);
        }

        @Override
        public int size() {
            return size;
        }
    }


    /**
     * BNodeSet (BNode 迭代器) (中序遍历)
     */
    class BNodeSet extends AbstractSet<BNode> {

        /**
         * 根节点
         */
        private final BNode root;


        public BNodeSet(BNode root) {
            this.root = root;
        }

        @NotNull
        @Override
        public Iterator<BNode> iterator() {
            return new BNodeIterator(root);
        }

        @Override
        public int size() {
            return size;
        }
    }


    /**
     * MapEntry 迭代器
     */
    class BNodeSortedEntryIterator extends BaseNowIterator<Tuple<BNode, Integer>, Entry<K, V>> {
        protected LinkedList<Entry<K, V>> values;
        protected BNode currentNode;
        protected Entry<K, V> currentEntry;

        public BNodeSortedEntryIterator() {
            values = new LinkedList<>();
        }

        public BNodeSortedEntryIterator(LinkedList<Tuple<BNode, Integer>> linkedList) {
            super(linkedList);
            values = new LinkedList<>();
        }

        public BNodeSortedEntryIterator(BNode root) {
            values = new LinkedList<>();
            linkedList.add(new TupleEntry<>(root, 0));
        }

        @Override
        public boolean hasNext() {
            clearInvalid();
            return !values.isEmpty() || !linkedList.isEmpty();
        }

        protected Entry<K, V> getCurrentEntry() {
            Entry<K, V> entry = values.pop();
            currentEntry = entry;
            return entry;
        }

        /**
         * 清除遍历完成的数据
         */
        protected void clearInvalid() {
            while (!linkedList.isEmpty()) {
                // 取出元素
                Tuple<BNode, Integer> tuple = linkedList.getFirst();
                BNode now = tuple.getKey();
                int index = tuple.getValue();
                if (now.hasNodes()) {
                    if (index >= now.nodes.size()) { // 超出范围
                        linkedList.pop();
                        continue;
                    }
                }
                break;
            }
        }

        /**
         * 获取下一个元素
         *
         * @return true: 跳出循环，存在下一个元素或已遍历完
         */
        protected boolean canNextOne() {
            // 取出元素
            Tuple<BNode, Integer> tuple = linkedList.getFirst();
            BNode now = tuple.getKey();
            int index = tuple.getValue();
            if (now.hasNodes()) {
                if (index >= now.nodes.size()) { // 超出范围
                    linkedList.pop();
                    return false; // 基本没这个情况
                } else { // 在里面
                    BNode child = now.nodes.get(index);
                    linkedList.addFirst(new TupleEntry<>(child, 0));
                    tuple.setValue(index + 1); // 更新索引

                    if (index > 0 && index - 1 < now.values.size()) {
                        Entry<K, V> entry = now.values.get(index - 1);
                        values.addFirst(entry); // 节点值

                        this.currentNode = now;
                        return true; // 有值
                    } else {
                        return false;
                    }
                }
            } else { // 叶子节点
                if (now.hasValues()) {
                    values.addAll(0, now.values);
                }
                linkedList.pop();

                this.currentNode = now;
                return true;
            }
        }


        public Entry<K, V> next() {
            if (!values.isEmpty()) {
                return getCurrentEntry();
            } else if (!linkedList.isEmpty()) {
                // 能否取出元素
                while (!linkedList.isEmpty() && !canNextOne()) ;
                return getCurrentEntry();
            }
            return null;
        }

        @Override
        public void remove() {
            if (currentEntry == null) return;
            if (currentNode != null) {
                currentNode.remove(currentEntry.getKey());
            } else {
                removeKey(currentEntry.getKey());
            }
        }
    }


    /**
     * EntrySortedSet (Map.Entry 迭代器) (key 遍历)
     */
    class EntrySortedSet extends AbstractSet<Entry<K, V>> {

        /**
         * 根节点
         */
        private final BNode root;


        public EntrySortedSet(BNode root) {
            this.root = root;
        }

        @NotNull
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new BNodeSortedEntryIterator(root);
        }

        @Override
        public int size() {
            return size;
        }
    }
}
