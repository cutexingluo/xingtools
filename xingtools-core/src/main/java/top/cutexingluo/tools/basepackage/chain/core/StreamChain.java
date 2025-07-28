package top.cutexingluo.tools.basepackage.chain.core;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.chain.base.StreamChainProcessor;
import top.cutexingluo.tools.common.data.Entry;
import top.cutexingluo.tools.designtools.builder.AbstractBuilder;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 流式链式处理器实体类
 *
 * <p>可以直接面向对象流式编程</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 9:37
 * @see Optional
 * @since 1.1.4
 */
public class StreamChain<T> extends AbstractBuilder<T> implements StreamChainProcessor<T> {


    /**
     * Creates an instance with the given source.
     * <p>不推荐直接使用构造方法，但还是提供了出来</p>
     * <p>建议使用 of 和 ofNullable 静态方法</p>
     *
     * @param source 原数据
     */
    public StreamChain(T source) {
        this.target = source;
    }

    @Override
    public T getValue() {
        return target;
    }

    //---------static----------

    /**
     * Common instance for empty().
     * <p>空对象</p>
     */
    private static final StreamChain<?> EMPTY = new StreamChain<>(null);


    /**
     * Returns an empty Optional instance. No value is present for this Optional.
     * <p>返回一个空对象</p>
     */
    public static <T> StreamChain<T> empty() {
        @SuppressWarnings("unchecked")
        StreamChain<T> t = (StreamChain<T>) EMPTY;
        return t;
    }

    /**
     * Returns an Optional describing the given non-null value.
     * <p>返回一个非空对象</p>
     */
    @Contract("_ -> new")
    @NotNull
    public static <T> StreamChain<T> of(T value) {
        return new StreamChain<>(Objects.requireNonNull(value));
    }

    /**
     * Returns an Optional describing the given value, if non-null, otherwise returns an empty
     * <p>返回一个可空对象</p>
     */
    @SuppressWarnings("unchecked")
    public static <T> StreamChain<T> ofNullable(T value) {
        return value == null ? (StreamChain<T>) EMPTY
                : new StreamChain<>(value);
    }

    /**
     * <p>从其他实现类获取数据</p>
     */
    public static <T> StreamChain<T> ofOther(StreamChainProcessor<T> other) {
        Objects.requireNonNull(other);
        return other.isPresent() ? StreamChain.of(other.getValue())
                : empty();
    }

    /**
     * <p>从任意实现类获取数据 (包含当前对象)</p>
     */
    public static <T> StreamChain<T> ofInstance(StreamChainProcessor<T> instance) {
        Objects.requireNonNull(instance);
        if (instance instanceof StreamChain) {
            return (StreamChain<T>) instance;
        } else {
            return ofOther(instance);
        }
    }


    //---------override----------
    //-----optional-----

    @Override
    public T get() {
        if (target == null) {
            throw new NoSuchElementException("No target value present");
        }
        return target;
    }

    @Override
    public boolean isPresent() {
        return target != null;
    }

    @Override
    public boolean isEmpty() {
        return target == null;
    }

    @Override
    public void ifPresent(Consumer<? super T> action) {
        if (target != null) {
            action.accept(target);
        }
    }

