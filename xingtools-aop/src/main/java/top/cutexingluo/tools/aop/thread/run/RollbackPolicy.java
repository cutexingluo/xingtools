package top.cutexingluo.tools.aop.thread.run;

/**
 * 事务回滚策略
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/4 16:41
 * @since 1.0.2
 */
public enum RollbackPolicy {

    /**
     * 一个有错误，单独回滚
     */
    Single,

    /**
     * 一个有错误，全部回滚
     */
    Group,
}
