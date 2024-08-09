package top.cutexingluo.tools.basepackage.function;

/**
 * one object to other object
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/25 14:19
 * @since 1.1.2
 */
public interface Parser<S, T> {

    /**
     * parse source to target
     *
     * @param source source
     * @return target
     */
    T parse(S source) throws Exception;
}
