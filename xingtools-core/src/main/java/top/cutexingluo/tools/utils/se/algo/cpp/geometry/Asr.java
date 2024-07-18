package top.cutexingluo.tools.utils.se.algo.cpp.geometry;

/**
 * 自适应辛普森法
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/25 20:23
 * @since 1.0.3
 */
public class Asr {

    protected double f(double x) {
        return x * x;//inupt the function
    }

    public double integral(double l, double r) {
        return (r - l) / 6 * (f(l) + f(r) + f((l + r) / 2) * 4);
    }

    public double asr(double l, double r, double eps, double A) {
        double mid = (l + r) / 2;
        double le = integral(l, mid), ri = integral(mid, r);
        if (Math.abs(le + ri - A) <= 15 * eps) return le + ri - (le + ri - A) / 15;
        return asr(l, mid, eps / 2, le) + asr(mid, r, eps / 2, ri);
    }

    public double asr(double l, double r) {
        return asr(l, r, 1e-5, integral(l, r));
    }
}
