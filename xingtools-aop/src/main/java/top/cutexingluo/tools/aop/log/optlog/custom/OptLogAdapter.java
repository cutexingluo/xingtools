package top.cutexingluo.tools.aop.log.optlog.custom;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import top.cutexingluo.tools.aop.log.optlog.OptConfig;
import top.cutexingluo.tools.aop.log.optlog.OptLog;

/**
 * 操作日志适配器，自定义注解操作
 * <p>可以通过实现该接口，并注册到容器，从而实现对aop的扩展</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/18 16:26
 */
@FunctionalInterface
public interface OptLogAdapter {


    /**
     * 在运行之前, 设置配置
     *
     * @param joinPoint 连接点
     * @param optLog    操作日志
     * @param optConfig 操作配置，可自定义操作
     */
    OptConfig beforeRun(ProceedingJoinPoint joinPoint, MethodSignature methodSignature, OptLog optLog, OptConfig optConfig);


    /**
     * 方法执行体，手动执行方法
     *
     * @param joinPoint 连接点
     * @param optLog    选择日志
     * @return {@link Object}
     * @throws Throwable throwable
     */
    default Object runBody(ProceedingJoinPoint joinPoint, OptLog optLog) throws Throwable {
        return joinPoint.proceed();
    }

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
