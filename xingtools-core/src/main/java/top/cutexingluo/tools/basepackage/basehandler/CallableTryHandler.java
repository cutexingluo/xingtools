package top.cutexingluo.tools.basepackage.basehandler;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 一元装饰附加接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/17 22:38
 * @since 1.0.2
 */
public interface CallableTryHandler {
    /**
     * 对 task 进行操作
     *
     * @param task       目标任务
     * @param canRunTask 是否可以运行任务
     * @param inCatch    异常捕获
     * @return {@link Callable}<{@link T}>
     */
    <T> Callable<T> decorate(Callable<T> task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch);

}
