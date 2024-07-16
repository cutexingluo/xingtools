package top.cutexingluo.tools.designtools.helper;

import org.jetbrains.annotations.NotNull;
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
     */
    XTLockHandlerMeta lockHandlerMeta();

    /**
     * 获取 LockerHandler 操作工具
     */
    default XTLockHandler newLockHandler() {
        XTLockHandlerMeta handlerMeta = lockHandlerMeta();
        if (handlerMeta == null) {
            throw new NullPointerException("No lockHandlerMeta !");
        }
        return new XTLockHandler(handlerMeta);
    }

    /**
     * 添加锁
     */
    default <V> Callable<V> lockTask(@NotNull XTLockHandler lockHandler, Callable<V> task) {
        return lockHandler.decorate(task);
    }

    /**
     * 添加锁
     */
    default <V> Callable<V> lockTask(Callable<V> task) {
        return newLockHandler().decorate(task);
    }
}
