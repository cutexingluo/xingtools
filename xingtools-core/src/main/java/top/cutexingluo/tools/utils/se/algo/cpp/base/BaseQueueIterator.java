package top.cutexingluo.tools.utils.se.algo.cpp.base;

import lombok.Getter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 基础离线队列迭代器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/28 14:09
 * @since 1.0.3
 */
@Getter
public abstract class BaseQueueIterator<T> implements Iterator<T> {

    protected final int length;
    protected final Queue<T> queue;
    protected int current;


    public BaseQueueIterator(Queue<T> queue) {
        this(queue, -1);
    }

    public BaseQueueIterator(Queue<T> queue, int length) {
        this.queue = queue == null ? new LinkedList<>() : queue;
        this.length = length;
        this.current = 0;
    }


    @Override
    public boolean hasNext() {
        return length == -1 ? !this.queue.isEmpty() : current < length;
    }

    @Override
    public T next() {
        if (hasNext()) {
            current++;
            return this.queue.poll();
        }
        return null;
    }
}
