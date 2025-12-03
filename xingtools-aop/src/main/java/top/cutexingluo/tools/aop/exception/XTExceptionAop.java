package top.cutexingluo.tools.aop.exception;

import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;
import top.cutexingluo.tools.exception.ExceptionPrintDelegate;
import top.cutexingluo.tools.exception.base.ExceptionDelegate;
import top.cutexingluo.tools.utils.log.handler.LogHandler;

import java.util.Arrays;

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

    /**
     * 异常处理，不提供则默认使用log 打印异常
     */
    protected ExceptionDelegate<Throwable> exceptionDelegate;


    public XTExceptionAop() {
    }

    public XTExceptionAop(ExceptionDelegate<Throwable> exceptionDelegate) {
        this.exceptionDelegate = exceptionDelegate;
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
            if(exception !=null){
                LogHandler log = new LogHandler(exception.logType().intCode());
                if(exception.wrong()){
                    if (exceptionDelegate != null) exceptionDelegate.handle(e, Arrays.asList(joinPoint, exception));
                    else{
                        new ExceptionPrintDelegate<>((throwable,list)->{
                            log.send( throwable.getMessage());
                            return null;
                        }).handle(e, Arrays.asList(joinPoint, exception));
                    }
                }else{
                    if (!exception.name().isEmpty()) log.send("发现异常: " + exception.name());
                    if (!exception.desc().isEmpty()) log.send("异常描述: " + exception.desc());
                }
            }
        }
        return result;
    }
}
