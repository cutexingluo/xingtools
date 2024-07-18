package top.cutexingluo.tools.utils.se.algo.cpp.geometry.convex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.cutexingluo.tools.utils.se.algo.cpp.geometry.Point;
import top.cutexingluo.tools.utils.se.algo.cpp.geometry.Triangle;
import top.cutexingluo.tools.utils.se.array.XTArrayUtil;

import static top.cutexingluo.tools.utils.se.algo.cpp.math.XTMath.eps;
import static top.cutexingluo.tools.utils.se.algo.cpp.math.XTMath.sgn;

/**
 * 圆的凸包
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/25 13:43
 * @since 1.0.3
 */
public class ConvexCircle {
    /**
     * 点的数目
     */
    private int n;

    /**
     * 点集
     */
    Point[] a;


    public ConvexCircle(Point[] points) {
        this.n = points.length;
        this.a = XTArrayUtil.movePos(points, 1, new Point[n + 1]);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Circle {
        /**
         * 圆心
         */
        public Point center;
        /**
         * 半径
         */
        public double radius;
    }


    /**
     * 最小圆覆盖
     *
     * @return 最小圆
     */
    public Circle minCircleCover() {
//        poi o;double r=0;
        Point o = a[1];
        double r = 0;
        for (int i = 1; i <= n; i++) {
            if (a[i].sub(o).len() <= r + eps) continue;// a[i] - o
            o = a[1].middle(a[i]);   // 1 i  中点
            r = a[i].sub(o).len();  // a[i] - o
            for (int j = 1; j < i; j++) {
                if (a[j].sub(o).len() <= r + eps) continue; // a[j] - o
                o = a[i].middle(a[j]);  // i j 中点
                r = a[i].sub(o).len(); // a[i] - o
                for (int k = 1; k < j; k++) {
                    if (a[k].sub(o).len() <= r + eps) continue;// a[k] - o
                    o = new Triangle(a[i], a[j], a[k]).circumCenter(); // 外心
                    r = a[i].sub(o).len(); // a[i] - o
                }
            }
        }
        return new Circle(o, r);
    }

    protected int getIndex(int i, int m) {
        return i + m > n ? i + m - n : i + m;
    }


    /**
     * 最小圆覆盖的凸包
     * <p>解决方法1</p>
     *
     * @return 直径
     */
    public double diameter() {
        double ret = 0;
        for (int i = 1; i <= n; i++) {
            int l = 1, r = n - 1;
            double dis = 0;
            while (r - l > 2) {
                int m1 = l + (r - l) / 3, m2 = l + 2 * (r - l) / 3;
                if (a[getIndex(i, m1)].sub(a[i]).len() > a[getIndex(i, m2)].sub(a[i]).len()) {
                    r = m2;
                } else l = m1;
            }
            for (int k = l; k <= r; k++) {
                double d = a[getIndex(i, k)].sub(a[i]).len();
                if (sgn(d - dis) > 0) {
                    dis = d;
                }
            }
            ret = Math.max(ret, dis);
        }
        return ret;
    }

    /**
     * 两边叉乘
     */
    protected double cross(int i, int now) {
        return a[i + 1].sub(a[i]).cross(a[now].sub(a[i]));
    }

    /**
     * 最小圆覆盖的凸包
     * <p>解决方法2</p>
     *
     * @return 直径
     */
    public double diameter2() {
        int ans = 0, now = 2;
        for (int i = 1; i < n; i++) {
            while (true) {
//                double ar1=(a[i+1]-a[i])^(a[now]-a[i]);
                double ar1 = cross(i, now);
                double ar2 = cross(i, now + 1);
                if (sgn(ar2 - ar1) <= 0) break;
                ++now;
                if (now == n) now = 1;
            }
            ans = Math.max(ans, (int) (a[now].sub(a[i]).len2()));
        }
        return ans;
    }


}
