package top.cutexingluo.tools.utils.se.core.iterator;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 基础在线迭代器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/13 18:15
 * @since 1.1.6
 */
public abstract class BaseNowIterator<C, T> implements Iterator<T> {

    protected final LinkedList<C> linkedList;

    public BaseNowIterator() {
        this(new LinkedList<>());
    }

    public BaseNowIterator(LinkedList<C> linkedList) {
        Objects.requireNonNull(linkedList);
        this.linkedList = linkedList;
    }

    public void consumer(@NotNull Consumer<LinkedList<C>> consumer) {
        consumer.accept(linkedList);
    }

    @Override
    public boolean hasNext() {
        return !linkedList.isEmpty();
    }


}
