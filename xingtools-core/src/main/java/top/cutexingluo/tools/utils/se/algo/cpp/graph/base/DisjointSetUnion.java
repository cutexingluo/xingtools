package top.cutexingluo.tools.utils.se.algo.cpp.graph.base;

/**
 * 并查集
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/27 18:52
 * @since 1.0.3
 */
public interface DisjointSetUnion {

    /**
     * 路径压缩
     */
    default int find(int[] f, int tar) {
        while (tar != f[tar]) {
            tar = f[tar] = f[f[tar]];
        }
        return tar;
    }

    /**
     * 初始化
     *
     * @param f 数组
     * @param n 最大位置
     */
    default void arrayInit(int[] f, int n) {
        for (int i = 1; i <= n; i++) {
            f[i] = i;
        }
    }

    /**
     * 合并
     */
    default void unionMerge(int[] f, int x, int y) {
        int fx = find(f, x), fy = find(f, y);
        if (fx != fy) f[fy] = fx;
    }
}
