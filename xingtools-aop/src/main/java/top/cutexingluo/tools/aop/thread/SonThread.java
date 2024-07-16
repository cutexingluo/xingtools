package top.cutexingluo.tools.aop.thread;


import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.TransactionDefinition;
import top.cutexingluo.tools.aop.thread.run.ThreadPolicy;
import top.cutexingluo.tools.designtools.juc.lock.extra.XTLockType;

import java.lang.annotation.*;

/**
 * 子线程
 * <p>若主线程没有加 @MainThread , 则失效</p>
 * <p>使用前需导入spring的 jdbc 包，并导入 dataSource 数据库依赖</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 15:20
 * @since 1.0.2
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SonThread {

    /**
     * 线程策略，默认Future。
     * <p>这里可以与主线程的不一致。</p>
     * <p>如果是 Custom 如果想要使用@Async 注解就必须改为 ThreadPolicy.Step</p>
     */
    ThreadPolicy policy() default ThreadPolicy.Future;

    //*********************** 线程 ***************************

    /**
     * 线程池的 bean 名称
     * <p>1. ThreadPolicy.Step 策略无需填该项，而需要添加@Async 并在该注解里填线程池</p>
     * <p>2.  其他策略如果不填，则自动在Spring里面查找Bean, 建议填</p>
     */
    @AliasFor("threadPoolName")
    String value() default "";

    /**
     * 线程池的 bean 名称
     * <p>1. ThreadPolicy.Step 策略无需填该项，而需要添加@Async 并在该注解里填线程池</p>
     * <p>2.  其他策略如果不填，则自动在Spring里面查找Bean, 建议填</p>
     */
    @AliasFor("value")
    String threadPoolName() default "";

//*********************** 事务 ***************************

    /**
     * 是否开启事务
     */
    boolean transaction() default false;

    /**
     * 事务隔离级别
     */
    int propagationBehavior() default TransactionDefinition.PROPAGATION_REQUIRES_NEW;


    //*********************** 锁 ***************************

    /**
     * <p>是否开启锁</p>
     * 简易锁类型，只适合简单的锁，其他自行添加
     */
    XTLockType lockType() default XTLockType.NonLock;

    /**
     * 锁名称,  仅分布式锁可用
     */
    String lockName() default "";

    /**
     * 是否公平, 仅本地锁可用
     */
    boolean isFair() default false;

    /**
     * 是否开启redissonClient ， 如果XTLockType 是 分布式锁类型则必须开启
     * <p>打开时需要开启 RedissonClient 配置，并需要进行相关配置</p>
     */
    boolean redisson() default false;

    // 由于Lock接口没有带 锁多长时间，所以这个只能适用于尝试获取锁。大于0则尝试获取，-1则直接锁

    /**
     * 尝试获取锁的时长，tryLock() ,默认-1, 单位 second
     * <p>如果为-1则直接阻塞获取锁，即lock()</p>
     */
    int tryTimeout() default -1;
}
