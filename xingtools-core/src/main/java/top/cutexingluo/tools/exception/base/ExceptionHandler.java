package top.cutexingluo.tools.exception.base;


import java.util.function.BiFunction;

/**
 * 异常处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/12/2 13:58
 * @since 1.2.1
 */
public interface ExceptionHandler<T extends Throwable> extends BiFunction<T,Object,Object> {

    /**
     * 是否支持该异常
     *
     * @param throwable 异常
     * @return 是否支持
     */
    default boolean support(T throwable){
        return true;
    }
}
