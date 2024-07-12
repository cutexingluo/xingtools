package top.cutexingluo.tools.aop.exception;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 16:12
 */
@Aspect
//@Component
public class XTExceptionAop {

//    @XTException(name = "内部错误", desc = "run方法内部错误")
//    public static void run(Runnable runnable) { //run带注释的接口
//        runnable.run();
//    }
//
//    @XTException(name = "内部错误", desc = "run方法内部错误", wrong = true)
//    public static void runTick(Runnable runnable) { //run带注释的接口输出系统错误
//        runnable.run();
//    }

    /**
     * 这里定义了一个总的匹配规则，以后拦截的时候直接拦截log()方法即可，无须去重复写execution表达式
     */
    @Pointcut("@annotation(XTException)") //拦截注解
    public void log() {
    }

    @Around("log()&&@annotation(exception)")
    public Object around(ProceedingJoinPoint joinPoint, XTException exception) {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            exception = AnnotationUtils.getAnnotation(exception, XTException.class);
            if (exception == null || exception.wrong()) e.printStackTrace();
            if (exception != null) {
                if (!exception.name().isEmpty()) System.out.println("发现异常: " + exception.name());
                if (!exception.desc().isEmpty()) System.out.println("异常描述: " + exception.desc());
            }
        }
        return result;
    }
}
