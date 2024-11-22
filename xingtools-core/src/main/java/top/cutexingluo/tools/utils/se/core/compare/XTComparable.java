package top.cutexingluo.tools.utils.se.core.compare;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.base.IData;

import java.util.Comparator;
import java.util.Objects;

/**
 * Comparable 封装类
 *
 * <p>实现 Comparable , 封装数据</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/19 17:38
 * @since 1.1.6
 */
public class XTComparable<T> implements Comparable<T>, IData<T> {
    /**
     * 比较器
     */
    protected Comparator<T> comparator;


    /**
     * 数据
     */
    protected T data;


    public XTComparable(@NotNull Comparator<T> comparator, T data) {
        Objects.requireNonNull(comparator);
        this.comparator = comparator;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Comparator<T> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compareTo(@NotNull T o) {
        return comparator.compare(data, o);
    }

    @Override
    public T data() {
        return data;
    }
}
