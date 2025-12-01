package top.cutexingluo.tools.designtools.helper;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.designtools.juc.lock.handler.LockHandler;

import java.util.concurrent.Callable;

/**
 * lock helper 类
 * <p>利用实现接口的方式，来使用该工具</p>
 * <p>redo 未来将添入 xingcore </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/17 15:25
 * @since 1.0.2
 */
@FunctionalInterface
public interface LockHelper {


    /**
     * lockHandler 操作工具
     *
     * @since 1.1.4
     */
    LockHandler lockHandler();


    /**
     * 添加锁
     */
    default <V> Callable<V> lockTask(@NotNull LockHandler lockHandler, Callable<V> task) {
        return lockHandler.decorate(task);
    }

    /**
     * 添加锁
     *
     * <p>于 1.1.4 更改为lockHandler() 方法</p>
     */
    default <V> Callable<V> lockTask(Callable<V> task) {
        return lockHandler().decorate(task);
    }


}
