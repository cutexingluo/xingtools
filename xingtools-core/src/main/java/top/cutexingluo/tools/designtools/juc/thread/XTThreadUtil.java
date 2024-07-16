package top.cutexingluo.tools.designtools.juc.thread;

import top.cutexingluo.tools.designtools.juc.lock.XTLock;

import java.util.concurrent.*;

/**
 * 不推荐用Thread，推荐用线程池<br>
 * <p>不推荐用该类，推荐使用 {@link  top.cutexingluo.tools.designtools.juc.async.XTAsync}</p>
 * <br>
 * update from old XTThead at  2023-4-6
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/6 21:54
 */
//@Slf4j
public class XTThreadUtil {
    //*********************************************************

    public static void runThread(Runnable runnable, String threadName) {//运行线程
        XTThread.getThread(runnable, threadName).start();
    }

    public static void runThread(Runnable runnable) {//运行线程
        runThread(runnable, null);
    }

    //    @XTException(name = "XTThread runSyncThread error", desc = "可能为 并发修改异常")
    //如果加锁了再使用这个可能会是 双重锁。
    public static void runSyncThread(XTLock lock, Runnable runnable, String threadName) {//运行同步线程
        XTThread.getThread(lock.getLockRunnable(runnable), threadName).start();
    }

    public static void runSyncThread(XTLock lock, Runnable runnable) {//运行同步线程
        runSyncThread(lock, runnable, null);
    }

    public static void runSyncThread(Runnable runnable, String threadName) {//运行同步线程
        runSyncThread(new XTLock(), runnable, threadName);
    }

    public static void runSyncThread(Runnable runnable) {//运行同步线程
        runSyncThread(new XTLock(), runnable, null);
    }


    public static Object callThread(Callable<?> callable, String threadName) throws ExecutionException, InterruptedException {
        FutureTask<?> futureTask = new FutureTask<>(callable);
        Thread thread = threadName == null ? new Thread(futureTask) : new Thread(futureTask, threadName);
        thread.start();
        return futureTask.get();
    }

    public static Object callThread(Callable<?> callable) throws ExecutionException, InterruptedException {
        return callThread(callable, null);
    }

    //    @XTException(name = "XTThread callThread error", desc = "可能为 并发修改异常 或 执行异常 或中断异常")
    public static Object callSyncThread(Callable<?> callable, String threadName) throws ExecutionException, InterruptedException {
        FutureTask<?> futureTask = new FutureTask<>(callable);
        Thread thread = threadName == null ? new Thread(futureTask) : new Thread(futureTask, threadName);
        thread.start();
        return futureTask.get();
    }

    public static Object callSyncThread(Callable<?> callable) throws ExecutionException, InterruptedException {
        return callThread(callable, null);
    }

    //*********************************************************

    //    @XTException(name = "XTThread runThreadByCount error", desc = "可能为 并发修改异常")
    public static void runThreadByCount(int count, Runnable runnable) {//多次运行线程
        if (count < 0) count = 1;
        for (int i = 1; i <= count; i++) {
            XTThread.getThread(runnable, String.valueOf(i)).start();
        }
    }

    //    @XTException(name = "XTThread runThreadByCount error", desc = "可能为 其他并发异常")
    public static void runThreadSyncByCount(int count, Runnable runnable) {//多次运行同步线程
        if (count < 0) count = 1;
        for (int i = 1; i <= count; i++) {
            XTThread.getThread(runnable, String.valueOf(i)).start();
        }
    }


    private static final long OVERTIME = 120;

    /**
     * 停止线程池
     * 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务.
     * 如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数.
     * 如果仍然超時，則強制退出.
     * 另对在shutdown时线程本身被调用中断做了处理.
     */
    public static void shutdownAndAwaitTermination(ExecutorService pool,Runnable notDoneTask) {
        if (pool != null && !pool.isShutdown()) {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(OVERTIME, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                    if (!pool.awaitTermination(OVERTIME, TimeUnit.SECONDS)) {
                        if(notDoneTask != null) notDoneTask.run();
                    }
                }
            } catch (InterruptedException ie) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 打印线程异常信息
     */
    public static void printException(Runnable r, Throwable t) {
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
