package top.cutexingluo.tools.designtools.juc.async;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * ExecutorCompletionService 的 工具
 *
 * <p>也可以使用工具类 {@link XTAsync}</p>
 * <p>或者直接实现接口 {@link top.cutexingluo.tools.designtools.helper.ThreadHelper}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/5 15:27
 * @update 1.0.3
 */
//--------------CompletionService--------------
public class XTCompletionService<V> extends ExecutorCompletionService<V> {

    public XTCompletionService(Executor executor) {
        super(executor);
    }

    public XTCompletionService(Executor executor, BlockingQueue<Future<V>> completionQueue) {
        super(executor, completionQueue);
    }

    public static <T> XTCompletionService<T> newInstance(Executor executor) {
        return new XTCompletionService<>(executor);
    }

    public static <T> XTCompletionService<T> newInstance(Executor executor, BlockingQueue<Future<T>> completionQueue) {
        return new XTCompletionService<>(executor, completionQueue);
    }

    /**
     * 执行全部
     */
    public ArrayList<Future<V>> submitAll(@NotNull Collection<Callable<V>> tasks) {
        ArrayList<Future<V>> futures = new ArrayList<>(tasks.size());
        for (Callable<V> task : tasks) {
            if (task == null) continue;
            futures.add(submit(task));
        }
        return futures;
    }

    /**
     * 执行全部
     */
    public ArrayList<Future<V>> submitAll(@NotNull List<Runnable> tasks, @NotNull List<V> results) {
        ArrayList<Future<V>> futures = new ArrayList<>(tasks.size());
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i) == null) continue;
            V result = i < results.size() ? results.get(i) : null;
            futures.add(submit(tasks.get(i), result));
        }
        return futures;
    }


}