package top.cutexingluo.tools.aop.log.optlog;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AnnotationUtils;
import top.cutexingluo.core.basepackage.basehandler.aop.BaseAspectHandler;
import top.cutexingluo.tools.aop.log.optlog.custom.OptLogAdapter;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectAroundHandler;
import top.cutexingluo.tools.utils.log.handler.LogHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志aop
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2022/11/22 19:27
 */
@Aspect
public class OptLogAop implements BaseAspectHandler<Map<String,Object>>, BaseAspectAroundHandler<OptLog> {

    protected OptLogAdapter optLogAdapter;


    public OptLogAop() {
    }

    public OptLogAop(OptLogAdapter optLogAdapter) {
        this.optLogAdapter = optLogAdapter;
    }


    @Override
    @Around("@annotation(optLog)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, OptLog optLog) throws Throwable {
        optLog = AnnotationUtils.getAnnotation(optLog, OptLog.class);
        Object result = null;
        if (optLog != null) {
            Map<String, Object> context = new HashMap<>();
            context.put("currentJoinPoint", joinPoint);
            context.put("optLog", optLog);
            LogHandler log = new LogHandler(optLog.type().intCode());
            context.put("log", log);
            OptConfig optConfig = new OptConfig();
            context.put("optConfig", optConfig);
            before(context);
            optConfig = (OptConfig) context.get("optConfig");
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
            after(context);
        } else {
            result = joinPoint.proceed();
        }
        return result;
    }

    @Override
    public void before(Map<String, Object> context) throws Exception {
        OptLog printLog = (OptLog) context.get("optLog");
        ProceedingJoinPoint currentJoinPoint = (ProceedingJoinPoint) context.get("currentJoinPoint");
        LogHandler log = (LogHandler) context.get("log");
        OptConfig optConfig = (OptConfig) context.get("optConfig");

        if (StrUtil.isNotBlank(printLog.before())) {
            log.send(printLog.before());
        }
        if (optLogAdapter != null) { //执行自定义方法
            optConfig = optLogAdapter.beforeRun(currentJoinPoint, (MethodSignature) currentJoinPoint.getSignature(), printLog, optConfig);
            context.put("optConfig", optConfig);
        }
    }

    @Override
    public void after(Map<String, Object> context) throws Exception {
        OptLog printLog = (OptLog) context.get("optLog");
        LogHandler log = (LogHandler) context.get("log");

        if (StrUtil.isNotBlank(printLog.after())) {
            log.send(printLog.after());
        }
    }
}
