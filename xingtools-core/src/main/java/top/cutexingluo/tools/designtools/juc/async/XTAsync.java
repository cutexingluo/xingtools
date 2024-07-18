package top.cutexingluo.tools.designtools.juc.async;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.basepackage.baseimpl.XTCallable;
import top.cutexingluo.tools.basepackage.baseimpl.XTRunCallUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * XTAsync.run()<br>
 * 异步工具类
 * <br>继承自 CompletableFuture
 *
 * <p>推荐使用对象 {@link XTCompletionService}</p>
 * <p>或者直接实现接口 {@link top.cutexingluo.tools.designtools.helper.ThreadHelper}</p>
 *
 * @author XingTian
 * @date 2023/2/2 18:39
 */

public class XTAsync<T> extends CompletableFuture<T> {

    @Override
    public CompletableFuture<T> toCompletableFuture() {
        return this;
    }

    //**********常用四大件
    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable);
    }

    @NotNull
    public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor) {
        return CompletableFuture.runAsync(runnable, executor);
    }

    /**
     * check 版本
     *
     * @since 1.0.5
     */
    @NotNull
    public static CompletableFuture<Void> runAsyncCheck(Runnable runnable, Executor executor) {
        if (executor == null) return CompletableFuture.runAsync(runnable);
        return CompletableFuture.runAsync(runnable, executor);
    }

    //回调使用
    //whenComplete((t,u)->{}).exceptionally((e)->{}).get()
    //whenApply, whenRun, whenComplete
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier);
    }

    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier, Executor executor) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    /**
     * check 版本
     *
     * @since 1.0.5
     */
    @NotNull
    public static <T> CompletableFuture<T> supplyAsyncCheck(Supplier<T> supplier, Executor executor) {
        if (executor == null) return CompletableFuture.supplyAsync(supplier);
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    /**
     * 常用异步调用方法
     * <p>1.0.2 版本 添加可空</p>
     *
     * @since 1.0.2
     */
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> logic, @Nullable  Function<Throwable, T> exceptionHandle, @Nullable Executor executor) {
        CompletableFuture<T> future = null;
        if (executor == null) future = supplyAsync(logic);
        else future = supplyAsync(logic, executor);
        if (exceptionHandle != null) return future.exceptionally(exceptionHandle);
        return future;
    }

    /**
     * 创建单个CompletableFuture任务
     *
     * @param logic           任务逻辑
     * @param exceptionHandle 异常处理
     * @param <T>             类型
     * @return 任务
     */
    public static <T> CompletableFuture<T> createFuture(Supplier<T> logic, Function<Throwable, T> exceptionHandle, Executor executor) {
        return supplyAsync(logic, exceptionHandle, executor);
    }

    // js   then
    public CompletableFuture<T> then(BiConsumer<? super T, ? super Throwable> action) {
        return whenComplete(action);
    }

    // 异常默认返回
    public CompletableFuture<T> errorReturn(Function<Throwable, ? extends T> errorReturnDefault) {
        return exceptionally(errorReturnDefault);
    }

    // catch
    public CompletableFuture<T> jsCatch(Function<Throwable, ? extends T> errorReturnDefault) {
        return exceptionally(errorReturnDefault);
    }

    // handler res有 err有自己处理
    // allOf anyOf
    // thenAccept

    //-------------

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FutureResult<T> {
        private CompletableFuture<T> future;
        private List<Object> resultList;
    }

    /**
     * 创建并行任务并执行
     *
     * @param list            数据源
     * @param api             API调用逻辑
     * @param exceptionHandle 异常处理逻辑
     * @param <S>             数据源类型
     * @param <T>             程序返回类型
     * @return 处理结果列表
     */
    public static <S, T> List<T> parallelFutureJoin(Collection<S> list, Function<S, T> api, BiFunction<Throwable, S, T> exceptionHandle, Executor executor) {
        //规整所有任务
        List<CompletableFuture<T>> collectFuture = list.stream()
                .map(s -> createFuture(() -> api.apply(s), e -> exceptionHandle.apply(e, s), executor)).collect(Collectors.toList());
        //汇总所有任务，并执行join，全部执行完成后，统一返回
        return collectFuture.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 创建并行任务并执行
     *
     * @param list            数据源
     * @param exceptionHandle 异常处理逻辑
     * @return 处理结果列表
     */
    public static <T> List<T> callParallelFutureJoin(Collection<Callable<T>> list, BiFunction<Throwable, Callable<T>, T> exceptionHandle, Executor executor) {
        //规整所有任务
        List<CompletableFuture<T>> collectFuture = list.stream()
                .map(s -> createFuture(XTCallable.getTrySupplier(s),
                        e -> exceptionHandle.apply(e, s), executor))
                .collect(Collectors.toList());
        //汇总所有任务，并执行join，全部执行完成后，统一返回
        return collectFuture.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 创建并行任务
     *
     * @param list            数据源
     * @param exceptionHandle 异常处理逻辑
     * @return 处理结果列表
     */
    public static <T> List<CompletableFuture<T>> getParallelFutureJoin(Collection<Callable<T>> list, BiFunction<Throwable, Callable<T>, T> exceptionHandle, Executor executor) {
        //规整所有任务
        List<CompletableFuture<T>> collectFuture = list.stream()
                .map(s -> createFuture(XTCallable.getTrySupplier(s),
                        e -> exceptionHandle.apply(e, s), executor))
                .collect(Collectors.toList());
        //汇总所有任务，并执行join，全部执行完成后，统一返回
        return collectFuture;
    }

    /**
     * 创建并行任务并执行
     *
     * @param list            数据源
     * @param exceptionHandle 异常处理逻辑   第二个参数- 数据源类型，可能为 Callable, 也可能为最终 CompletableFuture
     * @return 是否全部执行成功
     */
    public static <S, T> boolean callParallelFutureAllOf(Collection<Callable<T>> list, BiFunction<Throwable, S, T> exceptionHandle, Executor executor) {
        //规整所有任务
        CompletableFuture<Void> future = getParallelFutureAllOf(list, exceptionHandle, executor);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            exceptionHandle.apply(e, (S) future);
            return false;
        }
        return true;
    }

    /**
     * 创建并行任务
     *
     * @param list            数据源
     * @param exceptionHandle 异常处理逻辑   第二个参数- 数据源类型，可能为 Callable, 也可能为最终 CompletableFuture
     * @return 是否全部执行成功
     */
    public static <S, T> CompletableFuture<Void> getParallelFutureAllOf(Collection<Callable<T>> list, BiFunction<Throwable, S, T> exceptionHandle, Executor executor) {
        //规整所有任务
        return CompletableFuture.allOf(list.stream()
                .map(s -> createFuture(XTCallable.getTrySupplier(s),
                        e -> exceptionHandle.apply(e, (S) s), executor)).toArray(CompletableFuture[]::new));
    }

    /**
     * 创建串行任务
     * <p>调用 get() 执行</p>
     *
     * @param list            数据源
     * @param exceptionHandle 异常处理逻辑
     * @return 处理结果列表
     */
    public static <T> CompletableFuture<T> getSerialFutureJoin(Collection<Callable<T>> list, Function<Throwable, T> exceptionHandle, Executor executor) {
        CompletableFuture<T> future = CompletableFuture.completedFuture(null);
        for (Callable<T> callable : list) {
            future = future.thenComposeAsync(result -> CompletableFuture.supplyAsync(
                    XTRunCallUtil.getTryCallable(callable, null).getCatchSupplier(exceptionHandle::apply)
                    , executor));
        }
        return future;
    }


    /**
     * 创建串行任务，获得所有结果
     * <p>调用 get() 执行</p>
     *
     * @param list            数据源
     * @param exceptionHandle 异常处理逻辑
     * @return 处理结果列表
     */
    public static FutureResult<Object> serialFutureJoin(Collection<Callable<Object>> list, Function<Throwable, Object> exceptionHandle, Executor executor) {
        CompletableFuture<Object> future = CompletableFuture.completedFuture(null);
        List<Object> res = new ArrayList<>();
        for (Callable<Object> callable : list) {
            future = future.thenComposeAsync(result -> {
                        res.add(result);
                        return CompletableFuture.supplyAsync(
                                XTRunCallUtil.getTryCallable(callable, null).getCatchSupplier(exceptionHandle::apply), executor);
                    }
            );
        }
        return new FutureResult<>(future, res);
    }

    /**
     * 创建串行任务并执行，获得所有结果
     *
     * @param list            数据源
     * @param exceptionHandle 异常处理逻辑
     * @return 处理结果列表
     */
    public static List<Object> callSerialFutureJoin(Collection<Callable<Object>> list, Function<Throwable, Object> exceptionHandle, Executor executor) throws Exception {
        FutureResult<Object> futureResult = serialFutureJoin(list, exceptionHandle, executor);
        Object o = futureResult.getFuture().get();
        futureResult.getResultList().add(o);
        return futureResult.getResultList();
    }


}

