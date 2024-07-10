package top.cutexingluo.tools.basepackage.basehandler;

import top.cutexingluo.tools.basepackage.base.BaseCallableHandler;

import java.util.concurrent.Callable;

/**
 * 一元装饰接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 17:46
 * @since 1.0.2
 */
@FunctionalInterface
public interface CallableHandler extends BaseCallableHandler<Callable> {
    /**
     * 对 Callable 进行操作
     */
    @Override
    <T> Callable<T> decorate(Callable<T> callable);
}
