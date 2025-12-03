package top.cutexingluo.tools.exception;


import lombok.extern.slf4j.Slf4j;
import top.cutexingluo.tools.exception.base.ExceptionHandler;

/**
 * 异常日志处理委托
 *
 * <p>需要引入 slf4j 依赖</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/12/3 09:27
 * @since 1.2.1
 */
@Slf4j
public class ExceptionLogDelegate<T extends Throwable> extends ExceptionPrintDelegate<T> {

    public static final ExceptionHandler<Throwable> DEFAULT_LOG = (throwable, o) -> {
        log.error("ExceptionLogDelegate Message: {}", throwable.getMessage());
        return null;
    };

    public static final ExceptionHandler<Throwable> FULL_LOG = (throwable, o) -> {
        log.error("ExceptionLogDelegate Full Message: ", throwable);
        return null;
    };


    public ExceptionLogDelegate(ExceptionHandler<T> exceptionHandler) {
        super(exceptionHandler);
    }

    public ExceptionLogDelegate(ExceptionHandler<T> exceptionHandler, boolean printTrace) {
        super(exceptionHandler, printTrace);
    }

    public ExceptionLogDelegate(ExceptionHandler<T> exceptionHandler, boolean printTrace, boolean printSystemErr) {
        super(exceptionHandler, printTrace, printSystemErr);
    }


    public static  ExceptionLogDelegate<Throwable> create(boolean fullLog) {
        return new ExceptionLogDelegate<>(fullLog ? FULL_LOG : DEFAULT_LOG);
    }
}
