package top.cutexingluo.tools.aop.log.methodlog;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import top.cutexingluo.tools.aop.log.methodlog.custom.MethodLogAdapter;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectAroundHandler;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectHandler;
import top.cutexingluo.tools.utils.log.handler.LogHandler;
import top.cutexingluo.tools.utils.se.character.XTStrUtil;


/**
 * 方法日志AOP
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/16 14:58
 */
@Aspect
public class MethodLogAop implements BaseAspectAroundHandler<MethodLog>, BaseAspectHandler<MethodLog> {

    @Autowired(required = false)
    protected MethodLogAdapter methodLogAdapter;

    public static final String[] printKeys = {
            "Before Msg", // 打印方法执行前msg
            "BusinessName", // 打印描述信息
            "Class Method", // 打印 method Name 全路径及执行方法
            "Args", // 打印参数
            "Return", // 打印返回数据
            "After Msg", // 打印方法执行完成msg
    };

    public static int keyLen = XTStrUtil.getMaxLength(printKeys);
    protected LogHandler log;
    protected ProceedingJoinPoint currentJoinPoint = null;
    protected Object result = null;


    @Override
    @Around("@annotation(methodLog)")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, MethodLog methodLog) throws Throwable {
        methodLog = AnnotationUtils.getAnnotation(methodLog, MethodLog.class);
        result = null;
        if (methodLog != null) {
            currentJoinPoint = joinPoint;
            log = new LogHandler(methodLog.type().intCode());
            log.send("========== Start ==========");
            before(methodLog);
            result = joinPoint.proceed();
            after(methodLog);
            log.send("========== End ==========");
        } else {
            result = joinPoint.proceed();
        }

        return result;
    }

    protected String getStr(int index, String str) {
        String format = XTStrUtil.padRight(printKeys[index], keyLen + 2) + ":  {}";
        return StrUtil.format(format, str);
    }

    @Override
    public void before(MethodLog methodLog) throws Exception {
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
    public void after(MethodLog methodLog) throws Exception {
        // 执行自定义方法
        if (methodLogAdapter != null) {
            result = methodLogAdapter.afterRun(result);
        }
        if (methodLog.showReturn()) {
            log.send(getStr(4, JSONUtil.toJsonStr(result)));
        }
        if (StrUtil.isNotBlank(methodLog.afterMsg())) {
            log.send(getStr(5, methodLog.afterMsg()));
        }
    }
}
