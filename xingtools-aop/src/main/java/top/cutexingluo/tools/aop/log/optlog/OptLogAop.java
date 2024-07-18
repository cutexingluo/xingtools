package top.cutexingluo.tools.aop.log.optlog;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import top.cutexingluo.tools.aop.log.optlog.custom.OptLogAdapter;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectAroundHandler;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectHandler;
import top.cutexingluo.tools.utils.log.handler.LogHandler;

/**
 * 操作日志aop
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2022/11/22 19:27
 */
@Aspect
public class OptLogAop implements BaseAspectHandler<OptLog>, BaseAspectAroundHandler<OptLog> {

    @Autowired(required = false)
    protected OptLogAdapter optLogAdapter;

    protected LogHandler log;
    protected ProceedingJoinPoint currentJoinPoint;
    protected Object result = null;


    protected OptConfig optConfig;


    @Override
    public void before(OptLog printLog) {
        if (StrUtil.isNotBlank(printLog.before())) {
            log.send(printLog.before());
        }
        if (optLogAdapter != null) { //执行自定义方法
            optConfig = optLogAdapter.beforeRun(currentJoinPoint, (MethodSignature) currentJoinPoint.getSignature(), printLog, optConfig);
        }
    }

    @Override
    public void after(OptLog printLog) {
        if (StrUtil.isNotBlank(printLog.after())) {
            log.send(printLog.after());
        }
    }

    @Override
    @Around("@annotation(optLog)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, OptLog optLog) throws Throwable {
        optLog = AnnotationUtils.getAnnotation(optLog, OptLog.class);
        result = null;
        if (optLog != null) {
            currentJoinPoint = joinPoint;
            log = new LogHandler(optLog.type().intCode());
            optConfig = new OptConfig();
            before(optLog);
            if (optLogAdapter != null) { //执行自定义方法
                if (optConfig.enabled) {
                    if (optConfig.returnNull) return null;
                    if (optConfig.returnValue != null) return optConfig.returnValue;
                    result = optLogAdapter.runBody(joinPoint, optLog);
                    result = optLogAdapter.afterRun(result);
                }
            } else {
                result = joinPoint.proceed();
            }
            after(optLog);
        } else {
            result = joinPoint.proceed();
        }
        return result;
    }
}
