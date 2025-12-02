package top.cutexingluo.tools.designtools.juc.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import top.cutexingluo.tools.designtools.method.ClassUtil;
import top.cutexingluo.tools.exception.base.ExceptionDelegate;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * 普通注解锁切面
 * <p>
 *
 * @author XingTian
 * @version 1.0.1
 * @update 2023-4-6
 * @date 2022-11-21
 */

@ConditionalOnClass({RedissonClient.class, Config.class})
@ConditionalOnBean(RedissonClient.class)
@Aspect
public class XTAopLockAop {

    RedissonClient redissonClient;

    ExceptionDelegate<Throwable> exceptionDelegate;

    public XTAopLockAop(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public XTAopLockAop(ExceptionDelegate<Throwable> exceptionDelegate, RedissonClient redissonClient) {
        this.exceptionDelegate = exceptionDelegate;
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(xtAopLock)")
    public Object around(ProceedingJoinPoint joinPoint, XTAopLock xtAopLock) {
        XTAopLock lockAnno = ClassUtil.getAnnotation(xtAopLock, XTAopLock.class);
        if (lockAnno == null) throw new RuntimeException("XTAopLock 注解发生错误");
        Object result = null;
        try {
            result = new XTAopLockHandler(lockAnno, redissonClient).lock(() -> {
                try {
                    return joinPoint.proceed();
                } catch (Throwable e) {
                    if (exceptionDelegate != null) {
                        exceptionDelegate.handle(e, Arrays.asList(joinPoint, lockAnno));
                    }
                }
                return null;
            });
        } catch (Exception e) {
            if (exceptionDelegate != null) {
                exceptionDelegate.handle(e, Arrays.asList(joinPoint, lockAnno));
            }
        }
        return result;
    }
}
