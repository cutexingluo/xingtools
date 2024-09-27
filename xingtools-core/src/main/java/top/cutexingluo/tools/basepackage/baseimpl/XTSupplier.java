package top.cutexingluo.tools.basepackage.baseimpl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.base.ComCallable;
import top.cutexingluo.tools.basepackage.base.ComSupplier;
import top.cutexingluo.tools.basepackage.basehandler.CallableHandler;
import top.cutexingluo.tools.basepackage.basehandler.SupplierHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Supplier 和 Callable 综合类
 * <p>1. 可使用静态方法组装</p>
 * <p>2. 能够兼容Callable 和 Supplier </p>
 * <p>3. 三元整合类 </p>
 * <p>4.避免callable 转 supplier 的异常忽略问题</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/27 11:43
 * @see XTCallable
 * @since 1.1.5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class XTSupplier<T> extends XTAround implements Callable<T>, Supplier<T>, ComCallable<T>, ComSupplier<T>, CallableHandler, SupplierHandler {
    Supplier<T> now;
    Runnable before, after;

    public XTSupplier(Supplier<T> task) {
        this.now = task;
    }

    //---------------------static------------------------

    //---------------------constructor------------------------

    /**
     * of Callable 构造
     */
    @NotNull
    @Contract(pure = true)
    public static <O> XTSupplier<O> ofCallableInCatch(Callable<O> task, Consumer<Exception> inCatch) {
        return new XTSupplier<>(task != null ? () -> {
            try {
                return task.call();
            } catch (Exception e) {
                if (inCatch != null) inCatch.accept(e);
            }
            return null;
        } : null);
    }

    /**
     * of Callable 构造
     */
    @NotNull
    @Contract(pure = true)
    public static <O> XTSupplier<O> ofCallableInCatchRet(Callable<O> task, Function<Exception, O> inCatch) {
        return new XTSupplier<>(task != null ? () -> {
            try {
                return task.call();
            } catch (Exception e) {
                if (inCatch != null) return inCatch.apply(e);
            }
            return null;
        } : null);
    }

    /**
     * of Callable 构造
     *
     * <p>默认忽略异常</p>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> XTSupplier<O> ofCallable(Callable<O> task) {
        return ofCallableInCatch(task, null);
    }

    /**
     * of Callable 构造
     *
     * <p>转为 runtime 异常</p>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> XTSupplier<O> ofCallableRuntime(Callable<O> task) {
        return new XTSupplier<>(task != null ? () -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } : null);
    }

    /**
     * of Supplier 构造
     */
    @NotNull
    @Contract(pure = true)
    public static <O> XTSupplier<O> ofSupplier(Supplier<O> task) {
        return new XTSupplier<>(task);
    }

    /**
     * of Callable 构造
     *
     * <p>默认忽略异常</p>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> List<XTSupplier<O>> ofCallableList(@NotNull List<? extends Callable<O>> taskList) {
        return taskList.stream().map(XTSupplier::ofCallable).collect(Collectors.toList());
    }

    /**
     * of Callable 构造
     *
     * <p>转为 runtime 异常</p>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> List<XTSupplier<O>> ofCallableListRuntime(@NotNull List<? extends Callable<O>> taskList) {
        return taskList.stream().map(XTSupplier::ofCallableRuntime).collect(Collectors.toList());
    }

    /**
     * of Callable 构造
     *
     * <p>通用 inCatch</p>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> List<XTSupplier<O>> ofCallableListInCatch(@NotNull List<? extends Callable<O>> taskList, Consumer<Exception> inCatch) {
        return taskList.stream().map(task -> XTSupplier.ofCallableInCatch(task, inCatch)).collect(Collectors.toList());
    }

    /**
     * of Callable 构造
     *
     * <p>分别 inCatch</p>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> ArrayList<XTSupplier<O>> ofCallableListInCatch(@NotNull List<? extends Callable<O>> taskList, List<? extends Consumer<Exception>> inCatchList) {
        ArrayList<XTSupplier<O>> arrayList = new ArrayList<>(taskList.size());
        for (int i = 0; i < taskList.size(); i++) {
            arrayList.add(ofCallableInCatch(taskList.get(i), inCatchList.get(i)));
        }
        return arrayList;
    }


    /**
     * of Callable 构造
     *
     * <p>通用 inCatch</p>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> List<XTSupplier<O>> ofCallableListInCatchRet(@NotNull List<? extends Callable<O>> taskList, Function<Exception, O> inCatch) {
        return taskList.stream().map(task -> XTSupplier.ofCallableInCatchRet(task, inCatch)).collect(Collectors.toList());
    }

    /**
     * of Callable 构造
     *
     * <p>分别 inCatch</p>
     */
    @NotNull
    @Contract(pure = true)
    public static <O> ArrayList<XTSupplier<O>> ofCallableListInCatchRet(@NotNull List<? extends Callable<O>> taskList, List<? extends Function<Exception, O>> inCatchList) {
        ArrayList<XTSupplier<O>> arrayList = new ArrayList<>(taskList.size());
        for (int i = 0; i < taskList.size(); i++) {
            arrayList.add(ofCallableInCatchRet(taskList.get(i), inCatchList.get(i)));
        }
        return arrayList;
    }

    /**
     * of Supplier 构造
     */
    @NotNull
    @Contract(pure = true)
    public static <O> List<XTSupplier<O>> ofSupplierList(@NotNull List<? extends Supplier<O>> taskList) {
        return taskList.stream().map(XTSupplier::ofSupplier).collect(Collectors.toList());
    }

    //---------------------inner------------------------

    /**
     * <p>于 1.1.5 去除直接输出错误</p>
     */
    public Supplier<T> getSupplier() {
        return () -> {
            T res = null;
            if (before != null) before.run();
            try {
                if (now != null) res = now.get();
            } catch (Exception ignored) {
            } finally {
                if (after != null) after.run();
            }
            return res;
        };
    }

    /**
     * 添加异常捕获
     */
    public Supplier<T> getCatchSupplier(Consumer<Exception> catchError) {
        return () -> {
            T res = null;
            if (before != null) before.run();
            try {
                if (now != null) res = now.get();
            } catch (Exception e) {
                if (catchError != null) catchError.accept(e);
            } finally {
                if (after != null) after.run();
            }
            return res;
        };
    }

    /**
     * 添加异常捕获
     */
    public Supplier<T> getCatchRetSupplier(Function<Exception, T> catchError) {
        return () -> {
            T res = null;
            if (before != null) before.run();
            try {
                if (now != null) res = now.get();
            } catch (Exception e) {
                if (catchError != null) return catchError.apply(e);
            } finally {
                if (after != null) after.run();
            }
            return res;
        };
    }

    /**
     * 组装
     */
    public Callable<T> getCallable() {
        return () -> {
            T res = null;
            if (before != null) before.run();
            try {
                if (now != null) res = now.get();
            } catch (Exception e) { //重新抛出
                throw e;
            } finally {
                if (after != null) after.run();
            }
            return res;
        };
    }

    /**
     * 添加异常捕获
     */
    public Callable<T> getCatchCallable(Consumer<Exception> catchError) {
        return () -> getCatchSupplier(catchError).get();
    }

    /**
     * 添加异常捕获
     */
    public Callable<T> getCatchRetCallable(Function<Exception, T> catchError) {
        return () -> getCatchRetSupplier(catchError).get();
    }

    //try

    /**
     * try-catch 包围 执行方法
     *
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     */
    public Supplier<T> getTrySupplier(Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return getTrySupplierByCallable(getCallable(), canRunTask, inCatch);
    }

    /**
     * try-catch 包围 执行方法
     *
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     */
    public Callable<T> getTryCallable(Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return getTryCallable(getCallable(), canRunTask, inCatch);
    }

    /**
     * try-catch 包围 执行方法
     *
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     * @since 1.1.5
     */
    public Supplier<T> getTryRetSupplier(Supplier<Boolean> canRunTask, Function<Exception, T> inCatch) {
        return getTryRetSupplierByCallable(getCallable(), canRunTask, inCatch);
    }

    /**
     * try-catch 包围 执行方法
     *
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     * @since 1.1.5
     */
    public Callable<T> getTryRetCallable(Supplier<Boolean> canRunTask, Function<Exception, T> inCatch) {
        return getTryRetCallable(getCallable(), canRunTask, inCatch);
    }


    //----------------override---------------

    /**
     * 组装成callable
     */
    @Override
    public Callable<T> getCallable(Callable<T> now, Runnable before, Runnable after) {
        this.now = ofCallable(now);
        this.before = before;
        this.after = after;
        return getCallable();
    }

    /**
     * 组装成supplier
     */
    @Override
    public Supplier<T> getSupplier(Supplier<T> now, Runnable before, Runnable after) {
        this.now = now;
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
