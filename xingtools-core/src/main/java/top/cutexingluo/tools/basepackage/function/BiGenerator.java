package top.cutexingluo.tools.basepackage.function;

/**
 * generator
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/26 10:25
 */
public interface BiGenerator<T, U, R> {

    /**
     * generate R instance by T and U instances
     */
    R generate(T t, U u);
}
