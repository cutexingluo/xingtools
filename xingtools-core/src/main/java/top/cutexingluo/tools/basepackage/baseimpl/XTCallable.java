package top.cutexingluo.tools.basepackage.baseimpl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.base.ComCallable;
import top.cutexingluo.tools.basepackage.base.ComSupplier;
import top.cutexingluo.tools.basepackage.basehandler.CallableHandler;
import top.cutexingluo.tools.basepackage.basehandler.SupplierHandler;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Supplier 和 Callable 综合类
 * <p>1. 可使用静态方法组装</p>
 * <p>2. 能够兼容Callable 和 Supplier </p>
 * <p>3. 三元整合类 </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 23:08
 * @updateFrom 1.0.4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class XTCallable<T> implements Callable<T>, Supplier<T>, ComCallable<T>, ComSupplier<T>, CallableHandler, SupplierHandler {
    Callable<T> now;
    Runnable before, after;

    public XTCallable(Callable<T> task) {
        this.now = task;
    }


    //---------------------static------------------------

    /**
     * try-catch 包围 执行方法
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Callable<O> getTryCallable(Callable<O> task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return () -> getTrySupplier(task, canRunTask, inCatch).get();
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
     *
     * @param task       任务
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     * @return {@link Supplier}<{@link O}>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> Supplier<O> getTrySupplier(Callable<O> task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return () -> {
            if (canRunTask != null && !canRunTask.get()) {
                return null;
            }
            O res = null;
            try {
                if (task != null) res = task.call();
            } catch (Exception e) {
                if (inCatch != null) inCatch.accept(e);
                else e.printStackTrace();
            }
            return res;
        };
    }

    @NotNull
    @Contract(pure = true)
    public static <O> Supplier<O> getTrySupplier(Callable<O> task) {
        return getTrySupplier(task, null, null);
    }


    //---------------------inner------------------------

    public Supplier<T> getSupplier() {
        return () -> {
            T res = null;
            if (before != null) before.run();
            try {
                if (now != null) res = now.call();
            } catch (Exception e) {
                e.printStackTrace(); // 直接输出
            } finally {
                if (after != null) after.run();
            }
            return res;
        };
    }

    /**
     * 组装
     *
     * @since 1.0.4
     */
    public Callable<T> getCallable() {
        return () -> {
            T res = null;
            if (before != null) before.run();
            try {
                if (now != null) res = now.call();
            } catch (Exception e) { //重新抛出
                throw e;
            } finally {
                if (after != null) after.run();
            }
            return res;
        };
    }

    public Supplier<T> getCatchSupplier(Consumer<Exception> catchError) {
        return () -> {
            T res = null;
            if (before != null) before.run();
            try {
                if (now != null) res = now.call();
            } catch (Exception e) {
                if (after != null) catchError.accept(e);
            }
            return res;
        };
    }

    public Callable<T> getCatchCallable(Consumer<Exception> catchError) {
        return () -> getCatchSupplier(catchError).get();
    }

    /**
     * try-catch 包围 执行方法
     *
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     * @since 1.0.4
     */
    public Supplier<T> getTrySupplier(Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return getTrySupplier(getCallable(), canRunTask, inCatch);
    }

    /**
     * try-catch 包围 执行方法
     *
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     * @since 1.0.4
     */
    public Callable<T> getTryCallable(Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return getTryCallable(getCallable(), canRunTask, inCatch);
    }


    //----------------override---------------

    @Override
    public Callable<T> getCallable(Callable<T> now, Runnable before, Runnable after) {
        this.now = now;
        this.before = before;
        this.after = after;
        return getCallable();
    }

    /**
     * 组装成supplier
     *
     * <p>fix bug</p>
     *
     * @since 1.0.4
     */
    @Override
    public Supplier<T> getSupplier(Supplier<T> now, Runnable before, Runnable after) {
        this.now = now == null ? null : now::get;
        this.before = before;
        this.after = after;
        return getSupplier();
    }

    @Override
    public T call() throws Exception {
        return getCallable().call();
    }

    @Override
    public T get() {
        return getSupplier().get();
    }


    @Override
    public <O> Callable<O> decorate(Callable<O> task) {
        return getTryCallable(task);
    }


    @Override
    public <O> Supplier<O> decorate(Supplier<O> task) {
        return getTrySupplier(task);
    }
}
