package top.cutexingluo.tools.designtools.helper;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.designtools.juc.lock.handler.LockHandler;
import top.cutexingluo.tools.designtools.juc.lock.handler.XTLockHandler;
import top.cutexingluo.tools.designtools.juc.lock.handler.XTLockHandlerMeta;

import java.util.concurrent.Callable;

/**
 * lock helper 类
 * <p>利用实现接口的方式，来使用该工具</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/17 15:25
 * @since 1.0.2
 */
@FunctionalInterface
public interface LockHelper {

    /**
     * 设置锁基本数据
     *
     * @return 基本数据
     * @deprecated 1.1.4 版本 已弃用, 请直接使用 lockHandler() 方法
     */
    @Deprecated
    default XTLockHandlerMeta lockHandlerMeta() {
        return null;
    }

    /**
     * 获取 LockerHandler 操作工具
     *
     * @deprecated 1.1.4 版本 已弃用, 请直接使用 lockHandler() 方法
     */
    @Deprecated
    default XTLockHandler newLockHandler() {
        XTLockHandlerMeta handlerMeta = lockHandlerMeta();
        if (handlerMeta == null) {
            throw new NullPointerException("No lockHandlerMeta !");
        }
        return new XTLockHandler(handlerMeta);
    }

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
