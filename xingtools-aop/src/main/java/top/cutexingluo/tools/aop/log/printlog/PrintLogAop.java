package top.cutexingluo.tools.aop.log.printlog;

import cn.hutool.core.util.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AnnotationUtils;
import top.cutexingluo.core.basepackage.basehandler.aop.BaseAspectHandler;
import top.cutexingluo.tools.aop.log.printlog.custom.PrintLogAdapter;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectAroundHandler;
import top.cutexingluo.tools.utils.log.handler.LogHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * PrintLog 的 Aop
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2022/11/22 19:27
 */
@Aspect
public class PrintLogAop implements BaseAspectHandler<Map<String,Object>>, BaseAspectAroundHandler<PrintLog> {

    protected PrintLogAdapter printLogAdapter;


    public PrintLogAop() {
    }

    public PrintLogAop(PrintLogAdapter printLogAdapter) {
        this.printLogAdapter = printLogAdapter;
    }


    @Override
    @Around("@annotation(printLog)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, PrintLog printLog) throws Throwable {
        printLog = AnnotationUtils.getAnnotation(printLog, PrintLog.class);
        Object result = null;
        if (printLog != null) {
            Map<String, Object> context = new HashMap<>();
            context.put("currentJoinPoint", joinPoint);
            context.put("printLog", printLog);

            LogHandler log = new LogHandler(printLog.type().intCode());
            context.put("log", log);
            before(context);
            result = joinPoint.proceed();
            context.put("result", result);
            after(context);
            result = context.get("result");
        } else {
            result = joinPoint.proceed();
        }
        return result;
    }

    @Override
    public void before(Map<String, Object> context) throws Exception {
        ProceedingJoinPoint currentJoinPoint = (ProceedingJoinPoint) context.get("currentJoinPoint");
        PrintLog printLog = (PrintLog) context.get("printLog");
        LogHandler log = (LogHandler) context.get("log");

        if (StrUtil.isNotBlank(printLog.before())) {
            log.send(printLog.before());
        }
        if (printLogAdapter != null) { //执行自定义方法
            printLogAdapter.beforeRun(currentJoinPoint, printLog);
        }
    }

    @Override
    public void after(Map<String, Object> context) throws Exception {
        PrintLog printLog = (PrintLog) context.get("printLog");
        LogHandler log = (LogHandler) context.get("log");
        Object result = context.get("result");

        if (printLogAdapter != null) { //执行自定义方法
            result = printLogAdapter.afterRun(result);
            context.put("result", result);
        }
        if (StrUtil.isNotBlank(printLog.after())) {
            log.send(printLog.after());
        }
    }
}
