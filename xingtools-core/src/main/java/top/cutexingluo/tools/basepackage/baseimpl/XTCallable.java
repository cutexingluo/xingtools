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
 * <p>4.如果是 callable 转 supplier 会有异常忽略问题, 建议使用 {@link XTSupplier}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 23:08
 * @updateFrom 1.0.4
 * @see XTSupplier
 * @see BaseCallableSupplier
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class XTCallable<T> extends XTAround implements BaseCallableSupplier<T>,
        Callable<T>, Supplier<T>, ComCallable<T>, ComSupplier<T>, CallableHandler, SupplierHandler {
    /**
     * 当前任务
     */
    Callable<T> now;
    /**
     * 前置或后置任务
     */
    Runnable before, after;

    public XTCallable(Callable<T> task) {
        this.now = task;
    }

    //---------------------static------------------------

    //---------------------constructor------------------------

    /**
     * of Callable 构造
     *
     * @since 1.1.5
     */
    @NotNull
    @Contract(pure = true)
    public static <O> XTCallable<O> ofCallable(Callable<O> task) {
        return new XTCallable<>(task);
    }

    /**
     * of Supplier 构造
     *
     * @since 1.1.5
     */
    @NotNull
    @Contract(pure = true)
    public static <O> XTCallable<O> ofSupplier(Supplier<O> task) {
        return new XTCallable<>(task != null ? task::get : null);
    }

    /**
     * of Callable 构造
     *
     * @since 1.1.5
     */
    @NotNull
    @Contract(pure = true)
    public static <O> List<XTCallable<O>> ofCallableList(@NotNull List<? extends Callable<O>> taskList) {
        return taskList.stream().map(XTCallable::ofCallable).collect(Collectors.toList());
    }

    /**
     * of Supplier 构造
     *
     * @since 1.1.5
     */
    @NotNull
    @Contract(pure = true)
    public static <O> List<XTCallable<O>> ofSupplierList(@NotNull List<? extends Supplier<O>> taskList) {
        return taskList.stream().map(XTCallable::ofSupplier).collect(Collectors.toList());
    }


    //---------------------inner------------------------

    /**
     * 组装成新 Supplier
     * <p><b>与 XTSupplier  不同, callable 转 supplier  异常将被忽略</b></p>
     * <p>于 1.1.5 去除直接输出错误</p>
     */
    @Override
    public Supplier<T> getSupplier() {
        return () -> {
            T res = null;
            if (before != null) before.run();
            try {
                if (now != null) res = now.call();
            } catch (Exception e) {
//                e.printStackTrace(); // 直接输出
            } finally {
                if (after != null) after.run();
            }
            return res;
        };
    }


    /**
     * 添加异常捕获
     * <p>v1.1.5 fix bug</p>
     */
    @Override
    public Supplier<T> getCatchSupplier(Consumer<Exception> catchError) {
        return () -> {
            T res = null;
            if (before != null) before.run();
            try {
                if (now != null) res = now.call();
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
                if (now != null) res = now.call();
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
                if (now != null) res = now.call();
            } catch (Exception e) { //重新抛出
                throw e;
            } finally {
                if (after != null) after.run();
            }
            return res;
        };
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
        this.now = ofSupplier(now);
        this.before = before;
        this.after = after;
        return getSupplier();
    }

    /**
     * 调用 getCallable() 方法
     * <p><b>异常将被重新抛出</b></p>
     */
    @Override
    public T call() throws Exception {
        return getCallable().call();
    }

    /**
     * 调用 getSupplier() 方法
     * <p><b>callable 转 supplier 异常会被忽略</b></p>
     */
    @Override
    public T get() {
        return getSupplier().get();
    }


    /**
     * 转为 RuntimeException
     *
     * @since 1.1.5
     */
    public T getRuntime() {
        return getSupplierRuntime().get();
    }

    /**
     * inner catch 异常处理
     *
     * @since 1.1.5
     */
    public T getInCatch(Consumer<Exception> inCatch) {
        return getCatchSupplier(inCatch).get();
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
