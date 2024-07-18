package top.cutexingluo.tools.designtools.juc.thread;

/**
 * 拒绝策略
 *
 * @author XingTian
 * @date 2023/10/16
 */
public enum RejectPolicy {
    /**
     * 不处理抛出异常
     */
    Abort,
    /**
     * 哪里来的去哪里，主线程执行
     */
    CallerRuns,
    /**
     * 拒绝
     */
    Discard,
    /**
     * 拒绝队列第一个（最老的）
     */
    DiscardOldest,
}
