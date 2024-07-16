package top.cutexingluo.tools.aop.systemlog;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.cutexingluo.tools.basepackage.basehandler.BaseAroundHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/27 9:08
 */
@Aspect
@Slf4j
public class XTSystemLogAop implements BaseAroundHandler {


    public static final String[] printKeys = {
            "URL", // 请求url
            "BusinessName", // 打印描述信息
            "HTTP Method", // 打印HTTP method
            "Class Method", // 打印 controller全路径及执行方法
            "IP", // 打印IP地址
            "Request Args", // 打印请求参数
            "Response", // 打印响应数据
    };

    public static int length = maxLength();


    protected XTSystemLog currentSystemLog = null;
    protected ProceedingJoinPoint currentJoinPoint = null;
    protected Signature signature = null;
    protected Object result = null;

    @Around("@annotation(xtSystemLog)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, XTSystemLog xtSystemLog) throws Throwable {
        xtSystemLog = AnnotationUtils.getAnnotation(xtSystemLog, XTSystemLog.class);
        currentSystemLog = xtSystemLog;
        currentJoinPoint = joinPoint;
        signature = joinPoint.getSignature();
        result = null;
//        System.out.println(joinPoint.getSignature().getName());
        log.info("========== Start ==========");
        try {
            if (xtSystemLog != null) handleBefore();
            result = joinPoint.proceed();
            if (xtSystemLog != null) handleAfter();
        } finally {
            log.info("========== End ==========");
        }
        return result;
    }

    @Override
    public Object handleBefore() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            log.info(padRight(printKeys[0], length + 2) + ":  {}", request.getRequestURL());
            log.info(padRight(printKeys[1], length + 2) + ":  {}", currentSystemLog.businessName());
            log.info(padRight(printKeys[2], length + 2) + ":  {}", request.getMethod());
            log.info(padRight(printKeys[3], length + 2) + ":  {}. {}", signature.getDeclaringTypeName(), signature.getName());
            log.info(padRight(printKeys[4], length + 2) + ":  {}", request.getRemoteHost());
            if (currentSystemLog.showRequestArgs())
                log.info(padRight(printKeys[5], length + 2) + ":  {}", JSONUtil.toJsonStr(currentJoinPoint.getArgs()));
        }
        return null;
    }


    @Override
    public Object handleAfter() {
        if (currentSystemLog.showResponseArgs())
            log.info(padRight(printKeys[6], length + 2) + ":  {}", JSONUtil.toJsonStr(result));
        return null;
    }


    protected static String padRight(String inputString, int length) {
        return String.format("%-" + length + "s", inputString);
    }

    protected static int maxLength() {
        int maxLength = 0;
        for (String key : printKeys) {
            maxLength = Math.max(maxLength, key.length());
        }
        return maxLength;
    }
}
