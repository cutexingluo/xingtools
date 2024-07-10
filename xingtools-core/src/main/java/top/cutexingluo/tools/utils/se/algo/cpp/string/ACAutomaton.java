package top.cutexingluo.tools.utils.se.algo.cpp.string;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * AC自动机
 * <p>多模式串匹配</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/25 20:35
 * @since 1.0.3
 */
public class ACAutomaton {
    private static Node root;//根节点,没有值

    //内部节点类

    /**
     * 内部节点类
     */
    private static class Node {
        /**
         * 子节点
         * <p>以下标为26个字母,存储下一个节点</p>
         */
        private Node[] child;//以下标为26个字母,存储下一个节点
        /**
         * 是否为叶子节点
         */
        private int cnt;//是否为叶子节点
        /**
         * 失配指针
         */
        private Node fail;//失配指针

        public Node() {
            child = new Node[26];
            cnt = 0;
            fail = null;
        }

        private boolean contains(char key) {
            return child[key - 'a'] != null;
        }

        private Node get(char key) {
            return child[key - 'a'];
        }

        private void put(char key, Node n) {
            child[key - 'a'] = n;
        }
    }

    //

    /**
     * 多模式匹配
     *
     * @param text     文本
     * @param patterns 多模式串
     * @return 匹配数
     */
    public static int match(String text, String[] patterns) {
        root = new Node();
        //所有pattern加入树中
        for (String pattern : patterns) {
            put(pattern);
        }
        //更新fail指针
        getFail();
        //执行匹配
        int cnt = 0;
        Node cur = root;
        for (int i = 0; i < text.length(); i++) {
            cur = cur.get(text.charAt(i));
            for (Node tmp = cur; tmp != root && tmp.cnt != -1; tmp = tmp.fail) {
                cnt += tmp.cnt;
                tmp.cnt = -1;
            }
        }
        return cnt;
    }

    /**
     * 添加字符串
     */
    private static void put(String word) {
        word = word.toLowerCase();
        char[] chs = word.toCharArray();
        int n = chs.length;
        Node cur = root;
        //遍历每个字符
        for (int i = 0; i < n; i++) {
            //如果不包含其中某个字符,创建新节点
            if (!cur.contains(chs[i])) {
                cur.put(chs[i], new Node());
            }
            cur = cur.get(chs[i]);//如果包含则继续寻找下一个字符
        }
        cur.cnt++;
    }

    /**
     * 获取失配指针
     */
    private static void getFail() {
        Deque<Node> q = new ArrayDeque<>();
        //第二层节点fail都指向root
        for (int i = 0; i < 26; i++) {
            if (root.child[i] != null) {
                root.child[i].fail = root;
                q.add(root.child[i]);//存在则入队
            } else
                root.child[i] = root;
        }
        while (!q.isEmpty()) {
            Node cur = q.poll();
            for (int i = 0; i < 26; i++) {
                if (cur.child[i] != null) {//存在则该节点的fail指向父节点的fail指向节点的对应字符节点
                    cur.child[i].fail = cur.fail.child[i];
                    q.add(cur.child[i]);
                } else//当前节点不存在则指向父节点的fail指向节点的对应节点
                    cur.child[i] = cur.fail.child[i];
            }
        }
    }
}
