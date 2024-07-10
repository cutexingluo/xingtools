package top.cutexingluo.tools.basepackage.function;

import java.util.Objects;

/**
 * TriConsumer 函数接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/13 12:26
 * @see java.util.function.BiConsumer
 * @since 1.0.4
 */
public interface TriConsumer<T, U, V> {
    void accept(T t, U u, V v);

    default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v) -> {
            accept(t, u, v);
            after.accept(t, u, v);
        };
    }
}