    @Override
    public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (target != null) {
            action.accept(target);
        } else {
            emptyAction.run();
        }
    }

    @Override
    public StreamChain<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        } else {
            return predicate.test(target) ? this : empty();
        }
    }

    @Override
    public <R> StreamChain<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            return ofNullable(mapper.apply(target));
        }
    }

    @Override
    public <R> StreamChain<R> flatMap(Function<? super T, ? extends StreamChainProcessor<? extends R>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            @SuppressWarnings("unchecked")
            StreamChain<R> r = (StreamChain<R>) ofInstance(mapper.apply(target));
            return Objects.requireNonNull(r);
        }
    }

    @Override
    public StreamChain<T> or(Supplier<? extends StreamChainProcessor<? extends T>> supplier) {
        Objects.requireNonNull(supplier);
        if (isPresent()) {
            return this;
        } else {
            @SuppressWarnings("unchecked")
            StreamChain<T> r = (StreamChain<T>) ofInstance(supplier.get());
            return Objects.requireNonNull(r);
        }
    }

    @Override
    public Stream<T> stream() {
        if (!isPresent()) {
            return Stream.empty();
        } else {
            return Stream.of(target);
        }
    }

    @Override
    public T orElse(T other) {
        return target != null ? target : other;
    }

    @Override
    public T orElseGet(Supplier<? extends T> supplier) {
        return target != null ? target : supplier.get();
    }

    @Override
    public T orElseThrow() {
        if (target == null) {
            throw new NoSuchElementException("No target value present");
        }
        return target;
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (target != null) {
            return target;
        } else {
            throw exceptionSupplier.get();
        }
    }

    //-----self-----

    @SuppressWarnings("unchecked")
    @Override
    public <R> StreamChain<R> cast(@NotNull Class<R> clazz) {
        if (clazz.isInstance(target)) {
            return of((R) target);
        }
        return empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> StreamChain<R> castThrow(@NotNull Class<R> clazz) {
        if (clazz.isInstance(target)) {
            return of((R) target);
        }
        throw new ClassCastException("the target is not " + clazz.getName() + ", the target is " + (target != null ? target.getClass().getName() : "null"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R, X extends Throwable> StreamChain<R> castThrow(@NotNull Class<R> clazz, Supplier<? extends X> exceptionSupplier) throws X {
        if (clazz.isInstance(target)) {
            return of((R) target);
        }
        throw exceptionSupplier.get();
    }


    @SuppressWarnings("unchecked")
    @Override
    public <R> StreamChain<R> castOrElse(@NotNull Class<R> clazz, Function<T, R> elseMapper) {
        Objects.requireNonNull(elseMapper);
        if (clazz.isInstance(target)) {
            return of((R) target);
        }
        return ofNullable(elseMapper.apply(target));
    }

    @Override
    public StreamChain<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        action.accept(target);
        return this;
    }

    @Override
    public StreamChain<T> peekIfPresent(Consumer<? super T> action) {
        ifPresent(action);
        return this;
    }

    @Override
    public StreamChain<T> peekIfPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
        ifPresentOrElse(action, emptyAction);
        return this;
    }

    @Override
    public StreamChain<T> peekIfPresentOrEmpty(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        if (isEmpty()) {
            return empty();
        }
        action.accept(this.target);
        return this;
    }

    @SafeVarargs
    @Override
    public final StreamChain<T> peeksIfPresentOrEmpty(Consumer<? super T>... actions) {
        // 第三个参数 (opts, opt) -> null其实并不会执行到该函数式接口所以直接返回了个null
        return Stream.of(actions).reduce(this, StreamChain<T>::peekIfPresentOrEmpty, (opts, opt) -> null);
    }

    @Override
    public <R> StreamChain<R> directMap(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return ofNullable(mapper.apply(target));
    }


    @Override
    public <R> StreamChain<R> mapOrElse(Function<? super T, ? extends R> mapper, Runnable emptyAction) {
        Objects.requireNonNull(mapper);
        Objects.requireNonNull(emptyAction);
        if (isPresent()) {
            return ofNullable(mapper.apply(target));
        } else {
            emptyAction.run();
            return empty();
        }
    }

    @Override
    public <R> StreamChain<R> flattedMap(Function<? super T, ? extends Optional<? extends R>> mapper) {
        Objects.requireNonNull(mapper);
        if (isEmpty()) {
            return empty();
        } else {
            return ofNullable(mapper.apply(target).orElse(null));
        }
    }

    @Override
    public <R> StreamChain<R> filterMap(Predicate<? super T> condition, Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(condition);
        Objects.requireNonNull(mapper);
        if (condition.test(target)) {
            return ofNullable(mapper.apply(target));
        } else {
            return empty();
        }
    }

    @Override
    public <R> Entry<StreamChain<T>, StreamChain<?>> pairMap(Predicate<? super T> condition, Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(condition);
        Objects.requireNonNull(mapper);
        if (condition.test(target)) {
            return new Entry<>(
                    this,
                    ofNullable(mapper.apply(target))
            );
        } else {
            return new Entry<>(
                    this,
                    ofNullable(target)
            );
        }
    }


    //-----object-----


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof StreamChain<?>) {
            return Objects.equals(target, ((StreamChain<?>) obj).target);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(target);
    }

    @Override
    public String toString() {
        return target != null
                ? String.format("StreamChain[%s]", target)
                : "StreamChain.empty";
    }
}
