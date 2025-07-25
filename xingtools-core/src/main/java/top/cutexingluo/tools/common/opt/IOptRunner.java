package top.cutexingluo.tools.common.opt;

import top.cutexingluo.tools.common.data.Entry;
import top.cutexingluo.tools.common.data.PairEntry;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 条件节点接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/7/4 14:51
 * @since 1.1.7
 */
@FunctionalInterface
public interface IOptRunner<T, R> {

    /**
     * 执行任务
     *
     * @param t 输入参数
     * @return Boolean 执行完成, R 返回结果
     */
    PairEntry<Boolean, R> execute(T t);

    /**
     * 直接执行任务
     *
     * @param t 输入参数
     * @return Boolean 成功执行完成
     */
    default boolean run(T t) {
        return execute(t).getKey();
    }

    /**
     * 直接执行任务
     *
     * @param t 输入参数
     * @return R 返回结果
     */
    default R runTask(T t) {
        return execute(t).getValue();
    }


    // with


    default IOptRunner<T, R> with(IOptRunner<R, R> runner) {
        return with(true, PairEntry::getKey, runner);
    }


    default IOptRunner<T, R> with(boolean condition, IOptRunner<R, R> runner) {
        return with(condition, PairEntry::getKey, runner);
    }

    /**
     * 链式执行任务
     * <p>execute 成功执行后附带执行</p>
     *
     * @param condition     条件
     * @param openPredicate 结果判断条件/启用条件
     * @param runner        任务节点
     * @return IOptRunner<T, R> 组合后的链式任务节点
     */
    default IOptRunner<T, R> with(boolean condition, Predicate<PairEntry<Boolean, R>> openPredicate, IOptRunner<R, R> runner) {
        Objects.requireNonNull(runner);
        return t -> {
            PairEntry<Boolean, R> entry = execute(t);
            if (condition && openPredicate.test(entry)) {
                return runner.execute(entry.getValue());
            }
            return entry;
        };
    }


    default IOptRunner<T, R> withTask(Function<R, R> runner) {
        return withTask(true, PairEntry::getKey, runner);
    }

    default IOptRunner<T, R> withTask(boolean condition, Function<R, R> runner) {
        return withTask(condition, PairEntry::getKey, runner);
    }

    /**
     * 链式执行任务
     * <p>execute 成功执行后附带执行</p>
     *
     * @param condition     条件
     * @param openPredicate 结果判断条件/启用条件
     * @param runner        任务节点
     * @return IOptRunner<T, R> 组合后的链式任务节点
     */
    default IOptRunner<T, R> withTask(boolean condition, Predicate<PairEntry<Boolean, R>> openPredicate, Function<R, R> runner) {
        Objects.requireNonNull(runner);
        Objects.requireNonNull(openPredicate);
        return t -> {
            PairEntry<Boolean, R> entry = execute(t);
            if (condition && openPredicate.test(entry)) {
                R ret = runner.apply(entry.getValue());
                return new Entry<>(true, ret);
            }
            return entry;
        };
    }

    // then


    default <S> IOptRunner<T, S> then(IOptRunner<R, S> runner) {
        return then(true, PairEntry::getKey, runner);
    }


    default <S> IOptRunner<T, S> then(boolean condition, IOptRunner<R, S> runner) {
        return then(condition, PairEntry::getKey, runner);
    }

    /**
     * 链式执行任务，进入下一阶段
     * <p>execute 成功执行后下阶段执行</p>
     *
     * @param condition     条件
     * @param openPredicate 结果判断条件/启用条件
     * @param runner        任务节点
     * @return IOptRunner<T, R> 组合后的链式任务节点
     */
    default <S> IOptRunner<T, S> then(boolean condition, Predicate<PairEntry<Boolean, R>> openPredicate, IOptRunner<R, S> runner) {
        Objects.requireNonNull(runner);
        Objects.requireNonNull(openPredicate);
        return t -> {
            PairEntry<Boolean, R> entry = execute(t);
            if (condition && openPredicate.test(entry)) {
                PairEntry<Boolean, S> execute = runner.execute(entry.getValue());
                return execute;
            }
            return new Entry<>(false, null);
        };
    }


