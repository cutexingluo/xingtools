package top.cutexingluo.tools.utils.se.algo.cpp.geometry;

import static top.cutexingluo.tools.utils.se.algo.cpp.math.XTMath.sgn;

/**
 * 几何工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/24 20:59
 * @since 1.0.3
 */
public class GeometryUtil {
    //-1 edge
//1 in
//0 out
    /**
     * 边缘
     */
    public static final int EDGE = -1;
    /**
     * 在里面
     */
    public static final int IN = 1;
    /**
     * 在外面
     */
    public static final int OUT = 0;

    /**
     * 从0开始，下一个
     */
    protected static int getNext(int index, int n) {
        return index == n - 1 ? 0 : index + 1;
    }

    /**
     * 多边形面积
     *
     * @param a 点集，从index=0开始
     */
    public static double polyArea(Point[] a) {
        int n = a.length;
        double ret = 0;
        for (int i = 0; i < n; i++) {
            ret += (a[i].cross(a[i == n - 1 ? 0 : i + 1]));
        }
        return Math.abs(ret / 2);
    }

    /**
     * 点形位置关系
     *
     * @param a 点集
     * @param p 点
     * @return -1 在边缘，1 在里面，0 在外面
     */
    public static int inPoly(Point[] a, Point p) {//点形关系
        int n = a.length;
        int wn = 0;
        for (int i = 0; i < n; i++) {
            if (new Line(a[i], a[getNext(i, n)]).isPointOnLine(p)) return EDGE;//点在线上
            int k = sgn((a[getNext(i, n)].sub(a[i])).cross(p.sub(a[i])));
            int d1 = sgn(a[i].getY() - p.getY());
            int d2 = sgn(a[getNext(i, n)].getY() - p.getY());
            if (k > 0 && d1 <= 0 && d2 > 0) ++wn;//向量左边
            if (k < 0 && d2 <= 0 && d1 > 0) --wn;//向量右边
        }
        if (wn != 0) return IN;
        return OUT;
    }


}
