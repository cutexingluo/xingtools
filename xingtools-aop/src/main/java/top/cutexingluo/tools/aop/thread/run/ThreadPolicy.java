package top.cutexingluo.tools.aop.thread.run;

/**
 * 线程策略
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/2 13:12
 * @since 1.0.2
 */
public enum ThreadPolicy {

    /**
     * <p>默认策略</p>
     * 自动异步+并发+ 获得结果
     * <p> 使用线程池, 调用子线程会自动加入到线程池，并可以通过注入 AsyncThreadAop 获取结果, 需要在主线程执行完成前尽快使用</p>
     * <p>1. 子线程方法会有返回值, 并且通过 ThreadTimePolicy  策略来得到阻塞时机</p>
     * <p>2. 使用该策略 不能添加 @Async 注解</p>
     * <p>3 使用此策略会无视 ThreadTimePolicy 配置 </p>
     * <p><b>底层 FutureTask</b></p>
     */
    Future,

    /**
     * 自动异步+并行
     * <p>  使用线程池, 所有子线程加载完成后统一并发</p>
     * <p>1. 使用线程池, 所有子线程加载完成后统一并发</p>
     * <p>2. 使用该策略 不能添加 @Async 注解</p>
     * <p><b>底层 CompletableFuture </b></p>
     */
    AsyncParallel,

    /**
     * 自动异步 + 串行
     * <p> 使用线程池异步, 所有子线程依次执行</p>
     * <p>1. 返回最后一个方法值</p>
     * <p>2. 使用该策略 不能添加 @Async 注解</p>
     * <p><b>底层 CompletableFuture </b></p>
     */
    AsyncSerial,

    /**
     * 手动加 @Async + 调用直接开启线程
     * <p>1. 所有子线程依次建立线程(调用时直接建立线程)，有一个报错后续子任务不会执行</p>
     * <p>2. 需自行添加 @Async 注解，并且 调用方法的最后一个参数 传主线程 Thread.currentThread() </p>
     * <p>3. 主线程和子线程异步进行，多个子线程和主线程一旦存在异常，将会直接放行。然后根据回滚策略回滚。</p>
     * <p>4. 使用此策略会无视 ThreadTimePolicy 配置 </p>
     * <p><b>底层 CountDownLatch </b></p>
     */
    Step,


    /**
     * 自定义，需要实现ThreadAopHandler并注册到Spring容器
     */
    Custom,
}
