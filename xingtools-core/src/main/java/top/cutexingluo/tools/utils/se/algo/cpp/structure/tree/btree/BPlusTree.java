package top.cutexingluo.tools.utils.se.algo.cpp.structure.tree.btree;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.base.IData;
import top.cutexingluo.tools.common.base.IValue;
import top.cutexingluo.tools.common.data.MapEntry;
import top.cutexingluo.tools.designtools.method.ClassMaker;
import top.cutexingluo.tools.utils.se.algo.cpp.base.BaseBiNodeSource;
import top.cutexingluo.tools.utils.se.algo.cpp.math.XTBinarySearch;
import top.cutexingluo.tools.utils.se.core.compare.XTComparator;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

/**
 * B+ 树
 * <p>B+ Tree</p>
 * <p>B 树的 map 实现类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/18 16:09
 * @since 1.1.6
 */
public class BPlusTree<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V>, Iterable<Map.Entry<K, V>>, Serializable {

    /**
     * 阶数 (order)
     */
    protected transient int m;

    /**
     * 元素数量
     */
    protected transient int size;

    /**
     * 节点数量
     */
    protected transient int nodeSize;

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
    protected Supplier<List<Entry<K, V>>> valuesSupplier;

    /**
     * 节点集合构造器
     */
    protected Supplier<List<BNode>> nodesSupplier;

