package top.cutexingluo.tools.utils.se.algo.cpp.structure.tree;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.base.IData;
import top.cutexingluo.tools.common.base.IValue;
import top.cutexingluo.tools.utils.se.algo.cpp.base.BaseNode;
import top.cutexingluo.tools.utils.se.algo.cpp.base.BaseNodeIterator;

import java.util.*;

/**
 * AVL/BBT
 * <p>平衡二叉树</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/25 22:04
 * @since 1.0.3
 */
public class BBTree<K extends Comparable<K>, V> extends AbstractMap<K, V> {

    /**
     * 根节点
     */
    protected Node root;
    /**
     * 节点数量
     */
    protected int N;


    /**
     * 节点类
     */
    @AllArgsConstructor
    protected class Node implements Entry<K, V>, BaseNode<Node>, IData<Entry<K, V>>, IValue<V> {
        private K key;
        private V value;
        private Node left;
        private Node right;
        /**
         * 以该节点为根的树的高度
         */
        private int h;
        /**
         * 是否线索化，左子节点是否为线索
         */
        private boolean lFlag;
        /**
         * 是否线索化，右子节点是否为线索
         */
        private boolean rFlag;

        public Node(K key, V value) {
            this(key, value, null, null);
        }

        public Node(K key, V value, Node left, Node right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.h = 1;
            this.lFlag = false;
            this.rFlag = false;
        }

        public Entry<K, V> toEntry() {
            return this;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }

        @Override
        public Node leftNode() {
            return left;
        }

        @Override
        public Node rightNode() {
            return right;
        }

        @Override
        public String toString() {
            return "BBTNode{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }

        @Override
        public Entry<K, V> data() {
            return toEntry();
        }
    }


    /**
     * 树的大小
     *
     * @return 节点数量
     */
    @Override
    public int size() {
        return N;
    }


    /**
     * 树是否为空
     */
    @Override
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * 添加节点
     */
    @NotNull
    @Override
    public V put(K key, V value) {
        root = put(root, key, value);
        return value;
    }

    protected Node put(Node n, K key, V value) {
        if (n == null) {
            N++;//节点为空
            return new Node(key, value);
        }
        int cmp = key.compareTo(n.key);
        if (cmp > 0) {
            n.right = put(n.right, key, value);
            //判断是否需要旋转
            if (height(n.right) - height(n.left) == 2) {
                if (key.compareTo(n.right.key) > 0)
                    n = leftRotate(n);//RR
                else
                    n = rightLeftRotate(n);//RL
            }
        } else if (cmp < 0) {
            n.left = put(n.left, key, value);
            if (height(n.left) - height(n.right) == 2) {
                if (key.compareTo(n.left.key) < 0)
                    n = rightRotate(n);//LL
                else
                    n = leftRightRotate(n);//LR
            }
        } else
            n.value = value;//存在key,直接修改
        n.h = Math.max(height(n.left), height(n.right)) + 1;//更新高度
        return n;
    }

