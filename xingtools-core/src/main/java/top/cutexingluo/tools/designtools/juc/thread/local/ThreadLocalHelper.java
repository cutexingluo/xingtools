package top.cutexingluo.tools.designtools.juc.thread.local;

/**
 * ThreadLocal 的工具类
 * <p>helper类型，可以直接新建实例</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/9/27 15:27
 */
public class ThreadLocalHelper implements BaseThreadLocalHelper<String> {

    public static ThreadLocalHelper newInstance() {
        return new ThreadLocalHelper();
    }

    private final ThreadLocal<String> threadLocal;

    public ThreadLocalHelper() {
        threadLocal = new ThreadLocal<>();
    }

    public ThreadLocalHelper(String name) {
        threadLocal = new ThreadLocal<>();
        threadLocal.set(name);
    }


    @Override
    public ThreadLocalHelper setThreadLocalValue(String name) {
        threadLocal.set(name);
        return this;
    }

    @Override
    public String getThreadLocalValue() {
        return threadLocal.get();
    }

    @Override
    public ThreadLocal<String> getThreadLocal() {
        return threadLocal;
    }

    @Override
    public ThreadLocalHelper doTask(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    public void remove() {
        threadLocal.remove();
    }

    public void doTaskAndRemove(Runnable runnable) {
        runnable.run();
        remove();
    }

    @Override
    public void runTaskAndRemove(String taskName, Runnable runnable) {
        setThreadLocalValue(taskName);
        doTaskAndRemove(runnable);
    }

}
