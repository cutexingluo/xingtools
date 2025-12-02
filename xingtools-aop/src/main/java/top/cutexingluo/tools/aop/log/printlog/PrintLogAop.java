package top.cutexingluo.tools.aop.log.printlog;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import top.cutexingluo.core.basepackage.basehandler.aop.BaseAspectHandler;
import top.cutexingluo.tools.aop.log.printlog.custom.PrintLogAdapter;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectAroundHandler;
import top.cutexingluo.tools.utils.log.handler.LogHandler;

/**
 * PrintLog 的 Aop
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2022/11/22 19:27
 */
@Aspect
public class PrintLogAop implements BaseAspectHandler<PrintLog>, BaseAspectAroundHandler<PrintLog> {

    @Autowired(required = false)
    protected PrintLogAdapter printLogAdapter;

    protected LogHandler log;
    protected ProceedingJoinPoint currentJoinPoint;
    protected Object result = null;


    @Override
//    @Before("@annotation(printLog)")
    public void before(PrintLog printLog) {
        if (StrUtil.isNotBlank(printLog.before())) {
            log.send(printLog.before());
        }
        if (printLogAdapter != null) { //执行自定义方法
            printLogAdapter.beforeRun(currentJoinPoint, printLog);
        }
    }

    @Override
//    @After("@annotation(printLog)")
    public void after(PrintLog printLog) {
        if (printLogAdapter != null) { //执行自定义方法
            result = printLogAdapter.afterRun(result);
        }
        if (StrUtil.isNotBlank(printLog.after())) {
            log.send(printLog.after());
        }
    }

    @Override
    @Around("@annotation(printLog)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, PrintLog printLog) throws Throwable {
        printLog = AnnotationUtils.getAnnotation(printLog, PrintLog.class);
        result = null;
        if (printLog != null) {
            currentJoinPoint = joinPoint;
            log = new LogHandler(printLog.type().intCode());
            before(printLog);
            result = joinPoint.proceed();
            after(printLog);
        } else {
            result = joinPoint.proceed();
        }
        return result;
    }
}
