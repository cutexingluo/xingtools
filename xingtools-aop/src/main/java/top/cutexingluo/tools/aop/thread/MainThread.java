package top.cutexingluo.tools.aop.thread;

import org.springframework.core.annotation.AliasFor;
import top.cutexingluo.tools.aop.thread.run.RollbackPolicy;
import top.cutexingluo.tools.aop.thread.run.ThreadPolicy;
import top.cutexingluo.tools.aop.thread.run.ThreadTimePolicy;

import java.lang.annotation.*;

/**
 * 主线程
 * <p>需配合@SonThread 子线程</p>
 * <p>使用前需导入spring的 jdbc 包，并导入 dataSource 数据库依赖</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 15:00
 * @since 1.0.2
 */
@Inherited
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MainThread {

    /**
     * 自定义策略时，实现 AsyncThreadAop 的 bean 名称
     */
    String customAopBean() default "";

    /**
     * 子线程数量（满足并执行的数量）
     * <p>1. Async前缀的策略，该值必须小于等于实际调用子线程数量。若小于，则每达到该数量执行，所以要求必须整除，否则余下的线程不会执行</p>
     * <p>2. Step策略，该值必须等于实际调用子线程数量。若大于，则阻塞。若小于则余下的不会执行</p>
     */
    @AliasFor("sonThreadNum")
    int value() default 0;

    /**
     * 子线程数量（满足并执行的数量）
     * <p>1. Async前缀的策略，该值必须小于等于实际调用子线程数量。若小于，则每达到该数量执行，所以要求必须整除，否则余下的线程不会执行</p>
     * <p>2. Step策略，该值必须等于实际调用子线程数量若大于，则阻塞。若小于则余下的不会执行</p>
     */
    @AliasFor("value")
    int sonThreadNum() default 0;


    /**
     * 线程策略，默认Future
     * <p>需要和子线程注解上保持一致</p>
     */
    ThreadPolicy policy() default ThreadPolicy.Future;


    /**
     * 线程执行时机策略，默认最后一个子线程加载
     */
    ThreadTimePolicy startTime() default ThreadTimePolicy.GetResultAfterLastSon;

    /**
     * 线程事务回滚策略，默认单独回滚
     */
    RollbackPolicy rollbackPolicy() default RollbackPolicy.Single;
}