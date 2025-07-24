package top.cutexingluo.tools.common.opt;

import top.cutexingluo.tools.common.data.Entry;
import top.cutexingluo.tools.common.data.PairEntry;

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
     * 链式执行任务
     * <p>execute 成功执行后附带执行</p>
     *
     * @param runner 任务节点
     * @return IOptRunner<T, R> 组合后的链式任务节点
     */
    default IOptRunner<T, R> with(IOptRunner<R, R> runner) {
        return t -> {
            PairEntry<Boolean, R> entry = execute(t);
            if (entry.getKey()) {
                return runner.execute(entry.getValue());
            }
            return entry;
        };
    }

    /**
     * 判断条件, 返回是否继续执行任务
     *
     * @param predicate 结果判断条件
     * @return IOptRunner<T, R> 新条件的人物节点
     */
    default IOptRunner<T, R> check(Predicate<PairEntry<Boolean, R>> predicate) {
        return t -> {
            PairEntry<Boolean, R> entry = execute(t);
            return new Entry<>(predicate.test(entry), entry.getValue());
        };
    }
}
