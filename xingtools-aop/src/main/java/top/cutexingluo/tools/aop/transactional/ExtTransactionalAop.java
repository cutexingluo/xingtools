package top.cutexingluo.tools.aop.transactional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;

/**
 * 事务异常拦截，回滚
 * <p>需要导入 spring-tx 包</p>
 *
 * @author  XingTian
 * @date 2022/11/22 19:27
 * @version 1.0.0
 */

@Aspect
//@Component
public class ExtTransactionalAop {
    @Autowired
    TransactionalUtils transactionalUtils;

    @Around("@annotation(top.cutexingluo.tools.aop.transactional.ExtTransactional)")
    public Object around(ProceedingJoinPoint joinPoint) {
        TransactionStatus begin = null;
        try {
            begin = transactionalUtils.begin();
            joinPoint.proceed();
            transactionalUtils.commit(begin);
        } catch (Throwable e) {
            if (begin != null)  transactionalUtils.rollback(begin);
            e.printStackTrace();
        }
        return null;
    }
}
