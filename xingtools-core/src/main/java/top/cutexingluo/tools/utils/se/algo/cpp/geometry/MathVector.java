package top.cutexingluo.tools.utils.se.algo.cpp.geometry;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.designtools.method.ClassMaker;

import java.lang.reflect.Constructor;

import static top.cutexingluo.tools.utils.se.algo.cpp.math.XTMath.sgn;

/**
 * 向量
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/24 17:46
 * @since 1.0.3
 */
@Data
@AllArgsConstructor
public class MathVector {
    private double x, y;

    public MathVector() {
        x = 0;
        y = 0;
    }

    /**
     * 向量相加
     */
    //    Vector operator +(const Vector b)const{return Vector(x+b.x,y+b.y);}
    public MathVector add(final MathVector b) {
        return new MathVector(x + b.x, y + b.y);
    }

    /**
     * 向量相减
     */
    //    Vector operator -(const Vector b)const{return Vector(x-b.x,y-b.y);}
    public MathVector sub(final MathVector b) {
        return new MathVector(x - b.x, y - b.y);
    }
//    double operator *(const Vector b)const{return x*b.x+y*b.y;}

    /**
     * 向量相乘相加
     */
    public double mul(final MathVector b) {
        return x * b.x + y * b.y;
    }
//    double operator ^(const Vector b)const{return x*b.y-y*b.x;}//X

    /**
     * 向量叉乘 X
     */
    public double cross(final MathVector b) {
        return x * b.y - y * b.x;
    }
//    Vector operator *(const double b)const{return Vector(x*b,y*b);}

    /**
     * 向量点乘
     */
    public MathVector mul(final double b) {
        return new MathVector(x * b, y * b);
    }
//    Vector operator /(const double b)const{return Vector(x/b,y/b);}

    /**
     * 向量除法
     */
    // 向量除法
    public MathVector div(final double b) {
        return new MathVector(x / b, y / b);
    }
//    bool operator ==(const Vector b)const{return sgn(x-b.x)==0&&sgn(y-b.y)==0;}

    /**
     * 向量比较
     */
    // 向量除法
    public boolean equals(final MathVector b) {
        return compareTo(b) == 0;
    }


    public int compareTo(@NotNull MathVector o) {
        return compareTo(this, o);
    }

    public static int compareTo(MathVector a, MathVector b) {
        if (a == null) {
            return b == null ? 0 : 1;
        } else if (b == null) return -1;
        int sgn = sgn(a.x - b.x);
        if (sgn == 0) {
            return sgn(a.y - b.y);
        }
        return sgn;
    }

    /**
     * 比较位置
     */
    public boolean cmpPos(final MathVector b) {
        return sgn(x - b.x) == 0 ? sgn(y - b.y) < 0 : sgn(x - b.x) < 0;
    }

    /**
     * 比较角度
     */
    public boolean cmpAng(final MathVector b) {
        double anga = Math.atan2(y, x), angb = Math.atan2(b.y, b.x);
        return anga < angb;
    }
    //    bool operator !=(const Vector b)const{return !(*this==b);}
//    inline int read(){return scanf("%lf%lf",&x,&y);}

    /**
     * @return 长度的平方
     */
    public double len2() {
        return this.mul(this);
    }

    public double len() {
        return Math.sqrt(len2());
    }

    public static double len(MathVector a) {
        return a.len();
    }
    //---------------------其他方法---------------------

    /**
     * 夹角 (acos)
     */
    public double ang(MathVector b) {
        double v = this.mul(b) / this.len() / b.len();
        return Math.acos(v);
    }

    /**
     * 旋转
     */
    public MathVector trans(double the) {
        return new MathVector(x * Math.cos(the) - y * Math.sin(the), x * Math.sin(the) + y * Math.cos(the));
    }

    /**
     * 法线
     */
    public MathVector normal() {
        return new MathVector(-y / len(), x / len());
    }

    /**
     * 强制转化
     */
    public <T extends MathVector> T toTarget(Class<T> tClass) {
        Constructor<T> constructor = new ClassMaker<>(tClass).getConstructor(Double.class, Double.class);
        return ClassMaker.newInstance(constructor, x, y);
    }

}
