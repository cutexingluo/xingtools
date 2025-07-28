package top.cutexingluo.tools.common.opt;


import lombok.Data;
import top.cutexingluo.tools.common.data.Entry;
import top.cutexingluo.tools.common.data.PairEntry;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 链式任务节点
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/7/4 15:00
 * @since 1.1.7
 */
@Data
public class OptChainLinkedNode<T, R> implements IOptRunner<T, R> {

    /**
     * 条件
     */
    private boolean condition;

    /**
     * 任务
     */
    private Function<T, R> task;

    public OptChainLinkedNode() {
        this(true, null);
    }

    public OptChainLinkedNode(boolean condition) {
        this(condition, null);
    }

    public OptChainLinkedNode(Function<T, R> task) {
        this(true, task);
    }

    public OptChainLinkedNode(boolean condition, Function<T, R> task) {
        this.condition = condition;
        this.task = task;
    }

    public static <T, R> OptChainLinkedNode<T, R> from(IOptRunner<T, R> other) {
        return new OptChainLinkedNode<>(other != null ? other::runTask : null);
    }

    public static <T, R> OptChainLinkedNode<T, R> from(boolean condition, IOptRunner<T, R> other) {
        return new OptChainLinkedNode<>(condition, other != null ? other::runTask : null);
    }

    @Override
    public Entry<Boolean, R> execute(T t) {
        boolean executed = false;
        R ret = null;
        if (condition) {
            if (task != null) {
                executed = true;
                ret = task.apply(t);
            }
        }
        return new Entry<>(executed, ret);
    }

    @Override
    public OptChainLinkedNode<T, R> with(IOptRunner<R, R> runner) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.with(runner));
    }

    @Override
    public OptChainLinkedNode<T, R> with(boolean condition, IOptRunner<R, R> runner) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.with(condition, runner));
    }

    @Override
    public OptChainLinkedNode<T, R> with(boolean condition, Predicate<PairEntry<Boolean, R>> openPredicate, IOptRunner<R, R> runner) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.with(condition, openPredicate, runner));
    }

    @Override
    public OptChainLinkedNode<T, R> withTask(Function<R, R> runner) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.withTask(runner));
    }

    @Override
    public OptChainLinkedNode<T, R> withTask(boolean condition, Function<R, R> runner) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.withTask(condition, runner));
    }

    @Override
    public OptChainLinkedNode<T, R> withTask(boolean condition, Predicate<PairEntry<Boolean, R>> openPredicate, Function<R, R> runner) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.withTask(condition, openPredicate, runner));
    }

    @Override
    public <S> OptChainLinkedNode<T, S> then(IOptRunner<R, S> runner) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.then(runner));
    }

    @Override
    public <S> OptChainLinkedNode<T, S> then(boolean condition, IOptRunner<R, S> runner) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.then(condition, runner));
    }

    @Override
    public <S> OptChainLinkedNode<T, S> then(boolean condition, Predicate<PairEntry<Boolean, R>> openPredicate, IOptRunner<R, S> runner) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.then(condition, openPredicate, runner));
    }

    @Override
    public <S> OptChainLinkedNode<T, S> thenTask(Function<R, S> runner) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.thenTask(runner));
    }

    @Override
    public <S> OptChainLinkedNode<T, S> thenTask(boolean condition, Function<R, S> runner) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.thenTask(condition, runner));
    }

    @Override
    public <S> OptChainLinkedNode<T, S> thenTask(boolean condition, Predicate<PairEntry<Boolean, R>> openPredicate, Function<R, S> runner) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.thenTask(condition, openPredicate, runner));
    }

    @Override
    public OptChainLinkedNode<T, R> check(Predicate<PairEntry<Boolean, R>> predicate) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.check(predicate));
    }

    @Override
    public OptChainLinkedNode<T, R> peek(Consumer<R> consumer) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.peek(consumer));
    }

    @Override
    public OptChainLinkedNode<T, R> peek(boolean condition, Predicate<PairEntry<Boolean, R>> nullableOpenPredicate, Consumer<R> consumer) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.peek(condition, nullableOpenPredicate, consumer));
    }

    @Override
    public OptChainLinkedNode<T, R> passPeek(Consumer<R> passConsumer) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.passPeek(passConsumer));
    }

    @Override
    public OptChainLinkedNode<T, R> passPeek(boolean condition, Consumer<R> passConsumer) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.passPeek(condition, passConsumer));
    }

    @Override
    public OptChainLinkedNode<T, R> orPeek(Consumer<R> orConsumer) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.orPeek(orConsumer));
    }

    @Override
    public OptChainLinkedNode<T, R> orPeek(boolean condition, Consumer<R> orConsumer) {
        return OptChainLinkedNode.from(this.condition, IOptRunner.super.orPeek(condition, orConsumer));
    }
}
