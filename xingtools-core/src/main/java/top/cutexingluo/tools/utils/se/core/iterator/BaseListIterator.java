package top.cutexingluo.tools.utils.se.core.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 基础离线列表迭代器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/29 15:34
 * @since 1.0.3
 */
public abstract class BaseListIterator<T> implements Iterator<T> {

    protected final int length;
    protected final List<T> list;
    protected int current;

    public BaseListIterator(List<T> list) {
        this(list, -1);
    }

    public BaseListIterator(List<T> list, int length) {
        this.list = list == null ? new ArrayList<>() : list;
        this.length = length;
        this.current = 0;
    }

    @Override
    public boolean hasNext() {
        return length == -1 ? current < list.size() : current < length;
    }

    @Override
    public T next() {
        if (length == -1 || current < length) {
            if (current < list.size()) {
                return this.list.get(current++);
            }
        }
        return null;
    }
}
