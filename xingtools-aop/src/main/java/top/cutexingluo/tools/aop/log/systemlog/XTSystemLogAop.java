package top.cutexingluo.tools.aop.log.systemlog;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.cutexingluo.core.basepackage.basehandler.aop.BaseAspectHandler;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectAroundHandler;
import top.cutexingluo.tools.utils.log.handler.LogHandler;

import java.util.HashMap;
import java.util.Map;


/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/27 9:08
 */
@Aspect
public class XTSystemLogAop implements BaseAspectHandler<Map<String,Object>>, BaseAspectAroundHandler<XTSystemLog> {

    public static final String[] printKeys = {
            "URL", // 请求url
            "BusinessName", // 打印描述信息
            "HTTP Method", // 打印HTTP method
            "Class Method", // 打印 controller全路径及执行方法
            "IP", // 打印IP地址
            "Request Args", // 打印请求参数
            "Response", // 打印响应数据
    };

    protected ObjectMapper objectMapper;

    public XTSystemLogAop() {
        objectMapper = new ObjectMapper();
    }

    public XTSystemLogAop(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @Around("@annotation(xtSystemLog)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, XTSystemLog xtSystemLog) throws Throwable {
        xtSystemLog = AnnotationUtils.getAnnotation(xtSystemLog, XTSystemLog.class);

        Object result = null;
        if(xtSystemLog != null){
            Map<String, Object> context = new HashMap<>();
            context.put("currentJoinPoint", joinPoint);
            context.put("xtSystemLog", xtSystemLog);
            Signature signature = joinPoint.getSignature();
            context.put("signature", signature);
            LogHandler log = new LogHandler(xtSystemLog.type().intCode());
            context.put("log", log);
            if (xtSystemLog.enableStartAndEnd()) log.send("========== Start ==========");
            try {
                before(context);
                result = joinPoint.proceed();
                after(context);
            } finally {
                if (xtSystemLog.enableStartAndEnd()) log.send("========== End ==========");
            }
        }else {
            result = joinPoint.proceed();
        }

        return result;
    }


    @Override
    public void before(Map<String, Object> context) throws Exception {
        // 从 context 获取当前请求的信息
        ProceedingJoinPoint currentJoinPoint = (ProceedingJoinPoint) context.get("currentJoinPoint");
        Signature signature = (Signature) context.get("signature");
        XTSystemLog currentSystemLog = (XTSystemLog) context.get("xtSystemLog");
        LogHandler log = (LogHandler) context.get("log");

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            log.send(padRight(printKeys[0]) + ":  " + attributes.getRequest().getRequestURL());
            log.send(padRight(printKeys[1]) + ":  " +currentSystemLog.businessName());
            log.send(padRight(printKeys[2]) + ":  "+ attributes.getRequest().getMethod());
            log.send(padRight(printKeys[3]) + ":  " + signature.getDeclaringTypeName() +". "+ signature.getName());
            log.send(padRight(printKeys[4]) + ":  " + attributes.getRequest().getRemoteHost());
            if (currentSystemLog.showRequestArgs())
                log.send(padRight(printKeys[5]) + ":  " + objectMapper.writeValueAsString(currentJoinPoint.getArgs()));
        }
    }

    @Override
    public void after(Map<String, Object> context) throws Exception {
        // 从 context 获取当前请求的信息
        ProceedingJoinPoint currentJoinPoint = (ProceedingJoinPoint) context.get("currentJoinPoint");
        XTSystemLog currentSystemLog = (XTSystemLog) context.get("xtSystemLog");
        LogHandler log = (LogHandler) context.get("log");

        Object result = currentJoinPoint.getArgs()[0];

        if (currentSystemLog.showResponseArgs())
            log.send(padRight(printKeys[6]) + ":  "+ objectMapper.writeValueAsString(result));
    }


    protected static String padRight(String inputString) {
        return String.format("%-" + 14 + "s", inputString);
    }
}
