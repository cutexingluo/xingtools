package top.cutexingluo.tools.basepackage.basehandler;

import top.cutexingluo.tools.basepackage.base.BaseSupplierHandler;

import java.util.function.Supplier;

/**
 * 一元装饰接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 22:35
 * @since 1.0.2
 */
@FunctionalInterface
public interface SupplierHandler extends BaseSupplierHandler<Supplier> {
    /**
     * 对 Supplier 进行操作
     */
    @Override
    <T> Supplier<T> decorate(Supplier<T> supplier);
}
