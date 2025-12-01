package top.cutexingluo.tools.aop.exception;

import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.function.Consumer;

/**
 * 历史遗留aop
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 16:12
 */
@Data
@Aspect
public class XTExceptionAop {


    public boolean printTrace = true;
    public Consumer<Throwable> exceptionHandler = null;

    public XTExceptionAop() {
    }

    public XTExceptionAop(boolean printTrace, Consumer<Throwable> exceptionHandler) {
        this.printTrace = printTrace;
        this.exceptionHandler = exceptionHandler;
    }

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
            if (exception == null || exception.wrong()) {
                if (exceptionHandler != null) exceptionHandler.accept(e);
                else if (printTrace) e.printStackTrace();
            }
            if (exception != null) {
                if (!exception.name().isEmpty()) System.out.println("发现异常: " + exception.name());
                if (!exception.desc().isEmpty()) System.out.println("异常描述: " + exception.desc());
            }
        }
        return result;
    }
}
