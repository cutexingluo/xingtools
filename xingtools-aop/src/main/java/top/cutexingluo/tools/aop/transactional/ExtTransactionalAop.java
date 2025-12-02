package top.cutexingluo.tools.aop.transactional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.transaction.TransactionStatus;
import top.cutexingluo.tools.exception.base.ExceptionDelegate;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * 事务异常拦截，回滚
 * <p>需要导入 spring-tx 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2022/11/22 19:27
 */

@Aspect
//@Component
public class ExtTransactionalAop {

    TransactionalUtils transactionalUtils;

    ExceptionDelegate<Throwable> exceptionDelegate;


    public ExtTransactionalAop(TransactionalUtils transactionalUtils) {
        this.transactionalUtils = transactionalUtils;
    }

    public ExtTransactionalAop(TransactionalUtils transactionalUtils, ExceptionDelegate<Throwable> exceptionDelegate) {
        this.transactionalUtils = transactionalUtils;
        this.exceptionDelegate = exceptionDelegate;
    }

    @Around("@annotation(top.cutexingluo.tools.aop.transactional.ExtTransactional)")
    public Object around(ProceedingJoinPoint joinPoint) {
        TransactionStatus begin = null;
        try {
            begin = transactionalUtils.begin();
            joinPoint.proceed();
            transactionalUtils.commit(begin);
        } catch (Throwable e) {
            if (begin != null) transactionalUtils.rollback(begin);
            if (exceptionDelegate != null) exceptionDelegate.handle(e, Arrays.asList(joinPoint, transactionalUtils, begin));
        }
        return null;
    }
}
