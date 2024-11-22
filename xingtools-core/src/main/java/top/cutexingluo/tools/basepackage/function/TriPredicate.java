package top.cutexingluo.tools.basepackage.function;

import java.util.Objects;

/**
 * TriPredicate 函数接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/8 10:17
 * @see java.util.function.BiPredicate
 * @since 1.1.6
 */
@FunctionalInterface
public interface TriPredicate<T, U, V> {


    boolean test(T t, U u, V v);


    default TriPredicate<T, U, V> and(TriPredicate<? super T, ? super U, ? super V> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v) -> test(t, u, v) && other.test(t, u, v);
    }


    default TriPredicate<T, U, V> negate() {
        return (T t, U u, V v) -> !test(t, u, v);
    }


    default TriPredicate<T, U, V> or(TriPredicate<? super T, ? super U, ? super V> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v) -> test(t, u, v) || other.test(t, u, v);
    }
}
