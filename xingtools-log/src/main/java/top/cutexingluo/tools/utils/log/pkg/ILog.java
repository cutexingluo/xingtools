package top.cutexingluo.tools.utils.log.pkg;

/**
 * log 对象接口，用于获取不同版本的 log
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/10 11:47
 * @since 1.0.4
 */
@FunctionalInterface
public interface ILog<T> {

    T getLog();

    default Class<?> getLogClass() {
        return getLog().getClass();
    }
}
