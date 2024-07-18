package top.cutexingluo.tools.designtools.juc.thread.data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 线程池 Data 接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/31 20:15
 * @since 1.0.4
 */
public interface IThreadData {

    int getCorePoolSize();

    int getMaxPoolSize();

    long getKeepAliveTime();

    TimeUnit getUnit();

    BlockingQueue<Runnable> getWorkQueue();

    ThreadFactory getThreadFactory();

    RejectedExecutionHandler getHandler();

}