    /**
     * 查找节点
     */
    @Override
    public V get(Object key) {
        if (key != null) {
            try {
                K newKey = (K) key;
                return get(root, newKey);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }


    /**
     * 查找节点
     *
     * @param key 键
     */
    public V get(K key) {
        return get(root, key);
    }

    protected V get(Node n, K key) {
        if (n == null)//不存在
            return null;
        int cmp = key.compareTo(n.key);
        if (cmp > 0)
            return get(n.right, key);
        else if (cmp < 0)
            return get(n.left, key);
        else
            return n.value;//已存在
    }


    /**
     * 是否包含节点
     */
    @Override
    public boolean containsKey(Object key) {
        if (key != null) {
            try {
                K newKey = (K) key;
                return containsKey(newKey);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 是否包含键
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * 是否包含值
     */
    @Override
    public boolean containsValue(Object value) {
        return recursivePreErgodic(root, value);
    }

    protected boolean recursivePreErgodic(Node n, @NotNull Object value) {
        if (n == null)
            return false;
        if (value.equals(n.value)) return true;
        if (recursivePreErgodic(n.left, value)) return true;
        return recursivePreErgodic(n.right, value);
    }

    /**
     * 删除节点
     */
    @Override
    public V remove(Object key) {
        if (key != null) {
            try {
                K newKey = (K) key;
                return remove(root, newKey).value;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 删除节点
     */
    public Node remove(K key) {
        return remove(root, key);
    }

    protected Node remove(Node n, K key) {
        if (n == null || key == null)//不存在
            return null;
        //查找key对应节点
        int cmp = key.compareTo(n.key);
        if (cmp > 0) {
            n.right = remove(n.right, key);
        } else if (cmp < 0) {
            n.left = remove(n.left, key);
        } else {
            N--;
            if (n.right == null)
                n = n.left;
            else if (n.left == null)
                n = n.right;
            else {
                //查找右子树最小节点
                Node min = min(n.right);
                //替换当前节点
                n.key = min.key;
                n.value = min.value;
                n.right = remove(n.right, min.key);//删除min
            }
        }
        if (n == null) return null;
        //左子树较高
        if (height(n.left) - height(n.right) >= 2) {
            if (height(n.left.left) > height(n.left.right))
                return rightRotate(n);
            return leftRightRotate(n);
        } else if (height(n.right) - height(n.left) >= 2) {//右子树较高
            if (height(n.right.right) > height(n.right.left))
                return leftRotate(n);
            return rightLeftRotate(n);
        }
        n.h = Math.max(height(n.left), height(n.right)) + 1;//更新高度
        return n;
    }

    protected Node leftRightRotate(Node n) {
        n.left = leftRotate(n.left);
        return rightRotate(n);
    }

    protected Node rightLeftRotate(Node n) {
        n.right = rightRotate(n.right);
        return leftRotate(n);
    }

    protected Node rightRotate(Node n) {
        Node ans = n.left;
        n.left = ans.right;
        ans.right = n;
        n.h = Math.max(height(n.left), height(n.right)) + 1;
        ans.h = Math.max(height(ans.left), n.h) + 1;
        return ans;
    }

    protected Node leftRotate(Node n) {
        Node ans = n.right;
        n.right = ans.left;
        ans.left = n;
        n.h = Math.max(height(n.left), height(n.right)) + 1;
        ans.h = Math.max(height(ans.right), n.h) + 1;
        return ans;
    }

    //返回最小键
    public K min() {
        return min(root).key;
    }

    protected Node min(@NotNull Node n) {
        if (n.left != null)
            return min(n.left);
        return n;
    }

    //返回最大键
    public K max() {
        return max(root).key;
    }

    protected Node max(@NotNull Node n) {
        if (n.right != null)
            return max(n.right);
        return n;
    }

    /**
     * 树的最大深度(根节点为1)
     */
    public int height(Node n) {
        return n == null ? 0 : n.h;
    }

    /**
     * 前序遍历
     *
     * @return
     */
    public Deque<K> preErgodic() {
        return recursivePreErgodic(root, new ArrayDeque<K>());
    }


    /**
     * 递归
     */
    protected Deque<K> recursivePreErgodic(Node n, Deque<K> keys) {
        if (n == null)
            return null;
        keys.add(n.key);
        recursivePreErgodic(n.left, keys);
        recursivePreErgodic(n.right, keys);
        return keys;
    }

    /**
     * 前序遍历获取node
     */
    public Deque<Node> preErgodicGetNodes() {
        return recursivePreErgodicGetNodes(root, new ArrayDeque<>());
    }

    /**
     * 前序遍历获取Map.Entry
     */
    public Deque<Entry<K, V>> preErgodicGetEntries() {
        return recursivePreErgodicGetEntries(root, new ArrayDeque<>());
    }

    /**
     * 递归
     */
    protected Deque<Entry<K, V>> recursivePreErgodicGetEntries(Node n, Deque<Entry<K, V>> newDeque) {
        if (n == null)
            return null;
        newDeque.add(n.toEntry());
        recursivePreErgodic(n.left, newDeque);
        recursivePreErgodic(n.right, newDeque);
        return newDeque;
    }

    /**
     * 递归
     */
    protected Deque<Node> recursivePreErgodicGetNodes(Node n, Deque<Node> newDeque) {
        if (n == null)
            return null;
        newDeque.add(n);
        recursivePreErgodic(n.left, newDeque);
        recursivePreErgodic(n.right, newDeque);
        return newDeque;
    }

    /**
     * 层序遍历
     */
    public Deque<K> layerErgodic() {
        Deque<Node> q = new ArrayDeque<>();
        q.add(root);
        return recursiveLayerErgodic(q, new ArrayDeque<>());
    }


    /**
     * 非递归
     */
    public Deque<K> nonRecursiveLayerErgodic() {
        Deque<K> keys = new ArrayDeque<>();
        Deque<Node> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            Node n = q.poll();
            keys.add(n.key);
            if (n.left != null)
                q.add(n.left);
            if (n.right != null)
                q.add(n.right);
        }
        return keys;
    }


    /**
     * 递归添加
     *
     * @param q    原数据
     * @param keys 目标数据
     */
    public Deque<K> recursiveLayerErgodic(Deque<Node> q, Deque<K> keys) {
        if (q.isEmpty()) return keys;
        Node n = q.poll();
        keys.add(n.key);
        if (n.left != null)
            q.add(n.left);
        if (n.right != null)
            q.add(n.right);
        return recursiveLayerErgodic(q, keys);
    }

    /**
     * 中序线索化
     */
    public void inThread() {
        inThread(root, null).rFlag = true;
    }

    protected Node inThread(Node n, Node pre) {
        if (n == null) return null;
        if (n.left != null)//如果左子树不为空,获取左子树中序最后一个为当前节点前驱
            pre = inThread(n.left, pre);
        //当前节点的左子节点线索化
        if (n.left == null) {
            n.left = pre;
            n.lFlag = true;
        }
        //前驱节点的右子节点线索化
        if (pre != null && pre.right == null) {
            pre.right = n;
            pre.rFlag = true;
        }
        if (n.right != null)//如果右子树不为空,返回右子树中序最后一个作为下一节点前驱
            pre = inThread(n.right, n);
        else pre = n;//否则当前节点即为中序最后一个
        return pre;
    }


    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        super.putAll(m);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return super.keySet();
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return super.values();
    }

    @NotNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    final class BBTreeIterator extends BaseNodeIterator<Node, Entry<K, V>> {
        public BBTreeIterator() {
            super(BBTree.this.size(), BBTree.this.root);
        }

        @Override
        public void remove() {
            if (currentNode != null) {
                BBTree.this.remove(currentNode, currentNode.key);
            }
        }
    }


    final class EntrySet extends AbstractSet<Entry<K, V>> {

        @NotNull
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new BBTreeIterator();
        }

        @Override
        public int size() {
            return BBTree.this.size();
        }


    }

}
