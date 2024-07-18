package top.cutexingluo.tools.utils.se.algo.cpp.geometry;

/**
 * 三角形
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/25 13:09
 * @since 1.0.3
 */
public class Triangle {
    /**
     * 三点
     */
    private final Point[] a = new Point[3];

    public static final int MAX_EDGE = 3;

    /**
     * beginIndex开始的前三个点
     * <p>beginIndex + 3 必须小于b.length</p>
     */
    public Triangle(Point[] b, int beginIndex) {
        if (beginIndex + MAX_EDGE >= b.length) {
            throw new ArrayIndexOutOfBoundsException("beginIndex + 3 必须小于b.length");
        }
        this.a[0] = b[beginIndex];
        this.a[1] = b[beginIndex + 1];
        this.a[2] = b[beginIndex + 2];
    }

    /**
     * 前三个点
     * <p>b.length 必须大于等于3</p>
     */
    public Triangle(Point[] b) {
        this(b, 0);
    }

    public Triangle(Point a, Point b, Point c) {
        this.a[0] = a;
        this.a[1] = b;
        this.a[2] = c;
    }

    /**
     * 重心
     */
    public Point gravityCenter() {
        MathVector ret = new MathVector(0, 0);
        for (int i = 0; i < MAX_EDGE; i++) {
            ret = ret.add(a[i]);
        }
        return new Point(ret.div(3));
    }

    /**
     * 外心
     */
    public Point circumCenter() {
        // line aa(a[0],a[1]),b(a[1],a[2]);
        Line la = new Line(a[0], a[1]), lb = new Line(a[1], a[2]);
        // Vector v1=aa.t-aa.s,v2=b.t-b.s;
        MathVector v1 = la.lineVector(), v2 = lb.lineVector();
        v1 = v1.normal();
        v2 = v2.normal();
//        v1=normal(v1);v2=normal(v2);
        Point mida = a[0].middle(a[1]), midb = a[1].middle(a[2]); //中点
//        poi mida=(a[0]+a[1])/2,midb=(a[1]+a[2])/2;
//        aa=line(mida,mida+v1);b=line(midb,midb+v2);
        Point p1 = new Point(mida.add(v1)), p2 = new Point(midb.add(v2));
        Line aa = new Line(mida, p1), bb = new Line(midb, p2);
//        if(parallel(aa,b)){return poi(0,0);}
        if (aa.isParallel(bb)) {
            return new Point(0, 0);
        }
        return aa.getIntersect(bb);
//        return getll(aa,b);
    }


}
