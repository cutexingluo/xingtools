package top.cutexingluo.tools.basepackage.base;

import top.cutexingluo.tools.basepackage.baseimpl.XTAround;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Callable Supplier 公用方法接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/27 17:28
 * @since 1.1.5
 */
public interface BaseCallableSupplier<T> {

    //---------------------inner------------------------

    /**
     * 得到 Supplier
     */
    Supplier<T> getSupplier();

    /**
     * 得到 Supplier
     *
     * @param inCatch 异常处理处理
     */
    Supplier<T> getCatchSupplier(Consumer<Exception> inCatch);

    /**
     * 得到 Supplier
     *
     * @param inCatch 异常处理处理, 返回值
     */
    Supplier<T> getCatchRetSupplier(Function<Exception, T> inCatch);


    /**
     * 得到 Supplier
     *
     * <p>所有异常封装为 RuntimeException , 并抛出</p>
     */
    default Supplier<T> getSupplierRuntime() {
        return getCatchSupplier(e -> {
            throw new RuntimeException(e);
        });
    }

    /**
     * 得到 Callable
     */
    Callable<T> getCallable();

    /**
     * 得到 Callable
     *
     * @param inCatch 异常处理处理
     */
    default Callable<T> getCatchCallable(Consumer<Exception> inCatch) {
        return () -> getCatchSupplier(inCatch).get();
    }

    /**
     * 得到 Callable
     *
     * @param inCatch 异常处理处理, 返回值
     */
    default Callable<T> getCatchRetCallable(Function<Exception, T> inCatch) {
        return () -> getCatchRetSupplier(inCatch).get();
    }

    //---------------------inner-run------------------------

    /**
     * try-catch 包围 执行方法
     *
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     * @since 1.0.4
     */
    default Supplier<T> getTrySupplier(Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return XTAround.getTrySupplierByCallable(getCallable(), canRunTask, inCatch);
    }

    /**
     * try-catch 包围 执行方法
     *
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     * @since 1.0.4
     */
    default Callable<T> getTryCallable(Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return XTAround.getTryCallable(getCallable(), canRunTask, inCatch);
    }

    /**
     * try-catch 包围 执行方法
     *
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     * @since 1.1.5
     */
    default Supplier<T> getTryRetSupplier(Supplier<Boolean> canRunTask, Function<Exception, T> inCatch) {
        return XTAround.getTryRetSupplierByCallable(getCallable(), canRunTask, inCatch);
    }

    /**
     * try-catch 包围 执行方法
     *
     * @param canRunTask 是否可以运行任务
     * @param inCatch    捕获异常
     * @since 1.1.5
     */
    default Callable<T> getTryRetCallable(Supplier<Boolean> canRunTask, Function<Exception, T> inCatch) {
        return XTAround.getTryRetCallable(getCallable(), canRunTask, inCatch);
    }

}
