package top.cutexingluo.tools.designtools.juc.thread.error;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;

/**
 * 线程池异常捕捉器，用在线程池构造里面
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/9/18 20:12
 */

@FunctionalInterface
public interface ThreadFactoryErrorHandler extends ThreadFactory, Thread.UncaughtExceptionHandler {

    @Override
    default Thread newThread(@NotNull Runnable r) {
        Thread newThread = new Thread(r);
        newThread.setUncaughtExceptionHandler(this);
        return newThread;
    }
}
