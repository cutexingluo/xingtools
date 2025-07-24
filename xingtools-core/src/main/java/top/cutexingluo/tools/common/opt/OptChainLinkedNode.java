package top.cutexingluo.tools.common.opt;


import lombok.Data;
import top.cutexingluo.tools.common.data.Entry;
import top.cutexingluo.tools.common.data.PairEntry;

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

    public OptChainLinkedNode(Function<T, R> task) {
        this(true, task);
    }

    public OptChainLinkedNode(boolean condition, Function<T, R> task) {
        this.condition = condition;
        this.task = task;
    }

    public static <T, R> OptChainLinkedNode<T, R> from(IOptRunner<T, R> other) {
        return new OptChainLinkedNode<>(other != null ? t -> other.execute(t).getValue() : null);
    }

    public static <T, R> OptChainLinkedNode<T, R> from(boolean condition, IOptRunner<T, R> other) {
        return new OptChainLinkedNode<>(condition, other != null ? t -> other.execute(t).getValue() : null);
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
        return OptChainLinkedNode.from(condition, IOptRunner.super.with(runner));
    }

    @Override
    public OptChainLinkedNode<T, R> check(Predicate<PairEntry<Boolean, R>> predicate) {
        return OptChainLinkedNode.from(condition, IOptRunner.super.check(predicate));
    }
}
