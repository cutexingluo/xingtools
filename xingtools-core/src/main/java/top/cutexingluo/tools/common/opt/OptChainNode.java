package top.cutexingluo.tools.common.opt;

import lombok.Data;
import top.cutexingluo.tools.common.data.Entry;
import top.cutexingluo.tools.common.data.PairEntry;

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

    public OptChainNode(Function<T, T> task) {
        this(true, task);
    }

    public OptChainNode(boolean condition, Function<T, T> task) {
        this.condition = condition;
        this.task = task;
    }

    public static <T> OptChainNode<T> from(IOptRunner<T, T> other) {
        return new OptChainNode<>(other != null ? t -> other.execute(t).getValue() : null);
    }

    public static <T> OptChainNode<T> from(boolean condition, IOptRunner<T, T> other) {
        return new OptChainNode<>(condition, other != null ? t -> other.execute(t).getValue() : null);
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
    public OptChainNode<T> check(Predicate<PairEntry<Boolean, T>> predicate) {
        return OptChainNode.from(condition, IOptRunner.super.check(predicate));
    }
}
