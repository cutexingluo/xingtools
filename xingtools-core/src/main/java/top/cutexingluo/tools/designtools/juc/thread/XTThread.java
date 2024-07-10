package top.cutexingluo.tools.designtools.juc.thread;

/**
 * <p>
 * JUC 工具类 ，主要用于 获取线程，直接运行线程
 * <br>
 * 不推荐使用
 * </p>
 *
 * @author XingTian
 * @version 1.0
 * @date 2022-11-21
 */

//@Component
public class XTThread {

    public static Thread getThread(Runnable runnable) {//获取实例化线程
        return new Thread(runnable);
    }

    public static Thread getThread(Runnable runnable, String name) {//获取实例化线程
        if (name == null) return new Thread(runnable); //判断为空 name = "Thread-" + nextThreadNum()
        return new Thread(runnable, name);
    }

    public static Thread getCurrentThread() {//getName()//获取当前线程
        return Thread.currentThread();
    }

    public static String getCurrentThreadName() {//获取当前线程名称
        return Thread.currentThread().getName();
    }
}
