package top.cutexingluo.tools.utils.se.algo.cpp.geometry.convex;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.se.algo.cpp.geometry.Point;
import top.cutexingluo.tools.utils.se.array.XTArrayUtil;

import java.util.Arrays;
import java.util.Random;

import static top.cutexingluo.tools.utils.se.algo.cpp.math.XTMath.sgn;

/**
 * 凸包
 * <p>多边形类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/24 22:27
 * @since 1.0.3
 */
public class Convex implements Comparable<Convex> {
    /**
     * 点数量
     */
    private int n;
    /**
     * 栈顶
     */
    private int top, iop;
    /**
     * 原数组, create 后会排序且从1开始
     */
    private Point[] a;
    /**
     * 栈
     */
    private Point[] c;


    public Convex(Point[] points) {
        n = points.length;
        this.a = Arrays.copyOf(points, n); //复制一份
        Arrays.sort(a); //排序
        this.a = XTArrayUtil.movePos(a, 1, new Point[n + 1]); //右移从1开始
        top = iop = 0;
        c = new Point[n + 1];
        // ------- 初始化
        iop = 2;
        for (int i = 1; i <= n; i++) {
            push(a[i]);
        }
        iop = top + 1;
        for (int i = n; i >= 1; i--) {
            push(a[i]);
        }
    }

    public Point[] getA() {
        return a;
    }

    public Point[] getC() {
        return c;
    }


    @Override
    public int compareTo(@NotNull Convex o) {
        return 0;
    }

    protected int dis2(@NotNull Point x, @NotNull Point y) {
        return (int) x.distance2(y);
    }

    protected void push(@NotNull Point x) {//不要左边,求下凸包
        //sgn((x - c[top - 1]) ^ (c[top] - c[top - 1])) >= 0
        while (top >= iop && sgn((x.sub(c[top - 1])).cross(c[top].sub(c[top - 1]))) >= 0) {
            --top;
        }
        c[++top] = x;
    }

    /**
     * 旋转卡壳
     *
     * @return 凸包直径最大值
     */
    public int rotatingCalipers() {//旋转卡壳
        int j = 2, ans = 0;//从第二个开始
        for (int i = 1; i < top; ++i) {
            //while(((c[i+1]-c[i])^(c[j]-c[i]))<((c[i+1]-c[i])^(c[j+1]-c[i])))
            while (((c[i + 1].sub(c[i])).cross(c[j].sub(c[i]))) < ((c[i + 1].sub(c[i])).cross(c[j + 1].sub(c[i])))) {
                j = j % (top - 1) + 1;
            }
            ans = Math.max(ans, Math.max(dis2(c[i], c[j]), dis2(c[i + 1], c[j])));
        }
        return ans;
    }

    /**
     * 旋转卡壳-随机化
     *
     * @return 凸包直径最大值
     */
    public int workRandomRC() {//旋转卡壳-随机化
        int vn = -1;
        Random random = new Random(System.currentTimeMillis()); // 1.0.5
        for (int tm = 1; tm <= 200; tm++) {
            for (int p = random.nextInt() % n + 1, t = 1; t <= 5; t++) {
                int md = -1, mp = 0;
                for (int i = 1; i <= n; i++) {
                    int d = dis2(a[i], a[p]);
                    if (d > md) {
                        md = d;
                        mp = i;
                    }
                }
                vn = Math.max(vn, md);
                p = mp;
            }
        }
        return vn;
    }
}
