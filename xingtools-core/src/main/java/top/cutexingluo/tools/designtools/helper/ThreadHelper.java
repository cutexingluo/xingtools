package top.cutexingluo.tools.designtools.helper;

import top.cutexingluo.tools.basepackage.baseimpl.XTRunCallUtil;
import top.cutexingluo.tools.designtools.juc.async.XTAsync;
import top.cutexingluo.tools.designtools.juc.async.XTCompletionService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 多线程 helper 类
 * <p>1. 通过实现该接口即可使用里面的方法</p>
 * <p>2.一些简单实现。其他自行使用 {@link  XTCompletionService} 和 {@link  XTAsync}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/17 16:05
 * @updateFrom 1.0.4
 * @since 1.0.2
 */
@FunctionalInterface
public interface ThreadHelper {
    /**
     * 配置线程池
     *
     * <p>1.0.4  为避免冲突 改名为 executor </p>
     *
     * @return 需要返回线程池
     */
    Executor executor();

    //---------------------XTCompletionService------------------------

    default XTCompletionService<Object> newCompletionService() {
        return new XTCompletionService<>(executor());
    }


    /**
     * 提交任务
     */
    default <V> Future<V> submit(XTCompletionService<V> completionService, Runnable task, V result) {
        return completionService.submit(task, result);
    }

    /**
     * 提交任务
     */
    default <V> ArrayList<Future<V>> submit(XTCompletionService<V> completionService, List<Runnable> tasks, List<V> results) {
        return completionService.submitAll(tasks, results);
    }

    /**
     * 提交任务
     */
    default <V> Future<V> submit(XTCompletionService<V> completionService, Callable<V> task) {
        return completionService.submit(task);
    }

    /**
     * 提交所有任务
     */
    default <V> ArrayList<Future<V>> submit(XTCompletionService<V> completionService, List<Callable<V>> tasks) {
        return completionService.submitAll(tasks);
    }

    //---------------------XTAsync------------------------


    /**
     * 执行任务
     */
    default CompletableFuture<Void> runAsync(Runnable task) {
        return XTAsync.runAsync(task, executor());
    }

    /**
     * 执行任务
     */
    default <V> CompletableFuture<V> supplyAsync(Callable<V> task) {
        return XTAsync.supplyAsync(XTRunCallUtil.getTrySupplier(task), executor());
    }

    /**
     * 执行任务
     */
    default <V> CompletableFuture<V> supplyAsync(Callable<V> task, Function<Throwable, V> exceptionHandle) {
        return XTAsync.supplyAsync(XTRunCallUtil.getTrySupplier(task), exceptionHandle, executor());
    }

    /**
     * 执行系列任务
     *
     * @return CompletableFuture 列表
     */
    default <V> List<CompletableFuture<V>> getParallelFutureJoin(List<Callable<V>> tasks, BiFunction<Throwable, Callable<V>, V> exceptionHandle) {
        return XTAsync.getParallelFutureJoin(tasks, exceptionHandle, executor());
    }
}
