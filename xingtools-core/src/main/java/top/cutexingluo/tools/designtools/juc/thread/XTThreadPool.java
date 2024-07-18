package top.cutexingluo.tools.designtools.juc.thread;


import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.designtools.juc.thread.data.IThreadData;
import top.cutexingluo.tools.designtools.juc.thread.data.ThreadData;
import top.cutexingluo.tools.designtools.juc.utils.XTJUC;

import java.util.List;
import java.util.concurrent.*;

/**
 * 常用，推荐用，<br>
 * JUC 线程池 ，主要用于 获取线程池，直接运行线程池
 * <p>通过手动 @Bean 将 getThreadPool 注入到容器</p>
 * <p>于 1.0.4 更新默认为 n+1 核心数, 2 分钟 keepAlive</p>
 *
 * @author XingTian
 * @version 1.0.2
 * @date 2022-11-21
 */
@Data
public class XTThreadPool {
    private static volatile XTThreadPool instance;

    /**
     * 如果 12 核，默认核心线程 3 核，最大 9 核，阻塞 4 个 ，拒绝策略 主线程备用
     *
     * <p>1.0.4 以前的线程配置</p>
     * <p>于 1.0.4 更新默认为 n+1 核心数, 2 分钟 keepAlive</p>
     */
    public static final ThreadData FastThreadPoolData = new ThreadData(
            XTJUC.getCoresNumber() / 4, XTJUC.getCoresNumber() / 4 * 3,
            2, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(4), Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );


    @NotNull
    // 核心是这个ThreadPoolExecutor，封装了一层是避免冲突
    private ThreadPoolExecutor threadPool;

    public XTThreadPool execute(Runnable runnable) {//执行
        threadPool.execute(runnable);
        return this;
    }

    public XTThreadPool shutdown() {//关闭
        threadPool.shutdown();
        return this;
    }

    public void runThreadAndDown(Runnable runnable) {//内部执行
        threadPool.execute(runnable);
        threadPool.shutdown();
    }

    public void executeListAndDown(List<Runnable> list) {
        list.forEach(threadPool::execute);
        threadPool.shutdown();
    }

    public static void runNewToolAndDown(Runnable runnable) {//静态一次执行
        XTThreadPool threadPool = new XTThreadPool();
        threadPool.execute(runnable);
        threadPool.shutdown();
    }

    public static void runNewToolAndDown(List<Runnable> list) {//静态一次执行
        XTThreadPool threadPool = new XTThreadPool();
        list.forEach(threadPool::execute);
        threadPool.shutdown();
    }

    public XTThreadPool(int corePoolSize,
                        int maximumPoolSize,
                        long keepAliveTime,
                        TimeUnit unit,
                        BlockingQueue<Runnable> workQueue,
                        ThreadFactory threadFactory,
                        RejectedExecutionHandler rejectedExecutionHandler) {
        this.threadPool = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory, rejectedExecutionHandler);
    }

    public static XTThreadPool getInstance() { //单例
        if (instance == null) {
            synchronized (XTThreadPool.class) {
                if (instance == null) {
                    instance = new XTThreadPool();
                }
            }
        }
        return instance;
    }

    public XTThreadPool(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPool = threadPoolExecutor;
    }

    /**
     * 通过 IThreadData 构建线程池
     *
     * @param threadData 线程数据
     * @since 1.0.4
     */
    public XTThreadPool(@NotNull IThreadData threadData) {
        this(threadData.getCorePoolSize(), threadData.getMaxPoolSize(),
                threadData.getKeepAliveTime(), threadData.getUnit(),
                threadData.getWorkQueue(), threadData.getThreadFactory(),
                threadData.getHandler());
    }


    public XTThreadPool() {
        this(XTJUC.getCoresNumber() + 1, XTJUC.getCoresNumber() * 2 + 1,
                2, TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(200), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public XTThreadPool(int cores, int maxSize, int aliveTimeout, TimeUnit unit, int queueSize,
                        RejectedExecutionHandler rejectedExecutionHandler) {
        this(cores, maxSize,
                aliveTimeout, unit,
                new LinkedBlockingDeque<>(queueSize), Executors.defaultThreadFactory(),
                rejectedExecutionHandler);
    }

    public XTThreadPool(int cores, int maxSize, int aliveTimeout, TimeUnit unit, int queueSize,
                        RejectPolicy rejectPolicy) {
        this(cores, maxSize,
                aliveTimeout, unit,
                new LinkedBlockingDeque<>(queueSize), Executors.defaultThreadFactory(),
                getPolicy(rejectPolicy));
    }

    public XTThreadPool(int cores, int maxSize, int queueSize,
                        RejectedExecutionHandler rejectedExecutionHandler) {
        this(cores, maxSize,
                2, TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(queueSize), Executors.defaultThreadFactory(),
                rejectedExecutionHandler);
    }

    public XTThreadPool(int cores, int maxSize, int queueSize,
                        RejectPolicy rejectPolicy) {
        this(cores, maxSize,
                2, TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(queueSize), Executors.defaultThreadFactory(),
                getPolicy(rejectPolicy));
    }

    @NotNull
    @Contract("_ -> new")
    public static RejectedExecutionHandler getPolicy(RejectPolicy rejectPolicy) {
        return ThreadData.policy(rejectPolicy);
    }

    /**
     * 打印当前线程池的状态
     */
    public String printThreadPoolStatus() {
        return String.format("core_size:%s,thread_current_size:%s;" +
                        "thread_max_size:%s;queue_current_size:%s,total_task_count:%s", threadPool.getCorePoolSize(),
                threadPool.getActiveCount(), threadPool.getMaximumPoolSize(), threadPool.getQueue().size(),
                threadPool.getTaskCount());
    }
}
