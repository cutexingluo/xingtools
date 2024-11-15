package top.cutexingluo.tools.utils.se.core.iterable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

/**
 * Iterable 封装方法，提供更多操作
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/14 17:19
 * @since 1.1.6
 */
public class XTIterable<T> implements IterableExt<T> {

    protected final Iterable<T> iterable;

    public XTIterable(Iterable<T> iterable) {
        Objects.requireNonNull(iterable);
        this.iterable = iterable;
    }

    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> XTIterable<T> of(Iterable<T> iterable) {
        return new XTIterable<>(iterable);
    }


    @NotNull
    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }


}
