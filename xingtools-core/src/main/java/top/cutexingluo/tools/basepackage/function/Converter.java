package top.cutexingluo.tools.basepackage.function;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Converter interface for converting one object to another.
 * <p>from org.springframework.core.convert.converter</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/24 17:33
 * @since 1.1.2
 */
@FunctionalInterface
public interface Converter<S, T> {
    @Nullable
    T convert(S source);

    default <U> Converter<S, U> andThen(Converter<? super T, ? extends U> after) {
        Objects.requireNonNull(after, "After Converter must not be null");
        return (s) -> {
            T initialResult = this.convert(s);
            return initialResult != null ? after.convert(initialResult) : null;
        };
    }
}