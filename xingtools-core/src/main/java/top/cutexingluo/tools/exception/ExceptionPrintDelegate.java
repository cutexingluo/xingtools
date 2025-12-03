package top.cutexingluo.tools.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.cutexingluo.tools.exception.base.AbstractExceptionDelegate;
import top.cutexingluo.tools.exception.base.ExceptionHandler;

import java.util.Arrays;
import java.util.List;


/**
 * 异常打印条件
 *
 * <p>默认优先处理器处理, 处理器不存在，可配置是否打印打印异常栈，系统错误流</p>
 *
 * <p>用来判断并处理打印的操作</p>
 * <p>未来可能移植到 xingcore</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/12/2 13:48
 * @since 1.2.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExceptionPrintDelegate<T extends Throwable> extends AbstractExceptionDelegate<T> {

    /**
     * 异常处理器
     */
    protected ExceptionHandler<T> exceptionHandler;

    /**
     * 是否打印异常栈
     */
    protected boolean printTrace = false;

    /**
     * 是否打印系统错误流
     */
    protected boolean printSystemErr = false;


    public ExceptionPrintDelegate(ExceptionHandler<T> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public ExceptionPrintDelegate(ExceptionHandler<T> exceptionHandler, boolean printTrace) {
        this.exceptionHandler = exceptionHandler;
        this.printTrace = printTrace;
    }

    public ExceptionPrintDelegate(ExceptionHandler<T> exceptionHandler, boolean printTrace, boolean printSystemErr) {
        this.exceptionHandler = exceptionHandler;
        this.printTrace = printTrace;
        this.printSystemErr = printSystemErr;
    }

    @Override
    public List<ExceptionHandler<T>> exceptionHandlers() {
        return  Arrays.asList(
                (e, o) -> {
                    if (exceptionHandler != null && exceptionHandler.support(e)) {
                        return exceptionHandler.apply(e, o);
                    }
                    return null;
                },
                (e, o) -> {
                    if (printTrace) {
                        e.printStackTrace();
                    }
                    return null;
                },
                (e, o) -> {
                    if (printSystemErr) {
                        System.err.println(e);
                    }
                    return null;
                }
        );
    }

    /**
     * 处理异常, 优先处理器处理, 处理器不存在, 后续根据配置打印异常栈, 系统错误流
     */
    @Override
    public Object handle(T e, Object otherArg) {
        return this.handleRetainFirst(e, otherArg);
    }
}
