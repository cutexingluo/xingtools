package top.cutexingluo.tools.aop.thread.run;

/**
 * 线程执行时机策略
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/4 13:38
 * @since 1.0.2
 */
public enum ThreadTimePolicy {

    /**
     * 在最后一个子线程加载后, 统一执行线程。
     *
     * <p>仅 Async 前缀策略有用</p>
     * <p>如果是Future策略，与 GetResultAfterLastSon 策略自动互转 </p>
     */
    AsyncAfterLastSon,

    /**
     * 在主线程 @MainThread 结束后, 统一执行线程。
     *
     * <p>仅 Async 前缀策略有用</p>
     * <p>如果是Future策略，与 GetFuture 策略自动互转 </p>
     */
    AsyncAfterMain,


    /**
     * <p> Future  默认策略 </p>
     * 仅Future 策略有用，则是阻塞时机为最后一个子线程。先进先出 (FIFO) 。
     * <p>在最后一个子线程加载完成阻塞线程以获取结果，所有结果会放入 getResult 中</p>
     */
    GetResultAfterLastSon,

    /**
     * 仅Future 策略有用，则是阻塞时机为任意时机。 先进先出 (FIFO) 。
     * <p>如是 Future 策略，Future 结果会放入 getFuture 中。然后可以自行阻塞线程获取（在主线程里面获取）。</p>
     * <p>主线程执行完了，子线程才会阻塞等待执行完获取结果，所有结果会放入 getResult 中。</p>
     */
    GetFuture,

    /**
     * 仅Future 策略有用，则是阻塞时机为最后一个子线程，并且先执行完成的先进入结果列表。短作业(SLF)。
     * <p>在最后一个子线程加载完成阻塞线程以获取结果，所有执行结果会放入 getResult 中</p>
     * <p>如是 Future 策略，任务 先完成的会优先放入 结果(Result / Future) 中。</p>
     * <p>如果是其他策略，转为 AsyncAfterLastSon </p>
     */
    GetQuickResult,
}