    /**
     * 构造一个 m 阶 B 树
     *
     * @param m              阶数
     * @param valuesSupplier 值集合构造器
     * @param nodesSupplier  节点集合构造器
     */
    public BPlusTree(int m, Comparator<? super K> comparator,
                     Supplier<List<Entry<K, V>>> valuesSupplier,
                     Supplier<List<BNode>> nodesSupplier) {
        if (m <= 2) {
            throw new IllegalArgumentException("the order of BTree must be greater compare 2");
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
    public BPlusTree(int m, Comparator<? super K> comparator) {
        this(m, comparator, () -> new ArrayList<>(m), () -> new ArrayList<>(m));
    }

    /**
     * 构造一个 m 阶 B 树
     * <p>默认使用 XTComparator 作为比较器, LinkedList 作为构造器</p>
     *
     * @param m 阶数
     */
    public BPlusTree(int m) {
        this(m, new XTComparator<>(true));
    }


    /**
     * 复制一个 BTree 的属性
     *
     * @param reversed 是否反转 comparator
     * @return 新的 BTree
     */
    public BPlusTree<K, V> copyProperties(boolean reversed) {
        if (reversed) return new BPlusTree<K, V>(m, comparator.reversed(), valuesSupplier, nodesSupplier);
        return new BPlusTree<K, V>(m, comparator, valuesSupplier, nodesSupplier);
    }

    protected void init() {
        size = 0;
        nodeSize = 0;
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

    public BNode getRoot() {
        return root;
    }

    public void setRoot(BNode root) {
        this.root = root;
    }

    public Comparator<? super K> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    public Supplier<List<Entry<K, V>>> getValuesSupplier() {
        return valuesSupplier;
    }

    public void setValuesSupplier(Supplier<List<Entry<K, V>>> valuesSupplier) {
        this.valuesSupplier = valuesSupplier;
    }

    public Supplier<List<BNode>> getNodesSupplier() {
        return nodesSupplier;
    }

    public void setNodesSupplier(Supplier<List<BNode>> nodesSupplier) {
        this.nodesSupplier = nodesSupplier;
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
        BNode temp = root.searchInsertNode(key);
        Map.Entry<K, V> old = temp.insert(key, value);
        return old != null ? old.getValue() : null;
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
        if (isEmpty()) return null;
        if (!root.hasValues()) return null;
        return root.search(key);
    }

    /**
     * 查找节点
     *
     * <p>不存在返回 null</p>
     */
    public BNode searchNode(K key) {
        if (isEmpty()) return null;
        if (!root.hasValues()) return null;
        return root.searchDeleteNode(key);
    }

    /**
     * 查找节点
     *
     * <p>不存在返回 最后的叶子节点自身</p>
     */
    public BNode searchInsertNode(K key) {
        if (isEmpty()) return null;
        if (!root.hasValues()) return null;
        return root.searchInsertNode(key);
    }


    public V removeKey(K key) {
        if (isEmpty()) return null;
        if (!root.hasValues()) return null;
        V res = search(key);
        if (res == null) return null;
        root.delete(key);
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

    /**
     * 判断是否合法
     * <p>0 表示合法</p>
     * <pre>
     *     1- "左key大于右key"
     * </pre>
     */
    public int isLegal() {
        if (isEmpty()) return 0;
        int res = root.judgeLegal();

        if (res == 0) {
            BNode temp = root;
            while (!temp.isLeaf()) temp = temp.nodes.get(0);
            Entry<K, V> entry = temp.values.get(0);
            K max = entry.getKey();
            while (temp.rightNode != null) {
                for (int i = 1; i < temp.values.size(); i++) {
                    if (temp.compare(max, temp.values.get(i)) >= 0) return 1;
                    max = temp.values.get(i).getKey();
                }
                if (temp.rightNode != null) temp = temp.rightNode;
            }
        }
        return res;
    }

    // minimum

    /**
     * 该子树下索引最小的节点
     */
    public BNode minimum(final BNode node) {
        if (node == null) return null;
        if (node.isLeaf()) return node;
        return node.getMinNode();
    }

    /**
     * 索引最小的节点
     */
    @NotNull
    public BNode minimum() {
        return minimum(root);
    }

    /**
     * 索引最小的节点的 entry
     */
    public Entry<K, V> minimumEntry() {
        BNode node = minimum(root);
        return node.values.get(0);
    }

    /**
     * 索引最小的节点的 key
     */
    public K minimumKey() {
        Entry<K, V> entry = minimumEntry();
        if (entry == null) return null;
        return entry.getKey();
    }


    // maximum

    /**
     * 该子树下索引最大的节点
     */
    public BNode maximum(final BNode node) {
        if (node == null) return null;
        if (node.isLeaf()) return node;
        return node.getMaxNode();
    }

    /**
     * 索引最大的节点
     */
    @NotNull
    public BNode maximum() {
        return maximum(root);
    }

    /**
     * 索引最大的节点的 entry
     */
    public Entry<K, V> maximumEntry() {
        BNode node = maximum(root);
        if (node == null) return null;
        return node.values.get(node.values.size() - 1);
    }


    /**
     * 索引最小的节点的 key
     */
    public K maximumKey() {
        Entry<K, V> entry = maximumEntry();
        if (entry == null) return null;
        return entry.getKey();
    }


    @Override
    public Entry<K, V> lowerEntry(K key) {
        BNode node = searchInsertNode(key);
        if (node == null) return null;
        int index = XTBinarySearch.upperBoundReversed(node.getValues(),
                0, node.values.size(), Map.Entry::getKey,
                key, comparator);
        if (index < 0) {
            BNode leftNode = node.leftNode;
            if (leftNode != null) {
                return leftNode.values.get(leftNode.values.size() - 1);
            }
            return null;
        } else if (index >= node.values.size()) {
            BNode rightNode = node.rightNode;
            if (rightNode != null) {
                return rightNode.values.get(0);
            }
            return null;
        }
        return node.values.get(index);
    }

    @Override
    public K lowerKey(K key) {
        Entry<K, V> entry = lowerEntry(key);
        if (entry == null) return null;
        return entry.getKey();
    }


    @Override
    public Entry<K, V> higherEntry(K key) {
        BNode node = searchInsertNode(key);
        if (node == null) return null;
        int index = XTBinarySearch.upperBound(node.getValues(),
                0, node.values.size(), Map.Entry::getKey,
                key, comparator);
        if (index < 0) {
            BNode leftNode = node.leftNode;
            if (leftNode != null) {
                return leftNode.values.get(leftNode.values.size() - 1);
            }
            return null;
        } else if (index >= node.values.size()) {
            BNode rightNode = node.rightNode;
            if (rightNode != null) {
                return rightNode.values.get(0);
            }
            return null;
        }
        return node.values.get(index);
    }

    @Override
    public K higherKey(K key) {
        Entry<K, V> entry = higherEntry(key);
        if (entry == null) return null;
        return entry.getKey();
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        BNode node = searchInsertNode(key);
        if (node == null) return null;
        int index = XTBinarySearch.lowerBoundReversed(node.getValues(),
                0, node.values.size(), Map.Entry::getKey,
                key, comparator);
        if (index < 0) {
            BNode leftNode = node.leftNode;
            if (leftNode != null) {
                return leftNode.values.get(leftNode.values.size() - 1);
            }
            return null;
        } else if (index >= node.values.size()) {
            BNode rightNode = node.rightNode;
            if (rightNode != null) {
                return rightNode.values.get(0);
            }
            return null;
        }
        return node.values.get(index);
    }

    @Override
    public K floorKey(K key) {
        Entry<K, V> entry = floorEntry(key);
        if (entry == null) return null;
        return entry.getKey();
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        BNode node = searchInsertNode(key);
        if (node == null) return null;
        int index = XTBinarySearch.lowerBound(node.getValues(),
                0, node.values.size(), Map.Entry::getKey,
                key, comparator);
        if (index < 0) {
            BNode leftNode = node.leftNode;
            if (leftNode != null) {
                return leftNode.values.get(leftNode.values.size() - 1);
            }
            return null;
        } else if (index >= node.values.size()) {
            BNode rightNode = node.rightNode;
            if (rightNode != null) {
                return rightNode.values.get(0);
            }
            return null;
        }
        return node.values.get(index);
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
        if (!node.values.isEmpty()) return node.values.get(0);
        return null;
    }

    @Override
    public Entry<K, V> lastEntry() {
        BNode node = maximum();
        if (!node.values.isEmpty()) return node.values.get(node.values.size() - 1);
        return null;
    }

    @Override
    public Entry<K, V> pollFirstEntry() {
        BNode node = minimum();
        return node.deleteCurrentNode(0, null);
    }

    @Override
    public Entry<K, V> pollLastEntry() {
        BNode node = minimum();
        return node.deleteCurrentNode(node.values.size() - 1, null);
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        BPlusTree<K, V> tree = copyProperties(true);
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
        BPlusTree<K, V> tree = copyProperties(false);
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
        BPlusTree<K, V> tree = copyProperties(false);
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
        BPlusTree<K, V> tree = copyProperties(false);
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
        BPlusTree<K, V> tree = copyProperties(false);
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
        BPlusTree<K, V> tree = copyProperties(false);
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
        BPlusTree<K, V> tree = copyProperties(false);
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
    public class BNode implements BaseBiNodeSource<BNode>, IData<List<BNode>>, IValue<List<Entry<K, V>>> {
        /**
         * 父节点
         */
        protected BNode parent;
        /**
         * 键值对
         */
        @NotNull
        protected List<Map.Entry<K, V>> values;
        /**
         * 链接的节点
         */
        protected List<BNode> nodes;

        /**
         * 左节点, 前驱节点
         */
        protected BNode leftNode;

        /**
         * 右节点, 后继节点
         */
        protected BNode rightNode;

        public BNode(BNode parent) {
            this.parent = parent;
            this.values = valuesSupplier.get();
            this.nodes = null;
            nodeSize++;
        }

        public BNode(BNode parent, @NotNull List<Entry<K, V>> values) {
            this.parent = parent;
            this.values = values;
            this.nodes = null;
            nodeSize++;
        }

        public BNode(BNode parent,
                     @NotNull List<Entry<K, V>> values,
                     List<BNode> nodes) {
            this.parent = parent;
            this.values = values;
            this.nodes = nodes;
            nodeSize++;
        }

        @Override
        public BNode parentNode() {
            return parent;
        }

        @Override
        public void setParentNode(BNode parentNode) {
            this.parent = parentNode;
        }

        @Override
        public BNode leftNode() {
            return leftNode;
        }

        @Override
        public BNode rightNode() {
            return rightNode;
        }


        @Override
        public List<BNode> data() {
            return this.nodes;
        }

        @Override
        public List<Map.Entry<K, V>> getValue() {
            return this.values;
        }


        @Override
        public String toString() {
            return "BNode{" +
                    "values=" + values +
                    ", nodes=" + nodes +
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
            if (parent == null) return values.size() - 1;
            if (!isLeaf() && nodes.size() != values.size() + 1) return -1;
            return values.size() - (m / 2);
        }

        /**
         * 判断是否 是有值数据
         */
        public boolean hasValuesData() {
            for (Entry<K, V> entry : values) {
                if (entry.getValue() != null) return true;
            }
            return false;
        }

        public BNode getMaxNode() {
            if (isLeaf()) return this;
            return this.nodes.get(this.nodes.size() - 1).getMaxNode();
        }

        public BNode getMinNode() {
            if (isLeaf()) return this;
            return nodes.get(0).getMinNode();
        }

        protected void changeParent() {
            if (isLeaf()) return;
            for (BNode node : this.nodes) {
                node.parent = this;
            }
        }

        /**
         * 创建非叶子节点的键
         */
        protected Entry<K, V> newKeyEntry(K key) {
            return new MapEntry<>(key, null);
        }

        /**
         * 创建非叶子节点的键
         */
        protected Entry<K, V> newKeyEntry(@NotNull Map.Entry<K, V> entry) {
            return new MapEntry<>(entry.getKey(), null);
        }

        /**
         * 比较键
         */
        protected int compare(K key, @NotNull Map.Entry<K, V> entry) {
            return comparator.compare(key, entry.getKey());
        }

        /**
         * 比较键
         */
        protected int compare(K key, K otherKey) {
            return comparator.compare(key, otherKey);
        }

        /**
         * 二分查找, 找不到返回最后一次查找的位置
         */
        protected int binarySearch(K key) {
            int l = 0, r = values.size() - 1;
            int m = l + ((r - l) >> 1);
            while (l < r) {
                int compare = compare(key, values.get(m));
                if (compare == 0) return m;
                else if (compare > 0) l = m + 1;
                else r = m - 1;
                m = (l + r) >> 1;
            }
            if (m < 0) m = 0;
            return m;
        }


        protected int getInParentPosition(K key) {
            if (parent == null) throw new IllegalStateException("the node has no parent.");
            int position = parent.binarySearch(key);
            return compare(key, parent.values.get(position)) >= 0 ? position + 1 : position;
        }

        /**
         * 查找
         */
        public Map.Entry<K, V> searchEntry(K key) {
            // 非叶子节点左右减枝
            if (!isLeaf() && compare(key, values.get(0)) < 0)
                return nodes.get(0).searchEntry(key);
            if (!isLeaf() && compare(key, values.get(values.size() - 1)) > 0)
                return nodes.get(values.size()).searchEntry(key);

            // 对节点二分查找
            int position = binarySearch(key);
            int compare = compare(key, values.get(position));

            // 是叶子节点 存在返回对应数据 不存在返回null
            if (isLeaf()) {
                if (compare == 0) return values.get(position);
                else return null;
            }
            // 不是叶子节点 找到对应key 去右子节点找最小值返回
            // 没找到对应key 大于则去右子节点递归查询 小于去左子节点递归查询
            if (compare == 0) {
                return nodes.get(position + 1).getMinNode().values.get(0);
            } else if (compare > 0) {
                return nodes.get(position + 1).searchEntry(key);
            }
            return nodes.get(position).searchEntry(key);
        }

        /**
         * 搜索
         */
        public V search(K key) {
            Map.Entry<K, V> entry = searchEntry(key);
            return entry == null ? null : entry.getValue();
        }

        /**
         * 搜索将插入节点
         *
         * <p>到达叶子节点返回自身</p>
         */
        public BNode searchInsertNode(K key) {
            if (isLeaf()) return this;
            // 非叶子节点左右减枝
            if (compare(key, values.get(0)) < 0)
                return nodes.get(0).searchInsertNode(key);
            if (compare(key, values.get(values.size() - 1)) > 0)
                return nodes.get(nodes.size() - 1).searchInsertNode(key);
            int position = binarySearch(key);
            int compare = compare(key, values.get(position));
            if (compare >= 0) compare = 1;
            else compare = 0;
            return nodes.get(position + compare).searchInsertNode(key);
        }

        /**
         * 搜索将删除节点
         *
         * <p>到达叶子节点没有则返回null</p>
         */
        public BNode searchDeleteNode(K key) {
            if (isLeaf()) return this;
            // 非叶子节点左右剪枝
            if (compare(key, values.get(0)) < 0)
                return nodes.get(0).searchDeleteNode(key);
            if (compare(key, values.get(values.size() - 1)) > 0)
                return nodes.get(nodes.size() - 1).searchDeleteNode(key);
            int position = binarySearch(key);
            int compare = compare(key, values.get(position));
            if (compare == 0) return this;
            if (!isLeaf()) {
                compare = compare > 0 ? 1 : 0;
                return nodes.get(position + compare).searchDeleteNode(key);
            }
            return null;
        }


        /**
         * 插入
         */
        public Map.Entry<K, V> insert(Entry<K, V> entry) {
            if (!isLeaf()) throw new IllegalStateException("this node is not a leaf node, can not insert");
            if (values.size() == 0) {
                this.values.add(entry);
                size++;
                return null;
            }
            int position = binarySearch(entry.getKey());
            int compare = compare(entry.getKey(), values.get(position));
            if (compare == 0) {
                return this.values.set(position, entry);
            }
            if (compare > 0) position += 1;
            this.values.add(position, entry);
            size++;

            if (isLegal() > 0) overflow();
            return null;
        }

        /**
         * 插入
         */
        public Map.Entry<K, V> insert(K key, V value) {
            return insert(new MapEntry<>(key, value));
        }

        // check pass

        /**
         * 上溢
         */
        protected void overflow() {
            if (values.size() < m) return;
            int mid = values.size() >> 1;

            // 当叶子节点分裂时，数据需要多保存一份
            // 使用 temp 变量分割数据
            int temp = isLeaf() ? 0 : 1;

            Entry<K, V> entry = values.get(mid);

            // 分离右节点
            BNode rn = new BNode(parent);
            rn.values = valuesSupplier.get();
            rn.values.addAll(values.subList(mid + temp, values.size()));

            // 当前节点成为左节点
            this.values = values.subList(0, mid);

            // 非叶子节点分离子节点
            if (!isLeaf()) {


                rn.nodes = nodesSupplier.get();
                rn.nodes.addAll(nodes.subList(mid + 1, nodes.size()));
                rn.changeParent();

                this.nodes = nodes.subList(0, mid + 1);
            } else {
//            if (isLeaf()) {
                // 分离右节点数据
//                rn.data = valuesSupplier.get();
//                rn.data.addAll(data.subList(mid + temp, data.size()));
//                rn.data = new ArrayList<>(data.subList(mid + temp, data.size()));

                // 分离左节点数据
//                data = data.subList(0, mid);

                // 叶子节点连接调整
                if (this.rightNode != null) {
                    rn.rightNode = this.rightNode;
                    this.rightNode.leftNode = rn;
                }
                this.rightNode = rn;
                rn.leftNode = this;
            }

            // 没有父节点时上溢出新节点
            if (parent == null) {
                parent = new BNode(null);
                parent.values.add(newKeyEntry(entry.getKey()));
                parent.nodes = nodesSupplier.get();
                parent.nodes.add(this);
                parent.nodes.add(rn);
                rn.parent = parent;
                // 新父节点替换外层根节点
                root = parent;
                return;
            }

            int inParentPosition = getInParentPosition(values.get(0).getKey());
            parent.values.add(inParentPosition, newKeyEntry(entry.getKey()));
            parent.nodes.add(inParentPosition + 1, rn);
            if (isLeaf() && parent.nodes.size() > inParentPosition + 2) {
                BNode node = parent.nodes.get(inParentPosition + 2);
                rn.rightNode = node;
                node.leftNode = rn;
            }
            if (parent.isLegal() > 0) parent.overflow();
        }

        /**
         * 删除
         */
        public Entry<K, V> delete(K key) {
            BNode node = searchDeleteNode(key);
            if (node == null) return null;

            int rp = node.binarySearch(key);

            return node.deleteCurrentNode(rp, key);
        }

        // check pass

        /**
         * 删除索引
         * <p>优先按当前节点索引查询数据，rp = -1 时按 key</p>
         */
        public Entry<K, V> deleteCurrentNode(int rp, K defaultKey) {
            if (rp < -1 || rp == -1 && defaultKey == null) {
                throw new IllegalArgumentException("delete index  or defaultKey  error");
            }
            BNode node;
            K key = defaultKey;
            // 使 key, rp, node 都不为空
            if (rp != -1) { // rp 存在
                node = this;
                if (key == null) key = values.get(rp).getKey();
                if (key == null) return null;
            } else { // key 存在
                node = searchDeleteNode(key);
                if (node == null) return null;
                rp = node.binarySearch(key);
            }
            if (rp >= node.values.size()) {
                throw new IllegalArgumentException("delete index  on overflow for node.values");
            }

            // 原版代码
            Entry<K, V> removeEntry;
            // 删除的两种情况 叶子节点和非叶子节点
            if (node.isLeaf()) {
                removeEntry = node.values.remove(rp);
                size--; // 叶子节点删除元素后数量减少
                if (node.parent == null && node.nodes == null) return removeEntry;
                node.adjust(key, null);
            } else {
                // 非叶子节点 找到右子节点的最小节点删除第一位后调整节点
                BNode minNode = node.nodes.get(rp + 1).getMinNode();
                removeEntry = minNode.values.remove(0);
                size--; // 叶子节点删除元素后数量减少
                minNode.adjust(key, node);
            }
            return removeEntry;
        }

        /**
         * 调整
         */
        protected void adjust(K key, BNode supperNode) {
            if (isLegal() >= 0) {
                if (supperNode != null) {
                    int i = supperNode.binarySearch(key);
                    supperNode.values.set(i, newKeyEntry(values.get(0)));
                }
                return;
            }
            if (parent == null) {
                BNode temp = new BNode(null);
                for (BNode node : nodes) {
                    temp.values.addAll(node.values);
                    if (!node.isLeaf()) {
                        if (temp.nodes == null) temp.nodes = nodesSupplier.get();
                        temp.nodes.addAll(node.nodes);
                    }
                }
                temp.changeParent();
                root = temp;
                return;
            }

            int inPrent = getInParentPosition(key);

            if (inPrent < parent.nodes.size() - 1 && parent.nodes.get(inPrent + 1).isLegal() > 0) {
                // 可以从右兄弟节点拿取节点
                BNode prNode = parent.nodes.get(inPrent + 1);
                Entry<K, V> rightEntry = prNode.values.remove(0);

                if (prNode.isLeaf()) {
                    parent.values.set(inPrent, newKeyEntry(prNode.values.get(0)));
                    values.add(rightEntry);
//                    Entry<K, V> rd = prNode.values.remove(0);
//                    values.add(rd);
                }

                if (!prNode.isLeaf()) {
                    Entry<K, V> parentEntry = parent.values.get(inPrent);
                    parent.values.set(inPrent, newKeyEntry(rightEntry));
                    values.add(newKeyEntry(parentEntry));
                    BNode rn = prNode.nodes.remove(0);
                    rn.parent = this;
                    nodes.add(rn);
                }

                if (supperNode != null) {
                    int i = supperNode.binarySearch(key);
                    if (supperNode.values.size() > i && compare(key, supperNode.values.get(i)) == 0)
                        supperNode.values.set(i, newKeyEntry(values.get(0)));
                }

            } else if (inPrent > 0 && parent.nodes.get(inPrent - 1).isLegal() > 0) {
                // 左兄弟可以拿节点
                BNode plNode = parent.nodes.get(inPrent - 1);
                Entry<K, V> rightEntry = plNode.values.remove(plNode.values.size() - 1);
                if (plNode.isLeaf()) {
                    values.add(0, rightEntry);
                    parent.values.set(inPrent - 1, newKeyEntry(rightEntry));
//                    Object rd = plNode.data.remove(leftNode.data.size() - 1);
//                    data.add(0, rd);
                }
                if (!plNode.isLeaf()) {
                    Entry<K, V> parentEntry = parent.values.get(inPrent - 1);
                    parent.values.set(inPrent - 1, newKeyEntry(rightEntry));
                    values.add(0, newKeyEntry(parentEntry));
                    BNode rn = plNode.nodes.remove(plNode.nodes.size() - 1);
                    rn.parent = this;
                    nodes.add(0, rn);
                }

                if (supperNode != null) {
                    int i = supperNode.binarySearch(key);
                    if (supperNode.values.size() > i && compare(key, supperNode.values.get(i)) == 0)
                        supperNode.values.set(i, newKeyEntry(values.get(0)));
                }
            } else {
                // 左右无富余 进行合并节点
                Entry<K, V> rk = null;
                if (inPrent == 0) {
                    BNode prNode = parent.nodes.get(inPrent + 1);

                    rk = parent.values.remove(inPrent);
                    if (!isLeaf()) {
                        nodes.addAll(prNode.nodes);
                        changeParent();
                        values.add(newKeyEntry(rk));
                    }

                    values.addAll(prNode.values);
                    if (isLeaf()) {
//                        data.addAll(prNode.data);
                        rightNode = prNode.rightNode;
                        if (rightNode != null) {
                            rightNode.leftNode = this;
                        }
                    }
                    parent.nodes.remove(inPrent + 1);
                    if (supperNode != null) {
                        int i = supperNode.binarySearch(key);
                        if (supperNode.values.size() > i && compare(key, supperNode.values.get(i)) == 0)
                            supperNode.values.set(i, newKeyEntry(values.get(0)));
                    }
                } else {
                    BNode plNode = parent.nodes.get(inPrent - 1);

                    rk = parent.values.remove(inPrent - 1);
                    if (!isLeaf()) {
                        plNode.nodes.addAll(nodes);
                        plNode.changeParent();
                        plNode.values.add(newKeyEntry(rk));
                    }

                    plNode.values.addAll(values);
                    if (isLeaf()) {
//                        leftNode.data.addAll(data);
                        plNode.rightNode = rightNode;
                        if (rightNode != null) rightNode.leftNode = plNode;
                    }
                    parent.nodes.remove(inPrent);
                    if (supperNode != null) {
                        int i = supperNode.binarySearch(key);
                        if (supperNode.values.size() > i && compare(key, supperNode.values.get(i)) == 0)
                            supperNode.values.set(i, newKeyEntry(plNode.values.get(0)));
                    }
                }
                parent.adjust(rk.getKey(), null);
            }
        }


        /**
         * 判断是否合法
         * <p>0 合法，返回错误码</p>
         *
         * <pre>
         *     1-"非叶子节点存储数据"
         *     2-"节点的右节点最小key不等于当前key"
         *     3-"左兄弟节点的右指针不指向当前"
         *     4-"右兄弟节点的左指针不指向当前"
         *     5-"出现重复key"
         *     6-"key排序不正确"
         *     7-"错误: 子节点树不匹配关键字数+1"
         *     8-"子节点的父节点不关联"
         *     9-"父节点key等于左子节点key"
         *     10-"父节点key小于左子节点key"
         *     11-"父节点key大于右子节点key"
         * </pre>
         */
        public int judgeLegal() {
            if (parent == null && nodes == null) return 0;

            if (nodes != null && hasValuesData()) {
                return 1;
            }
            if (!isLeaf()) {
//                System.out.println("nodes: " + nodes);
//                System.out.println("values: " + values);
                for (int i = 1; i < nodes.size(); i++) {
                    BNode node = nodes.get(i);
                    BNode minNode = node.getMinNode();
                    Entry<K, V> entry = minNode.values.get(0);
                    K minKey = entry.getKey();
                    K key = values.get(i - 1).getKey();
//                    System.out.println("node: " + node + " key: " + key + "minNode:" + minNode + " minKey: " + minKey);
                    if (compare(key, minKey) != 0) {
                        return 2;
                    }
                }
            }

            if (isLeaf()) {
                if (leftNode != null && leftNode.rightNode != this) return 3;
                if (rightNode != null && rightNode.leftNode != this) return 4;
            }

            for (int i = 1; i < values.size(); i++) {
                int compare = compare(values.get(i - 1).getKey(), values.get(i));
                if (compare == 0) return 5;
                if (compare > 0) return 6;
            }

            if (nodes != null) {
                if (nodes.size() != values.size() + 1) return 7;

                for (BNode node : nodes) if (node.parent != this) return 8;

                for (int i = 0; i < values.size(); i++) {
                    K key = values.get(i).getKey();
                    BNode left = nodes.get(i);
                    for (Map.Entry<K, V> entry : left.values) {
                        K lk = entry.getKey();
                        int than = compare(key, lk);
                        if (than == 0) return 9;
                        if (than < 0) return 10;
                    }
                    BNode right = nodes.get(i + 1);
                    for (Map.Entry<K, V> entry : right.values) {
                        K rk = entry.getKey();
                        int than = compare(key, rk);
                        if (than > 0) return 11;
                    }
                    for (BNode node : nodes) {
                        int code = node.judgeLegal();
                        if (!Objects.equals(code, 0)) return code;
                    }
                }
            }
            return 0;
        }
    }

    // 迭代器
    @NotNull
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new BNodeEntryIterator(root, true);
    }

    // set
    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet(root);
    }

    // 迭代器和 set 类

    /**
     * MapEntry 迭代器 (顺序遍历)
     */
    class BNodeEntryIterator implements Iterator<Entry<K, V>> {
        protected BNode currentNode;
        protected int currentValuesIndex;
        protected boolean isAsc;


        public BNodeEntryIterator(BNode root, boolean isAsc) {
            this.isAsc = isAsc;
            if (isAsc) {
                currentNode = root.getMinNode();
                currentValuesIndex = 0;
            } else {
                currentNode = root.getMaxNode();
                currentValuesIndex = currentNode.values.size() - 1;
            }
        }

        protected boolean checkBound() {
            return currentValuesIndex >= 0 && currentValuesIndex < currentNode.values.size();
        }

        @Override
        public boolean hasNext() {
            return currentNode != null && checkBound();
        }

        @Override
        public Entry<K, V> next() {
            if (currentNode != null) {
                if (checkBound()) {
                    if (isAsc) {
                        Entry<K, V> entry = currentNode.values.get(currentValuesIndex++);
                        if (!checkBound()) {
                            currentNode = currentNode.rightNode;
                            if (currentNode != null) {
                                currentValuesIndex = 0;
                            }
                        }
                        return entry;
                    } else {
                        Entry<K, V> entry = currentNode.values.get(currentValuesIndex--);
                        if (!checkBound()) {
                            currentNode = currentNode.leftNode;
                            if (currentNode != null) {
                                currentValuesIndex = currentNode.values.size() - 1;
                            }
                        }
                        return entry;
                    }
                }
            }
            return null;
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
            return new BNodeEntryIterator(root, true);
        }

        @Override
        public int size() {
            return size;
        }
    }

}
