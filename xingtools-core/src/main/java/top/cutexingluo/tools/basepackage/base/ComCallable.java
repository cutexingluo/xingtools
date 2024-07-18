package top.cutexingluo.tools.basepackage.base;

import java.util.concurrent.Callable;

/**
 * 三元整合接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 23:06
 */
@FunctionalInterface
public interface ComCallable<T> {
    Callable<T> getCallable(Callable<T> now, Runnable before, Runnable after);
}
