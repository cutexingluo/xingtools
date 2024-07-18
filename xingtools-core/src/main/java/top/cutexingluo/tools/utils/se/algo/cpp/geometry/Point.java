package top.cutexingluo.tools.utils.se.algo.cpp.geometry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

/**
 * 点
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/24 18:41
 * @since 1.0.3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Point extends MathVector implements Comparable<Point> {
    private double x, y;

    public Point() {
        x = 0;
        y = 0;
    }

    public Point(MathVector mathVector) {
        x = mathVector.getX();
        y = mathVector.getY();
    }

    /**
     * 两点距离
     */
    public double distance(Point p) {
        return Math.sqrt(distance2(p));
    }

    /**
     * 两点距离的平方
     */
    public double distance2(Point p) {
        double dx = x - p.x, dy = y - p.y;
        return dx * dx + dy * dy;
    }

    @Override
    public int compareTo(@NotNull Point o) {
        return super.compareTo(o);
    }

    /**
     * 中点
     */
    public Point middle(Point b) {//中点
        return new Point(this.add(b).div(2));
    }
}
