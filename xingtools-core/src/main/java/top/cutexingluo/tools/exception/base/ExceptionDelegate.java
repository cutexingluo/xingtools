package top.cutexingluo.tools.exception.base;

import java.util.List;

/**
 * 异常处理委托
 *
 * <p>用来判断并处理异常的操作</p>
 * <p>未来可能移植到 xingcore</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/12/2 13:52
 * @since 1.2.1
 */
public interface ExceptionDelegate<T extends Throwable> {


    /**
     * 异常处理函数器
     */
    default  List<ExceptionHandler<T>> exceptionHandlers(){
        return null;
    }


    /**
     * 异常处理函数
     *
     * <p>默认只处理，不返回结果</p>
     *
     * @param e        异常
     * @param otherArg 其他参数
     * @return 处理后的结果
     */
     Object handle(T e,Object otherArg);

}
