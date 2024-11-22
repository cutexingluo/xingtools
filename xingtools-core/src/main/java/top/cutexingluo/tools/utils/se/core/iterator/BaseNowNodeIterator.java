package top.cutexingluo.tools.utils.se.core.iterator;

import java.util.LinkedList;

/**
 * 基础在线迭代器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/14 9:36
 * @since 1.1.6
 */
public class BaseNowNodeIterator<T> extends BaseNowIterator<T, T> {
    public BaseNowNodeIterator() {
    }

    public BaseNowNodeIterator(LinkedList<T> linkedList) {
        super(linkedList);
    }

    @Override
    public boolean hasNext() {
        return super.hasNext();
    }

    /**
     * 建议重写该方法, 然后为列表添加元素
     */
    @Override
    public T next() {
        return linkedList.removeFirst();
    }
}
