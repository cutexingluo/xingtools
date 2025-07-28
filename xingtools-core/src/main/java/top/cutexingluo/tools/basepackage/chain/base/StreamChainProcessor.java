package top.cutexingluo.tools.basepackage.chain.base;

import top.cutexingluo.tools.common.base.IDataValue;
import top.cutexingluo.tools.common.data.PairEntry;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * stream 流式 处理器 接口
 * <p>可以直接面向对象流式编程</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 10:40
 * @see java.util.stream.Stream
 * @see java.util.Optional
 * @since 1.1.4
 */
public interface StreamChainProcessor<T> extends IDataValue<T>, ChainProcessor {

    // Optional

    /**
     * f a value is present, returns the value, otherwise throws NoSuchElementException.
     * <p>如果存在则返回，否则抛出异常</p>
     */
    T get();

    /**
     * If a value is present, returns true, otherwise false.
     * <p>目标是否不为空</p>
     */
    boolean isPresent();

    /**
     * If a value is not present, returns true, otherwise false.
     * <p>目标是否为空</p>
     */
    boolean isEmpty();

    /**
     * If a value is present, performs the given action with the value, otherwise does nothing.
     * <p>目标不为空时执行</p>
     */
    void ifPresent(Consumer<? super T> action);

    /**
     * If a value is present, performs the given action with the value, otherwise performs the given empty-based action.
     * <p>不为空时执行action，为空时执行emptyAction</p>
     *
     * @param action      the action to be performed, if a value is present
     * @param emptyAction the empty-based action to be performed, if no value is present
     */
    void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction);


    /**
     * If a value is present, and the value matches the given predicate, returns an Optional describing the value, otherwise returns an empty Optional.
     * <p>不为空时执行，若 predicate 返回值 为false 则返回空对象</p>
     */
    StreamChainProcessor<T> filter(Predicate<? super T> predicate);


    /**
     * If a value is present, returns a StreamChainProcessor describing (as if by ofNullable) the result of applying the given mapping function to the value, otherwise returns an empty StreamChainProcessor.
     *
     * <p>为空时返回空对象，不为空时执行mapper </p>
     * <p>检查的映射操作</p>
     * <p>将当前对象映射为目标对象或null value 对象</p>
     */
    <R> StreamChainProcessor<R> map(Function<? super T, ? extends R> mapper);


    /**
     * If a value is present, returns the result of applying the given Optional-bearing mapping function to the value, otherwise returns an empty StreamChainProcessor.
     * <p>为空时返回空对象，不为空时执行mapper ，并且检查非空 </p>
     */
    <R> StreamChainProcessor<R> flatMap(Function<? super T, ? extends StreamChainProcessor<? extends R>> mapper);


    /**
     * If a value is present, returns an Optional describing the value, otherwise returns an Optional produced by the supplying function.
     * <p>为空时进行替换, 返回新对象</p>
     */
    StreamChainProcessor<T> or(Supplier<? extends StreamChainProcessor<? extends T>> supplier);

    /**
     * If a value is present, returns a sequential Stream containing only that value, otherwise returns an empty Stream.
     * <p>获取stream</p>
     * <p>为空时返回空Stream, 不为空时返回Stream.of</p>
     */
    Stream<T> stream();

    /**
     * If a value is present, returns the value, otherwise returns other.
     * <p>为空时返回 other value</p>
     */
    T orElse(T other);


    /**
     * If a value is present, returns the value, otherwise returns the result produced by the supplying function.
     * <p>为空时返回 supplier 的值</p>
     */
    T orElseGet(Supplier<? extends T> supplier);

    /**
     * If a value is present, returns the value, otherwise throws NoSuchElementException.
     * <p>为空时抛出异常</p>
     */
    T orElseThrow();

    /**
     * If a value is present, returns the value, otherwise throws an exception produced by the exception supplying function.
     * <p>为空时抛出异常</p>
     */
    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    // Self

    /**
     * 转化类型
     * <p>将当前对象转化为指定类型</p>
     * <p>如果转化失败，则返回目标类型的空对象</p>
     */
    <R> StreamChainProcessor<R> cast(Class<R> clazz);

    /**
     * 转化类型
     * <p>将当前对象转化为指定类型</p>
     * <p>如果转化失败，则抛出异常</p>
     */
    <R> StreamChainProcessor<R> castThrow(Class<R> clazz);

    /**
     * 转化类型
     * <p>将当前对象转化为指定类型</p>
     * <p>如果转化失败，则抛出异常</p>
     */
    <R, X extends Throwable> StreamChainProcessor<R> castThrow(Class<R> clazz, Supplier<? extends X> exceptionSupplier) throws X;

    /**
     * 转化类型
     * <p>将当前对象转化为指定类型</p>
     * <p>如果转化失败，则返回默认mapper 的返回值</p>
     */
    <R> StreamChainProcessor<R> castOrElse(Class<R> clazz, Function<T, R> elseMapper);


    /**
     * 提取操作
     * <p>使用当前对象值</p>
     */
    StreamChainProcessor<T> peek(Consumer<? super T> action);


    /**
     * @return this
     * @see #ifPresent(Consumer)
     */
    StreamChainProcessor<T> peekIfPresent(Consumer<? super T> action);

    /**
     * @return this
     * @see #ifPresentOrElse(Consumer, Runnable)
     */
    StreamChainProcessor<T> peekIfPresentOrElse(Consumer<? super T> action, Runnable emptyAction);


    /**
     * 提取操作
     * <p>不为空则执行, 为空返回空对象</p>
     * <p>hutool 包的 Opt 扩展操作 peek</p>
     */
    StreamChainProcessor<T> peekIfPresentOrEmpty(Consumer<? super T> action);

    /**
     * 提取操作
     * <p>不为空则执行, 为空返回空对象</p>
     * <p>hutool 包的 Opt 扩展操作 peeks</p>
     */
    StreamChainProcessor<T> peeksIfPresentOrEmpty(Consumer<? super T>... actions);


    /**
     * 映射操作
     * <p>将当前对象直接映射为目标对象</p>
     */
    <R> StreamChainProcessor<R> directMap(Function<? super T, ? extends R> mapper);


    /**
     * 存在执行 mapper , 不存在执行 emptyAction
     *
     * @see #map(Function)
     * @see #peekIfPresentOrElse(Consumer, Runnable)
     */
    <R> StreamChainProcessor<R> mapOrElse(Function<? super T, ? extends R> mapper, Runnable emptyAction);


    /**
     * 如果存在则执行mapper ,  mapper 返回的是 Optional 对象
     *
     * @see #flatMap(Function)
     */
    <R> StreamChainProcessor<R> flattedMap(Function<? super T, ? extends Optional<? extends R>> mapper);

    /**
     * 过滤映射操作
     * <p>将当前对象映射为目标对象，如果条件不满足，则返回空对象</p>
     *
     * @param condition 条件, 满足条件返回 true, 如果结果为 true 则会执行 mapper 映射操作
     */
    <R> StreamChainProcessor<R> filterMap(Predicate<? super T> condition, Function<? super T, ? extends R> mapper);


    /**
     * 过滤映射操作
     * <p>将当前对象映射为目标对象，如果条件满足，返回原对象和映射对象二元组，如果条件不满足，则返回原对象和原对象二元组</p>
     *
     * @param condition 条件, 满足条件返回 true, 如果结果为 true 则会执行 mapper 映射操作
     * @return PairEntry 原对象和映射对象二元组 或 原对象和原对象二元组
     */
    <R> PairEntry<? extends StreamChainProcessor<T>, ? extends StreamChainProcessor<?>> pairMap(Predicate<? super T> condition, Function<? super T, ? extends R> mapper);


    /**
     * 将当前对象转化为 Optional 对象
     */
    default Optional<T> toOptional() {
        return Optional.ofNullable(getValue());
    }
}
