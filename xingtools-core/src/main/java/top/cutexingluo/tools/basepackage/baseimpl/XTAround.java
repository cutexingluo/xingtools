package top.cutexingluo.tools.basepackage.baseimpl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Callable , Supplier , Runnable 基础静态方法封装
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/27 12:26
 * @see XTCallable
 * @see XTSupplier
 * @since 1.1.5
 */
public class XTAround {

    //---------------------static------------------------

    //---------------------method------------------------


    //callable->callable

    /**
     * try-catch 包围 执行方法
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Callable<O> getTryCallable(Callable<O> task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return () -> getTrySupplierByCallable(task, canRunTask, inCatch).get();
    }

    /**
     * try-catch 包围 执行方法
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Callable<O> getTryCallable(Callable<O> task) {
        return getTryCallable(task, null, null);
    }


    /**
     * try-catch 包围 执行方法
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Callable<O> getTryRetCallable(Callable<O> task, Supplier<Boolean> canRunTask, Function<Exception, O> inCatch) {
        return () -> getTryRetSupplierByCallable(task, canRunTask, inCatch).get();
    }

    /**
     * try-catch 包围 执行方法
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Callable<O> getTryRetCallable(Callable<O> task) {
        return getTryRetCallable(task, null, null);
    }


    //supplier->supplier


    /**
     * try-catch 包围 执行方法
     * <p>fix bug</p>
     *
     * @since 1.0.4
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Supplier<O> getTrySupplier(Supplier<O> task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return () -> {
            if (canRunTask != null && !canRunTask.get()) {
                return null;
            }
            O res = null;
            try {
                if (task != null) res = task.get();
            } catch (Exception e) {
                if (inCatch != null) inCatch.accept(e);
                else throw e;
            }
            return res;
        };
    }

    /**
     * try-catch 包围 执行方法
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Supplier<O> getTrySupplier(Supplier<O> task) {
        return getTrySupplier(task, null, null);
    }

    /**
     * try-catch 包围 执行方法
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Supplier<O> getTryRetSupplier(Supplier<O> task, Supplier<Boolean> canRunTask, Function<Exception, O> inCatch) {
        return () -> {
            if (canRunTask != null && !canRunTask.get()) {
                return null;
            }
            O res = null;
            try {
                if (task != null) res = task.get();
            } catch (Exception e) {
                if (inCatch != null) return inCatch.apply(e);
                else throw e;
            }
            return res;
        };
    }

    /**
     * try-catch 包围 执行方法
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Supplier<O> getTryRetSupplier(Supplier<O> task) {
        return getTryRetSupplier(task, null, null);
    }


    //callable->supplier

    /**
     * try-catch 包围 执行方法
     *
     * <p>于 1.1.5  将打印错误去除</p>
     *
     * @param task       任务
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Supplier<O> getTrySupplierByCallable(Callable<O> task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return () -> {
            if (canRunTask != null && !canRunTask.get()) {
                return null;
            }
            O res = null;
            try {
                if (task != null) res = task.call();
            } catch (Exception e) {
                if (inCatch != null) inCatch.accept(e);
            }
            return res;
        };
    }

    /**
     * try-catch 包围 执行方法
     *
     * <p>不推荐使用该方法, 没有异常捕获, 异常将被 忽略 </p>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Supplier<O> getTrySupplierByCallable(Callable<O> task) {
        return getTrySupplierByCallable(task, null, null);
    }

    /**
     * try-catch 包围 执行方法
     *
     * @since 1.1.5
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Supplier<O> getTrySupplierByCallable(Callable<O> task, Consumer<Exception> inCatch) {
        return getTrySupplierByCallable(task, null, inCatch);
    }

    /**
     * try-catch 包围 执行方法
     *
     * <p>于 1.1.5  将打印错误去除</p>
     *
     * @param task       任务
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Supplier<O> getTryRetSupplierByCallable(Callable<O> task, Supplier<Boolean> canRunTask, Function<Exception, O> inCatch) {
        return () -> {
            if (canRunTask != null && !canRunTask.get()) {
                return null;
            }
            O res = null;
            try {
                if (task != null) res = task.call();
            } catch (Exception e) {
                if (inCatch != null) return inCatch.apply(e);
            }
            return res;
        };
    }

    /**
     * try-catch 包围 执行方法
     *
     * <p>不推荐使用该方法, 没有异常捕获, 异常将被 忽略 </p>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Supplier<O> getTryRetSupplierByCallable(Callable<O> task) {
        return getTryRetSupplierByCallable(task, null, null);
    }


    //supplier->callable

    /**
     * try-catch 包围 执行方法
     *
     * @param task       任务
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Callable<O> getTryCallableBySupplier(Supplier<O> task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return () -> {
            if (canRunTask != null && !canRunTask.get()) {
                return null;
            }
            O res = null;
            try {
                if (task != null) res = task.get();
            } catch (Exception e) {
                if (inCatch != null) inCatch.accept(e);
                else throw e;
            }
            return res;
        };
    }

    /**
     * try-catch 包围 执行方法
     *
     * <p>不推荐使用该方法, 没有异常捕获, 异常将被 重新抛出 </p>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Callable<O> getTryCallableBySupplier(Supplier<O> task) {
        return getTryCallableBySupplier(task, null, null);
    }

    /**
     * try-catch 包围 执行方法
     *
     * @since 1.1.5
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Callable<O> getTryCallableBySupplier(Supplier<O> task, Consumer<Exception> inCatch) {
        return getTryCallableBySupplier(task, null, inCatch);
    }

    /**
     * try-catch 包围 执行方法
     *
     * @param task       任务
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Callable<O> getTryRetCallableBySupplier(Supplier<O> task, Supplier<Boolean> canRunTask, Function<Exception, O> inCatch) {
        return () -> {
            if (canRunTask != null && !canRunTask.get()) {
                return null;
            }
            O res = null;
            try {
                if (task != null) res = task.get();
            } catch (Exception e) {
                if (inCatch != null) return inCatch.apply(e);
                else throw e;
            }
            return res;
        };
    }

    /**
     * try-catch 包围 执行方法
     *
     * <p>不推荐使用该方法, 没有异常捕获, 异常将被 重新抛出 </p>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Callable<O> getTryRetCallableBySupplier(Supplier<O> task) {
        return getTryRetCallableBySupplier(task, null, null);
    }


    //runnable

    /**
     * 获取Runnable
     *
     * @param task       任务
     * @param canRunTask 是否可以运行任务
     * @param inCatch    异常捕获
     * @return {@link Runnable}
     */
    @NotNull
    @Contract(pure = true)
    public static Runnable getTryRunnable(Runnable task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return () -> {
            if (canRunTask != null && !canRunTask.get()) {
                return;
            }
            try {
                if (task != null) task.run();
            } catch (Exception e) {
                if (inCatch != null) inCatch.accept(e);
                else throw e;
            }
        };
    }
}
