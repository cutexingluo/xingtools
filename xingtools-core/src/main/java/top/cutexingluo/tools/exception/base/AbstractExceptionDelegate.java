package top.cutexingluo.tools.exception.base;

/**
 * 异常处理委托
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/12/2 14:20
 */
public abstract class AbstractExceptionDelegate<T extends Throwable> implements ExceptionDelegate<T> {


    /**
     * 默认处理忽略返回值
     */
    @Override
    public Object handle(T e, Object otherArg) {
        return this.handleIgnoreRet(e,otherArg);
    }

    /**
     * 处理异常，全部处理，忽略返回值
     *
     * @param e 异常
     * @param otherArg 其他参数
     * @return 处理结果
     */
    public Object handleIgnoreRet(T e, Object otherArg){
        if(exceptionHandlers() != null){
            for (ExceptionHandler<T> handler : exceptionHandlers()) {
                if(handler != null && handler.support(e) ){
                    handler.apply(e, otherArg);
                }
            }
        }
        return null;
    }

    /**
     * 处理异常，第一个有返回值的返回，直接返回处理结果，后续处理忽略
     *
     * @param e 异常
     * @param otherArg 其他参数
     * @return 处理结果
     */
    public Object handleStopFirst(T e, Object otherArg){
        if(exceptionHandlers() != null){
            for (ExceptionHandler<T> handler : exceptionHandlers()) {
                if (handler != null && handler.support(e)) {
                    Object o = handler.apply(e, otherArg);
                    if (o != null) {
                        return o;
                    }
                }
            }
        }
        return null;
    }


    /**
     * 处理异常，所有处理全部执行，最后返回第一个处理结果
     *
     * @param e 异常
     * @param otherArg 其他参数
     * @return 处理结果
     */
    public Object handleRetainFirst(T e, Object otherArg){
        Object ret = null;
        if(exceptionHandlers() != null){
            for (ExceptionHandler<T> handler : exceptionHandlers()) {
                if (handler != null && handler.support(e)) {
                    Object o = handler.apply(e, otherArg);
                    if (o != null && ret == null) {
                        ret = o;
                    }
                }
            }
        }
        return ret;
    }


    /**
     * 处理异常，所有处理全部执行，最后返回最后处理结果
     *
     * @param e 异常
     * @param otherArg 其他参数
     * @return 处理结果
     */
    public Object handleRetainLast(T e, Object otherArg){
        Object ret = null;
        if(exceptionHandlers() != null){
            for (ExceptionHandler<T> handler : exceptionHandlers()) {
                if (handler != null && handler.support(e)) {
                    Object o = handler.apply(e, otherArg);
                    if (o != null) {
                        ret = o;
                    }
                }
            }
        }
        return ret;
    }

}
