package top.cutexingluo.tools.basepackage.function;

/**
 * generator
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/26 10:21
 */
public interface UniGenerator<T, R> {


    /**
     * generate R instance by T instance
     */
    R generate(T t);
}
