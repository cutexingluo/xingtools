package top.cutexingluo.tools.designtools.helper;

import top.cutexingluo.tools.designtools.juc.async.XTAsync;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 多线程 helper 部分实现接口
 *
 * <p>对 {@link ExecutorHelper} 进行简单实现</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/26 14:28
 * @since 1.1.5
 */
@FunctionalInterface
public interface ThreadExecutorHelper extends ExecutorHelper {

    @Override
    Executor executor();

    //---------------------XTAsync------------------------


    @Override
    default CompletableFuture<Void> runAsync(Runnable task) {
        return XTAsync.runAsync(task, executor());
    }

    @Override
    default <V> CompletableFuture<V> supplyAsync(Supplier<V> task) {
        return XTAsync.supplyAsync(task, executor());
    }

    @Override
    default <V> CompletableFuture<V> supplyAsync(Supplier<V> task, Function<Throwable, V> exceptionHandle) {
        return XTAsync.supplyAsync(task, exceptionHandle, executor());
    }

    @Override
    default <V> List<CompletableFuture<V>> getParallelFutureJoin(List<Supplier<V>> tasks, BiFunction<Throwable, Supplier<V>, V> exceptionHandle) {
        return XTAsync.getParallelFutureJoin(tasks, exceptionHandle, executor());
    }
}
