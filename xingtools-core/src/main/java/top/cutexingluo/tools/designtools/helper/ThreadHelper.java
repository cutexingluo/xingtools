package top.cutexingluo.tools.designtools.helper;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.designtools.juc.async.XTAsync;
import top.cutexingluo.tools.designtools.juc.async.XTCompletionService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

/**
 * 多线程 helper 部分实现接口
 * <p>1. 通过实现该接口即可使用里面的方法</p>
 * <p>2.一些简单实现。其他自行使用 {@link  XTCompletionService} 和 {@link  XTAsync}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/17 16:05
 * @updateFrom 1.0.4
 * @since 1.0.2
 */
@FunctionalInterface
public interface ThreadHelper extends ThreadExecutorHelper {

    @Override
    Executor executor();

    //---------------------XTCompletionService------------------------

    @Override
    default <V> XTCompletionService<V> newCompletionService() {
        return new XTCompletionService<>(executor());
    }


    /**
     * 提交任务
     */
    default <V> Future<V> submit(@NotNull CompletionService<V> completionService, Runnable task, V result) {
        return completionService.submit(task, result);
    }

    /**
     * 提交任务
     */
    default <V> ArrayList<Future<V>> submit(@NotNull XTCompletionService<V> completionService, List<Runnable> tasks, List<V> results) {
        return completionService.submitAll(tasks, results);
    }

    /**
     * 提交任务
     */
    default <V> Future<V> submit(@NotNull CompletionService<V> completionService, Callable<V> task) {
        return completionService.submit(task);
    }

    /**
     * 提交所有任务
     */
    default <V> ArrayList<Future<V>> submit(@NotNull XTCompletionService<V> completionService, List<Callable<V>> tasks) {
        return completionService.submitAll(tasks);
    }


}
