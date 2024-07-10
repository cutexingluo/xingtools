package top.cutexingluo.tools.basepackage.base;


import java.util.function.Supplier;

/**
 * 二元整合接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 22:30
 */
@FunctionalInterface
public interface BinSupplier<T> {
    Supplier<T> getSupplier(Supplier<T> now, Runnable after);
}
