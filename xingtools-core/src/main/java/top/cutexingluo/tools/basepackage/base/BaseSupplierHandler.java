package top.cutexingluo.tools.basepackage.base;

import java.util.function.Supplier;

/**
 * 一元装饰接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 22:36
 */
@FunctionalInterface
public interface BaseSupplierHandler<O> {
    /**
     * 对 Supplier 进行操作
     */
    <T> O decorate(Supplier<T> supplier);
}
