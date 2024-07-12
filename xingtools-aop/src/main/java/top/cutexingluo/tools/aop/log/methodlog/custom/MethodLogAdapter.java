package top.cutexingluo.tools.aop.log.methodlog.custom;

import org.aspectj.lang.ProceedingJoinPoint;
import top.cutexingluo.tools.aop.log.methodlog.MethodLog;

/**
 * 方法Log适配器
 * <p>可以通过实现该接口，并注册到容器，从而实现对aop的扩展</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/18 16:03
 */
@FunctionalInterface
public interface MethodLogAdapter {
    /**
     * 方法之前执行
     *
     * @param joinPoint 连接点
     * @param methodLog 方法记录，里面可提出key值
     */
    void beforeRun(ProceedingJoinPoint joinPoint, MethodLog methodLog);

    /**
     * 方法之后执行，返回方法值
     *
     * @param result 结果
     * @return {@link Object}
     */
    default Object afterRun(Object result) {
        return result;
    }
}
