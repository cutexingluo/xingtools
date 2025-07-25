package top.cutexingluo.tools.common.opt;

import lombok.Data;
import top.cutexingluo.tools.common.data.Entry;
import top.cutexingluo.tools.common.data.PairEntry;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 任务节点
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/7/4 14:46
 * @since 1.1.7
 */
@Data
public class OptChainNode<T> implements IOptRunner<T, T> {

    /**
     * 条件
     */
    private boolean condition;

    /**
     * 任务
     */
    private Function<T, T> task;

    public OptChainNode() {
        this(true, Function.identity());
    }

    public OptChainNode(boolean condition) {
        this(condition, Function.identity());
    }

    public OptChainNode(Function<T, T> task) {
        this(true, task);
    }

    public OptChainNode(boolean condition, Function<T, T> task) {
        this.condition = condition;
        this.task = task;
    }

    public static <T> OptChainNode<T> from(IOptRunner<T, T> other) {
        Objects.requireNonNull(other);
        return new OptChainNode<>(other::runTask);
    }

    public static <T> OptChainNode<T> from(boolean condition, IOptRunner<T, T> other) {
        Objects.requireNonNull(other);
        return new OptChainNode<>(condition, other::runTask);
    }


    @Override
    public Entry<Boolean, T> execute(T t) {
        boolean executed = false;
        T ret = t;
        if (condition) {
            if (task != null) {
                executed = true;
                ret = task.apply(t);
            }
        }
        return new Entry<>(executed, ret);
    }

    @Override
    public OptChainNode<T> with(IOptRunner<T, T> runner) {
        return OptChainNode.from(condition, IOptRunner.super.with(runner));
    }

    @Override
    public OptChainNode<T> withTask(Function<T, T> runner) {
        return OptChainNode.from(condition, IOptRunner.super.withTask(runner));
    }

    @Override
    public OptChainNode<T> check(Predicate<PairEntry<Boolean, T>> predicate) {
        return OptChainNode.from(condition, IOptRunner.super.check(predicate));
    }

    @Override
    public OptChainNode<T> peek(Consumer<T> consumer) {
        return OptChainNode.from(condition, IOptRunner.super.peek(consumer));
    }

    @Override
    public OptChainNode<T> passPeek(Consumer<T> passConsumer) {
        return OptChainNode.from(condition, IOptRunner.super.passPeek(passConsumer));
    }

    @Override
    public OptChainNode<T> orPeek(Consumer<T> orConsumer) {
        return OptChainNode.from(condition, IOptRunner.super.orPeek(orConsumer));
    }

}
