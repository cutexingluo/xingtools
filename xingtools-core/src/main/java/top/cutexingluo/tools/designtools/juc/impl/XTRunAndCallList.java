package top.cutexingluo.tools.designtools.juc.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Runnable 或者Callable 列表
 * <p>只能装一个，已废弃，不建议使用</p>
 *
 * @author XingTian
 * @date 2022/10/2
 */
@Data
@AllArgsConstructor
public class XTRunAndCallList<V> implements Runnable, Callable<V> {
    //双任务类
    //一次只能装一个
    private List<Runnable> runnableTasks;
    private List<Callable<V>> callableTasks;
    private List<V> results;

    public XTRunAndCallList(Class<?> type) {
        if (type == Runnable.class) {
            this.runnableTasks = new ArrayList<>();
        } else if (type == Callable.class) {
            this.callableTasks = new ArrayList<>();
        }
    }

    public <T> XTRunAndCallList(Class<T> type, List<T> tasks) {
        if (type == Runnable.class) {
            this.runnableTasks = (List<Runnable>) tasks;
        } else if (type == Callable.class) {
            this.callableTasks = (List<Callable<V>>) tasks;
        }
    }

    //默认Runnable
    public XTRunAndCallList(List<Runnable> tasks) {
        this.runnableTasks = (List<Runnable>) tasks;
    }


    //    @XTException(name = "XTRunnableList addRunnable error", desc = "可能为 空指针 异常")
    public void addRunnable(Runnable runnable) {
        runnableTasks.add(runnable);
    }

    //    @XTException(name = "XTRunnableList addRunnable error", desc = "可能为 空指针 异常")
    public void addCallable(Callable<V> callable) {
        callableTasks.add(callable);
    }

    public Class<?> getValueClass() {//一次只能装一个，获取类型
        if (runnableTasks != null && runnableTasks.size() > 0) {
            return Runnable.class;
        } else if (callableTasks != null && callableTasks.size() > 0) {
            return Callable.class;
        }
        return null;
    }

    public int getSize() {//获取大小
        if (runnableTasks != null && runnableTasks.size() > 0) {
            return runnableTasks.size();
        } else if (callableTasks != null && callableTasks.size() > 0) {
            return callableTasks.size();
        }
        return 0;
    }

    public int runnableSize() {
        return runnableTasks.size();
    }

    public int callableSize() {
        return callableTasks.size();
    }

    @Deprecated //废弃，因为只能装一个
    public XTRunAndCallList(List<Runnable> runnableList, List<Callable<V>> callableList) {
        this.runnableTasks = runnableList;
        this.callableTasks = callableList;
    }

    @Override
    @Deprecated //这里是直接单线程运行,不建议直接使用。需要获得list列表使用
    public void run() {
        for (Runnable task : runnableTasks) {
            task.run();
        }
    }

    @Override
    @Deprecated //这里是直接单线程运行,不建议直接使用
    public V call() throws Exception { //结果需要在本类的result获取
        if (results == null) results = new ArrayList<>();
        for (Callable<V> task : callableTasks) {
            results.add(task.call());
        }
        return null;
    }
}
