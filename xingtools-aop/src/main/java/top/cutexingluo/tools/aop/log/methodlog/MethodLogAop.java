package top.cutexingluo.tools.aop.log.methodlog;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AnnotationUtils;
import top.cutexingluo.core.basepackage.basehandler.aop.BaseAspectHandler;
import top.cutexingluo.core.common.data.Entry;
import top.cutexingluo.core.common.data.PairEntry;
import top.cutexingluo.core.utils.se.character.XTStrUtil;
import top.cutexingluo.tools.aop.log.methodlog.custom.MethodLogAdapter;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectAroundHandler;
import top.cutexingluo.tools.exception.base.ExceptionDelegate;
import top.cutexingluo.tools.utils.log.handler.LogHandler;

import java.util.HashMap;
import java.util.Map;


/**
 * 方法日志AOP
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/16 14:58
 */
@Aspect
public class MethodLogAop implements BaseAspectAroundHandler<MethodLog>, BaseAspectHandler<Map<String,Object>> {


    protected MethodLogAdapter methodLogAdapter;

    public MethodLogAop() {
    }

    public MethodLogAop(MethodLogAdapter methodLogAdapter) {
        this.methodLogAdapter = methodLogAdapter;
    }

    public static final String[] printKeys = {
            "Before Msg", // 打印方法执行前msg
            "BusinessName", // 打印描述信息
            "Class Method", // 打印 method Name 全路径及执行方法
            "Args", // 打印参数
            "Return", // 打印返回数据
            "After Msg", // 打印方法执行完成msg
    };


    @Override
    @Around("@annotation(methodLog)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, MethodLog methodLog) throws Throwable {
        methodLog = AnnotationUtils.getAnnotation(methodLog, MethodLog.class);
        Object result = null;
        if (methodLog != null) {
            Map<String, Object> context = new HashMap<>();
            context.put("methodLog", methodLog);
            context.put("currentJoinPoint", joinPoint);
            LogHandler log = new LogHandler(methodLog.type().intCode());
            context.put("log", log);
            log.send("========== Start ==========");
            before(context);
            result = joinPoint.proceed();
            context.put("result", result);
            after(context);
            result = context.get("result");
            log.send("========== End ==========");
        } else {
            result = joinPoint.proceed();
        }

        return result;
    }

    protected String getStr(int index, String str) {
        String format = XTStrUtil.padRight(printKeys[index], 14) + ":  {}";
        return StrUtil.format(format, str);
    }


    @Override
    public void before(Map<String, Object> context) throws Exception {
        ProceedingJoinPoint  currentJoinPoint = (ProceedingJoinPoint) context.get("currentJoinPoint");
        MethodLog methodLog = (MethodLog) context.get("methodLog");
        LogHandler log = (LogHandler) context.get("log");


        if (StrUtil.isNotBlank(methodLog.beforeMsg())) {
            log.send(getStr(0, methodLog.beforeMsg()));
        }
        if (StrUtil.isNotBlank(methodLog.businessName())) {
            log.send(getStr(1, methodLog.businessName()));
        }
        // 执行自定义方法
        if (methodLogAdapter != null) {
            methodLogAdapter.beforeRun(currentJoinPoint, methodLog);
        }
        if (methodLog.showName() == NameType.Simple) {
            Signature signature = currentJoinPoint.getSignature();
            log.send(getStr(2, signature.getName()));
        } else if (methodLog.showName() == NameType.Full) {
            Signature signature = currentJoinPoint.getSignature();
            log.send(getStr(2, signature.getDeclaringTypeName() + ". " + signature.getName()));
        }
        if (methodLog.showArgs()) {
            log.send(getStr(3, JSONUtil.toJsonStr(currentJoinPoint.getArgs())));
        }
    }

    @Override
    public void after(Map<String, Object> context) throws Exception {
        ProceedingJoinPoint  currentJoinPoint = (ProceedingJoinPoint) context.get("currentJoinPoint");
        MethodLog methodLog = (MethodLog) context.get("methodLog");
        LogHandler log = (LogHandler) context.get("log");
        Object result = context.get("result");

        // 执行自定义方法
        if (methodLogAdapter != null) {
            result = methodLogAdapter.afterRun(result);
            context.put("result", result);
        }
        if (methodLog.showReturn()) {
            log.send(getStr(4, JSONUtil.toJsonStr(result)));
        }
        if (StrUtil.isNotBlank(methodLog.afterMsg())) {
            log.send(getStr(5, methodLog.afterMsg()));
        }
    }
}
