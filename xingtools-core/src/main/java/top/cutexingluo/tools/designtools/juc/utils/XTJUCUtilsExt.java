package top.cutexingluo.tools.designtools.juc.utils;

import top.cutexingluo.tools.basepackage.baseimpl.XTCallable;
import top.cutexingluo.tools.basepackage.baseimpl.XTRunCallUtil;
import top.cutexingluo.tools.designtools.juc.impl.XTRunAndCallList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Thread扩展工具类 一些Thread常规方法
 * <p>不推荐用该类，推荐使用 {@link  top.cutexingluo.tools.designtools.juc.async.XTAsync}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/6 22:06
 */
public class XTJUCUtilsExt {

    //不推荐使用
    public static void doRunsOnThread(List<? extends Runnable> tasks, Runnable beforeThis, Runnable afterThis) {//运行列表，后处理
        for (Runnable t : tasks) {
            Runnable runnable = XTRunCallUtil.getRunnable(t, beforeThis, afterThis);
            new Thread(runnable).start();
        }
    }

    //不推荐使用
    public static List<Object> doCallsOnThread(List<? extends Callable<?>> tasks,
                                               Runnable beforeThis,
                                               Runnable afterThis,
                                               List<Object> results) throws ExecutionException, InterruptedException {
        if (results == null) results = new ArrayList<>();
        for (Callable<?> t : tasks) {
            XTCallable<?> tryCallable = XTRunCallUtil.getTryCallable(t, beforeThis, afterThis);
            FutureTask<?> task = new FutureTask(tryCallable);
            new Thread(task).start();
            results.add(task.get());
        }
        return results;
    }

    //CountDownLatch
    public static void runAsyncToSync(XTRunAndCallList<?> tasks) throws ExecutionException, InterruptedException { //异步转同步，减数工具
        CountDownLatch latch = new CountDownLatch(tasks.getSize());
        Class<?> clazz = tasks.getValueClass();
        if (clazz == Runnable.class) doRunsOnThread(tasks.getRunnableTasks(), null, latch::countDown);
        else if (clazz == Callable.class)
            doCallsOnThread(tasks.getCallableTasks(), null,
                    latch::countDown, (List<Object>) tasks.getResults());
    }

    public static void runAsyncToSync(List<Runnable> tasks) { //异步转同步，减数工具,常规版
        CountDownLatch latch = new CountDownLatch(tasks.size());
        doRunsOnThread(tasks, null, latch::countDown);
    }

    private static void doThis(XTRunAndCallList<?> tasks, Runnable beforeThis, Runnable afterThis) throws ExecutionException, InterruptedException {
        Class<?> clazz = tasks.getValueClass();
        if (clazz == Runnable.class) doRunsOnThread(tasks.getRunnableTasks(), beforeThis, afterThis);
        else if (clazz == Callable.class)
            doCallsOnThread((List<? extends Callable<?>>) tasks.getCallableTasks(), beforeThis,
                    afterThis, (List<Object>) tasks.getResults());
    }

    // CyclicBarrier 需要List实例化这个类赋值
    public static void runCyclicBarrier(XTRunAndCallList<?> tasks) throws ExecutionException, InterruptedException { //异步转同步，加法计数器，循环屏障
        CyclicBarrier cyclicBarrier = new CyclicBarrier(tasks.getSize());
        Runnable afterThis = () -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };
        doThis(tasks, null, afterThis);
    }

    public static void runCyclicBarrier(List<Runnable> tasks) throws ExecutionException, InterruptedException { //异步转同步，加法计数器，循环屏障
        CyclicBarrier cyclicBarrier = new CyclicBarrier(tasks.size());
        Runnable afterThis = () -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        };
        doRunsOnThread(tasks, null, afterThis);
    }

    // Semaphore 限流 需要List实例化这个类赋值
    public static void runSemaphore(XTRunAndCallList<?> tasks, int count) throws ExecutionException, InterruptedException { //异步转同步，加法计数器，循环屏障
        Semaphore semaphore = new Semaphore(count);
        doThis(tasks, () -> {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, semaphore::release);
    }

    public static void runSemaphore(List<Runnable> tasks, int count) throws ExecutionException, InterruptedException { //异步转同步，加法计数器，循环屏障
        Semaphore semaphore = new Semaphore(count);
        doRunsOnThread(tasks, () -> {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, semaphore::release);
    }
}
