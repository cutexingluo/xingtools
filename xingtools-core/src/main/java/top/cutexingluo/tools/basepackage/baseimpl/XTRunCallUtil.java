package top.cutexingluo.tools.basepackage.baseimpl;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>Runnable, Callable, Supplier 的工具类</p>
 * <p>可进行整合，转化，装饰</p>
 * <p>其他方法在 {@link XTRunnable} 和 {@link XTCallable} 里面</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 18:04
 */
public class XTRunCallUtil {
    public static Runnable getRunnable(Runnable now, Runnable before, Runnable after) {
        return new XTRunnable(now, before, after);
    }

    public static Runnable getRunnable(Runnable now, Runnable after) {
        return new XTRunnable(now, null, after);
    }

    public static Runnable getTryRunnable(Runnable now, Runnable before, Runnable after) {
        return XTRunnable.getTryRunnable(now, before, after);
    }

    public static Runnable getTryRunnable(Runnable now, Runnable after) {
        return getTryRunnable(now, null, after);
    }

    //最标准 Callable 套用模板
    public static <V> XTCallable<V> getTryCallable(Callable<V> now, Runnable before, Runnable after) {
        return new XTCallable<V>(now, before, after);
    }

    public static <V> XTCallable<V> getTryCallable(Callable<V> now, Runnable after) {
        return getTryCallable(now, null, after);
    }

    /**
     * try-catch 包围 执行方法
     * <p>其他方法请用 XTCallable 调用 </p>
     */
    public static <V> Callable<V> getTryCallable(Callable<V> task) {
        return XTCallable.getTryCallable(task);
    }

    /**
     * try-catch 包围 执行方法
     * <p>其他方法请用 XTCallable 调用 </p>
     */
    public static <V> Supplier<V> getTrySupplier(Supplier<V> task) {
        return XTCallable.getTrySupplier(task);
    }

    /**
     * try-catch 包围 执行方法
     * <p>其他方法请用 XTCallable 调用 </p>
     */
    public static <V> Supplier<V> getTrySupplier(Callable<V> task, Consumer<Exception> inCatch) {
        return XTCallable.getTrySupplier(task, inCatch);
    }

}
