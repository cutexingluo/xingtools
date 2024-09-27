package top.cutexingluo.tools.designtools.helper;

import top.cutexingluo.tools.basepackage.baseimpl.XTAround;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * try-catch 工具 helper 类
 * <p>1. 通过实现该接口即可使用里面的方法
 * <p>2. 封装try-catch语句，使之结构化</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/16 15:58
 * @since 1.0.2
 */
public interface TryCatchHelper {

    default <O> Supplier<O> getTrySupplier(Callable<O> task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return XTAround.getTrySupplierByCallable(task, canRunTask, inCatch);
    }

    default <O> Supplier<O> getTrySupplier(Supplier<O> task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return XTAround.getTrySupplier(task, canRunTask, inCatch);
    }

    default <O> Callable<O> getTryCallable(Callable<O> task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return XTAround.getTryCallable(task, canRunTask, inCatch);
    }

    default <O> Runnable getTtyRunnable(Runnable task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return XTAround.getTryRunnable(task, canRunTask, inCatch);
    }
}
