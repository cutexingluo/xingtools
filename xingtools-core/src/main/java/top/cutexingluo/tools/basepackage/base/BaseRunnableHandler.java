package top.cutexingluo.tools.basepackage.base;

/**
 * 一元装饰接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 16:48
 */
@FunctionalInterface
public interface BaseRunnableHandler<T> {
    /**
     * 对 Runnable 进行操作
     */
    T decorate(Runnable runnable);
}
