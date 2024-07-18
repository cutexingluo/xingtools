package top.cutexingluo.tools.basepackage.base;


import java.util.concurrent.Callable;

/**
 * 一元装饰接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 17:44
 */
@FunctionalInterface
public interface BaseCallableHandler<O> {
    /**
     * 对 Callable 进行操作
     */
    <T> O decorate(Callable<T> callable);
}
