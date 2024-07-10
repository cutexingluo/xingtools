package top.cutexingluo.tools.utils.se.algo.cpp.geometry;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import static top.cutexingluo.tools.utils.se.algo.cpp.math.XTMath.sgn;

/**
 * 直线 , 线 ，线段
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/24 18:41
 * @since 1.0.3
 */

@Data
@AllArgsConstructor
public class Line implements Comparable<Line> {
    /**
     * 两点之一
     * <p>s - 开始 , t - 结束</p>
     */
    private Point s, t;
    /**
     * 角度
     */
    double ang;

    public Line() {
        this(new Point(), new Point());
    }

    public Line(Point a, Point b) {
        s = a;
        t = b;
        MathVector sub = a.sub(b);
        ang = Math.atan2(sub.getY(), sub.getX());
    }

    /**
     * 获取直线向量
     */
    public MathVector lineVector() {
        return t.sub(s);
    }

    @Override
    public int compareTo(@NotNull Line o) {
        return sgn(ang - o.ang);
    }

    /**
     * 平行判定
     *
     * @return 是否平行
     */
    public boolean isParallel(Line b) {
        return sgn((t.sub(s)).cross(t.sub(s))) == 0;
    }

    /**
     * 判定是否有交点
     */
    protected boolean hasIntersectWith(Line b) {
        MathVector u = t.sub(s), v = b.s.sub(s), w = b.t.sub(s);
        return sgn(u.cross(v)) != sgn(u.cross(w));
    }

    /**
     * 判定是否有交点
     */
    public boolean hasIntersect(Line b) {
        return hasIntersectWith(b) && b.hasIntersectWith(this);
    }

    /**
     * 获取交点
     */
    public Point getIntersect(Line b) {
        if (!hasIntersect(b)) return null;
        MathVector l1 = t.sub(s), l2 = b.t.sub(b.s), x = s.sub(b.s);
        double t = l2.cross(x) / l1.cross(l2);
        return new Point(s.add(l1.mul(t)));
    }

    /**
     * 点线距离
     */
    public double distance(Point a) {//点线距离
        MathVector v1 = t.sub(s), v2 = a.sub(s);
        return Math.abs((v1.cross(v2)) / v1.len());
    }

    /**
     * 是否点在线上
     */
    public boolean isPointOnLine(Point a) {//点线距离
        return sgn(distance(a)) == 0;
    }

    /**
     * 是否点在线段上
     */
    public boolean isPointOnSeg(Point a) {//点在线段上
        MathVector v1 = a.sub(s), v2 = a.sub(t);
        return isPointOnLine(a) && (sgn(v1.mul(v2)) <= 0);
    }

    /**
     * 点是否在射线上
     */
    public boolean isPointOnHalfLine(Point a) {//点在射线上
        return isPointOnLine(a) && sgn((a.sub(s)).mul(t.sub(s))) >= 0;
    }

}
