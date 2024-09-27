package top.cutexingluo.tools.basepackage.baseimpl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.base.BaseCallableSupplier;
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
 * @see BaseCallableSupplier
 * @since 1.1.5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class XTSupplier<T> extends XTAround implements BaseCallableSupplier<T>,
        Callable<T>, Supplier<T>, ComCallable<T>, ComSupplier<T>, CallableHandler, SupplierHandler {
    /**
     * 当前任务
     */
    Supplier<T> now;
    /**
     * 前置或后置任务
     */
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
     * <p><b>callable 转 supplier 默认忽略异常</b></p>
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
        return ofCallableInCatch(task, e -> {
            throw new RuntimeException(e);
        });
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
     * 组装成新 Supplier
     * <p><b>与 XTCallable  不同, 异常将被重新抛出</b></p>
     */
    @Override
    public Supplier<T> getSupplier() {
        return () -> {
            T res = null;
            if (before != null) before.run();
            try {
                if (now != null) res = now.get();
            } catch (Exception e) {
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
    @Override
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
    @Override
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
     * 组装成新 Callable
     * <p>XTCallable 和 XTSupplier 相同, 异常将被重新抛出</p>
     */
    @Override
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

    /**
     * 调用 getCallable() 方法
     * <p><b>未处理的异常将被重新抛出</b></p>
     */
    @Override
    public T call() throws Exception {
        return getCallable().call();
    }

    /**
     * 调用 getSupplier() 方法
     * <p><b>未处理的异常将被重新抛出</b></p>
     */
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
