package top.cutexingluo.tools.exception;

/**
 * <p>不建议使用</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/2 16:24
 */
public class ExceptionUtil {

    /**
     * 转化为Exception
     */
    public static <T extends Throwable> Exception toException(T throwable) {
        if (throwable instanceof Exception) return (Exception) throwable;
        else return new Exception(throwable);
    }
}
