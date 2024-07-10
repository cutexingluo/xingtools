package top.cutexingluo.tools.basepackage.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * TriFunction 函数接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/13 12:21
 * @see java.util.function.BiFunction
 * @since 1.0.4
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);

    default <W> TriFunction<T, U, V, W> andThen(Function<? super R, ? extends W> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v) -> after.apply(apply(t, u, v));
    }
}
