package top.cutexingluo.tools.utils.se.algo.cpp.structure.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典树
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/27 11:04
 * @since 1.0.3
 */
public class TrieTree {

    /**
     * 根节点，不存储值
     */
    protected Node root;


    /**
     * 节点类
     */
    public static class Node {
        /**
         * 以下标为26个字母,存储下一个节点
         */
        private final Node[] child;
        /**
         * 计数
         */
        private int cnt;
        /**
         * 是否为叶子节点
         */
        private boolean isLeaf;

        public Node() {
            child = new Node[26];
            cnt = 0;
            isLeaf = false;
        }

        protected boolean containsKey(char key) {
            return child[key - 'a'] != null;
        }

        protected Node get(char key) {
            return child[key - 'a'];
        }

        protected void put(char key, Node n) {
            child[key - 'a'] = n;
        }
    }

    public TrieTree() {
        root = new Node();
    }


    /**
     * 添加字符串
     */
    public void put(String word) {
        word = word.toLowerCase();
        char[] chs = word.toCharArray();
        int n = chs.length;
        Node cur = root;
        //遍历每个字符
        for (char ch : chs) {
            if (!cur.containsKey(ch)) {
                cur.put(ch, new Node());
            }
            cur = cur.get(ch);//继续寻找下一个字符
            cur.cnt++;
        }
        cur.get(chs[n - 1]).isLeaf = true;
    }


    /**
     * 查询是否存在字符串
     */
    public boolean contains(String word) {
        word = word.toLowerCase();
        char[] chs = word.toCharArray();
        int n = chs.length;
        Node cur = root;
        for (char ch : chs) {
            if (!cur.containsKey(ch))
                return false;
            cur = cur.get(ch);
        }
        if (cur.isLeaf)//判断是否为叶子
            return true;
        else return false;
    }

    /**
     * 删除字符串
     */
    public void remove(String word) {
        word = word.toLowerCase();
        char[] chs = word.toCharArray();
        int n = chs.length;
        Node cur = root;
        for (char ch : chs) {
            if (!cur.containsKey(ch))
                return;
            cur = cur.get(ch);
        }
        if (cur.isLeaf)//判断是否为叶子
            cur.cnt--;
    }


    /**
     * 统计所有字符串
     */
    public List<String> getAll() {
        return preOrder(root, new ArrayList<>(), "");
    }

    private List<String> preOrder(Node n, List<String> l, String s) {
        if (n == null)
            return l;
        //前序遍历
        if (n.cnt > 0)
            l.add(s);
        for (int i = 0; i < n.child.length; i++) {
            if (n.child[i] != null) {
                char ch = (char) ('a' + i);
                preOrder(n.child[i], l, s + ch);
            }
        }
        return l;
    }

    /**
     * 查询含有某前缀的字符串数量
     */
    public int startsWith(String prefix) {
        Node cur = root;
        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            if (cur.get(ch) == null)
                return 0;
            cur = cur.get(ch);
        }
        return cur.cnt;
    }
}