    default <S> IOptRunner<T, S> thenTask(Function<R, S> runner) {
        return thenTask(true, PairEntry::getKey, runner);
    }

    default <S> IOptRunner<T, S> thenTask(boolean condition, Function<R, S> runner) {
        return thenTask(condition, PairEntry::getKey, runner);
    }

    /**
     * 链式执行任务，进入下一阶段
     * <p>execute 成功执行后下阶段执行</p>
     *
     * @param condition     条件
     * @param openPredicate 结果判断条件/启用条件
     * @param runner        任务节点
     * @return IOptRunner<T, R> 组合后的链式任务节点
     */
    default <S> IOptRunner<T, S> thenTask(boolean condition, Predicate<PairEntry<Boolean, R>> openPredicate, Function<R, S> runner) {
        Objects.requireNonNull(runner);
        return t -> {
            PairEntry<Boolean, R> entry = execute(t);
            if (condition && openPredicate.test(entry)) {
                S ret = runner.apply(entry.getValue());
                return new Entry<>(true, ret);
            }
            return new Entry<>(false, null);
        };
    }

    // check

    /**
     * 判断条件, 返回是否继续执行任务
     *
     * @param predicate 结果判断条件
     * @return IOptRunner<T, R> 新条件的任务节点
     */
    default IOptRunner<T, R> check(Predicate<PairEntry<Boolean, R>> predicate) {
        Objects.requireNonNull(predicate);
        return t -> {
            PairEntry<Boolean, R> entry = execute(t);
            return new Entry<>(predicate.test(entry), entry.getValue());
        };
    }

    // peek

    /**
     * 消费对象，取出操作
     *
     * @param consumer 消费对象
     * @return IOptRunner<T, R> 新条件的任务节点
     */
    default IOptRunner<T, R> peek(Consumer<R> consumer) {
        return peek(true, null, consumer);
    }

    /**
     * 消费对象，取出操作
     *
     * @param condition             条件
     * @param consumer              消费对象
     * @param nullableOpenPredicate 提取条件
     * @return IOptRunner<T, R> 新条件的任务节点
     */
    default IOptRunner<T, R> peek(boolean condition, Predicate<PairEntry<Boolean, R>> nullableOpenPredicate, Consumer<R> consumer) {
        Objects.requireNonNull(consumer);
        return t -> {
            PairEntry<Boolean, R> entry = execute(t);
            R ret = entry.getValue();
            if (condition) {
                if (nullableOpenPredicate == null || nullableOpenPredicate.test(entry)) consumer.accept(ret);
            }
            return new Entry<>(entry.getKey(), ret);
        };
    }

    /**
     * 返回 true 后消费对象，取出操作
     *
     * @param passConsumer 消费对象
     * @return IOptRunner<T, R> 新条件的任务节点
     */
    default IOptRunner<T, R> passPeek(Consumer<R> passConsumer) {
        return peek(true, PairEntry::getKey, passConsumer);
    }

    /**
     * 返回 true 后消费对象，取出操作
     *
     * @param condition    条件
     * @param passConsumer 消费对象
     * @return IOptRunner<T, R> 新条件的任务节点
     */
    default IOptRunner<T, R> passPeek(boolean condition, Consumer<R> passConsumer) {
        return peek(condition, PairEntry::getKey, passConsumer);
    }

    /**
     * 返回 false 后消费对象，取出操作
     *
     * @param orConsumer 消费对象
     * @return IOptRunner<T, R> 新条件的任务节点
     */
    default IOptRunner<T, R> orPeek(Consumer<R> orConsumer) {
        return peek(true, entry -> !entry.getKey(), orConsumer);
    }

    /**
     * 返回 false 后消费对象，取出操作
     *
     * @param condition  条件
     * @param orConsumer 消费对象
     * @return IOptRunner<T, R> 新条件的任务节点
     */
    default IOptRunner<T, R> orPeek(boolean condition, Consumer<R> orConsumer) {
        return peek(condition, entry -> !entry.getKey(), orConsumer);
    }
}
