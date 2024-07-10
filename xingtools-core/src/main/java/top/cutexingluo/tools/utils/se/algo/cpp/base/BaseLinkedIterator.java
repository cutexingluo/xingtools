package top.cutexingluo.tools.utils.se.algo.cpp.base;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 基础双端队列 / 链表迭代器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/10 15:18
 * @since 1.0.3
 */
public abstract class BaseLinkedIterator<T> implements Iterator<T> {

    protected final int length;
    protected final LinkedList<T> linkedList;
    protected int current;

    public BaseLinkedIterator(LinkedList<T> linkedList) {
        this(linkedList, -1);
    }

    public BaseLinkedIterator() {
        this(null, -1);
    }

    public BaseLinkedIterator(LinkedList<T> linkedList, int length) {
        this.linkedList = linkedList == null ? new LinkedList<>() : linkedList;
        this.length = length;
        this.current = 0;
    }

    @Override
    public boolean hasNext() {
        return length == -1 ? !this.linkedList.isEmpty() : current < length;
    }

    @Override
    public T next() {
        if (hasNext()) {
            current++;
            return this.linkedList.poll();
        }
        return null;
    }
}
