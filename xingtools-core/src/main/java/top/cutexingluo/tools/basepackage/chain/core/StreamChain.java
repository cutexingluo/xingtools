package top.cutexingluo.tools.basepackage.chain.core;


import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.chain.base.StreamChainProcessor;
import top.cutexingluo.tools.common.data.Entry;
import top.cutexingluo.tools.designtools.builder.XTBuilder;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 流式链式处理器实体类
 *
 * <p>可以直接面向对象流式编程</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 9:37
 * @since 1.1.4
 */
public class StreamChain<T> extends XTBuilder<T> implements StreamChainProcessor<T> {


    public StreamChain(T source) {
        this.target = source;
    }


    @Override
    public <R> StreamChain<R> cast(@NotNull Class<R> clazz) {
        if (clazz.isInstance(target)) {
            return new StreamChain<>((R) target);
        }
        return new StreamChain<>(null);
    }

    @Override
    public <R> StreamChain<R> castThrow(@NotNull Class<R> clazz) {
        if (clazz.isInstance(target)) {
            return new StreamChain<>((R) target);
        }
        throw new ClassCastException("the target is not " + clazz.getName() + ", the target is " + (target != null ? target.getClass().getName() : "null"));
    }

    @Override
    public <R> StreamChain<R> castOrElse(@NotNull Class<R> clazz, Function<T, R> elseMapper) {
        Objects.requireNonNull(elseMapper);
        if (clazz.isInstance(target)) {
            return new StreamChain<>((R) target);
        }
        return new StreamChain<>(elseMapper.apply(target));
    }

    @Override
    public <R> StreamChain<R> map(Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        return new StreamChain<>(mapper.apply(target));
    }

    @Override
    public <R> StreamChain<R> filterMap(Predicate<? super T> condition, Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(condition);
        Objects.requireNonNull(mapper);
        if (condition.test(target)) {
            return new StreamChain<>(mapper.apply(target));
        } else {
            return new StreamChain<>(null);
        }
    }

    @Override
    public <R> Entry<StreamChain<T>, StreamChain<?>> pairMap(Predicate<? super T> condition, Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(condition);
        Objects.requireNonNull(mapper);
        if (condition.test(target)) {
            return new Entry<>(
                    this,
                    new StreamChain<>(mapper.apply(target))
            );
        } else {
            return new Entry<>(
                    this,
                    new StreamChain<>(target)
            );
        }
    }

    @Override
    public StreamChain<T> peek(Consumer<? super T> chain) {
        Objects.requireNonNull(chain);
        chain.accept(target);
        return this;
    }
}
