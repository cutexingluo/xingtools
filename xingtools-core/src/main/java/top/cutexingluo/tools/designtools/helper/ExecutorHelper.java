package top.cutexingluo.tools.designtools.helper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 多线程 helper 接口
 * <p>通过实现该接口即可使用里面的方法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/25 9:29
 * @since 1.1.5
 */
public interface ExecutorHelper {

    /**
     * 配置线程池
     *
     * <p>1.0.4  为避免冲突 改名为 executor </p>
     *
     * @return 需要返回线程池
     */
    Executor executor();

    //---------------------runTask------------------------

    /**
     * 执行任务
     */
    CompletableFuture<Void> runAsync(Runnable task);

    /**
     * 执行任务 (有返回值)
     */
    <V> CompletableFuture<V> supplyAsync(Supplier<V> task);

    /**
     * 执行任务(有返回值, 异常捕获)
     */
    <V> CompletableFuture<V> supplyAsync(Supplier<V> task, Function<Throwable, V> exceptionHandle);

    /**
     * 执行系列任务
     *
     * @return CompletableFuture 列表
     */
    <V> List<CompletableFuture<V>> getParallelFutureJoin(List<Supplier<V>> tasks, BiFunction<Throwable, Supplier<V>, V> exceptionHandle);


    //---------------------CompletionService------------------------

    default <V> ExecutorCompletionService<V> newCompletionService() {
        return new ExecutorCompletionService<>(executor());
    }


}
