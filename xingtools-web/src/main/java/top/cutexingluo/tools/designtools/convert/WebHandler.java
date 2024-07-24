package top.cutexingluo.tools.designtools.convert;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.bridge.servlet.HttpServletRequestData;

import java.lang.reflect.Method;
import java.util.function.BiFunction;

/**
 * Web 处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/13 13:16
 * @since 1.0.4
 */
public interface WebHandler extends BiFunction<Method, HttpServletRequestData, String> {

    /**
     * 使用 bundle 的数据
     *
     * @since 1.1.1
     */
    default String apply(@NotNull AspectBundle bundle) {
        return this.apply(bundle.getMethod(), bundle.getRequest());
    }
}
