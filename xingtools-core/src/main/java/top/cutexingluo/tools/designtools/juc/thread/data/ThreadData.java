package top.cutexingluo.tools.designtools.juc.thread.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.designtools.juc.thread.RejectPolicy;
import top.cutexingluo.tools.designtools.juc.utils.XTJUC;

import java.util.concurrent.*;

/**
 * 线程池数据
 *
 * <p>于 1.0.4 更新默认为 n+1 核心数, 2 分钟 keepAlive</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/31 20:17
 * @updateFrom 1.0.4
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class ThreadData implements IThreadData {
    /**
     * 核心线程数
     */
    private int corePoolSize;
    /**
     * 最大线程数
     */
    private int maxPoolSize;
    /**
     * 活跃时间
     */
    private long keepAliveTime;
    /**
     * 时间单位
     */
    private TimeUnit unit;
    /**
     * 阻塞队列
     */
    private BlockingQueue<Runnable> workQueue;
    /**
     * 线程工厂
     */
    private ThreadFactory threadFactory;
    /**
     * 拒绝策略
     */
    private RejectedExecutionHandler handler;

    public ThreadData(int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        this(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory(), handler);
    }

    public ThreadData(int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit unit, int queueSize, RejectedExecutionHandler handler) {
        this(corePoolSize, maxPoolSize, keepAliveTime, unit, new LinkedBlockingDeque<>(queueSize), Executors.defaultThreadFactory(), handler);
    }

    public ThreadData(int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit unit, int queueSize, RejectPolicy rejectPolicy) {
        this(corePoolSize, maxPoolSize, keepAliveTime, unit, new LinkedBlockingDeque<>(queueSize), Executors.defaultThreadFactory(), policy(rejectPolicy));
    }

    public ThreadData(int cores, int maxSize, int aliveTimeout, TimeUnit unit, int queueSize,
                      RejectedExecutionHandler rejectedExecutionHandler) {
        this(cores, maxSize,
                aliveTimeout, unit,
                new LinkedBlockingDeque<>(queueSize), Executors.defaultThreadFactory(),
                rejectedExecutionHandler);
    }

    public ThreadData(int cores, int maxSize, int aliveTimeout, TimeUnit unit, int queueSize,
                      RejectPolicy rejectPolicy) {
        this(cores, maxSize,
                aliveTimeout, unit,
                new LinkedBlockingDeque<>(queueSize), Executors.defaultThreadFactory(),
                policy(rejectPolicy));
    }


    public ThreadData(int cores, int maxSize, int queueSize,
                      RejectedExecutionHandler rejectedExecutionHandler) {
        this(cores, maxSize,
                2, TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(queueSize), Executors.defaultThreadFactory(),
                rejectedExecutionHandler);
    }

    public ThreadData(int cores, int maxSize, int queueSize,
                      RejectPolicy rejectPolicy) {
        this(cores, maxSize,
                2, TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(queueSize), Executors.defaultThreadFactory(),
                policy(rejectPolicy));
    }

    public ThreadData() {
        this(XTJUC.getCoresNumber() + 1, XTJUC.getCoresNumber() * 2 + 1,
                2, TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(200), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }


    @NotNull
    @Contract("_ -> new")
    public static RejectedExecutionHandler policy(@NotNull RejectPolicy rejectPolicy) {
        switch (rejectPolicy) {
            case Abort:
                return new ThreadPoolExecutor.AbortPolicy();
            case Discard:
                return new ThreadPoolExecutor.DiscardPolicy();
            case DiscardOldest:
                return new ThreadPoolExecutor.DiscardOldestPolicy();
            default:
                return new ThreadPoolExecutor.CallerRunsPolicy();
        }
    }

}
