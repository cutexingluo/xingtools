package top.cutexingluo.tools.basepackage.chain.base;

import top.cutexingluo.tools.common.data.PairEntry;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * stream 流式 处理器 接口
 * <p>可以直接面向对象流式编程</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 10:40
 * @see java.util.stream.Stream
 * @since 1.1.4
 */
public interface StreamChainProcessor<T> {

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
     * <p>如果转化失败，则返回默认mapper 的返回值</p>
     */
    <R> StreamChainProcessor<R> castOrElse(Class<R> clazz, Function<T, R> elseMapper);

    /**
     * 映射操作
     * <p>将当前对象映射为目标对象</p>
     */
    <R> StreamChainProcessor<R> map(Function<? super T, ? extends R> mapper);

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
     * 提取操作
     * <p>使用当前对象值</p>
     */
    StreamChainProcessor<T> peek(Consumer<? super T> consumer);
}
