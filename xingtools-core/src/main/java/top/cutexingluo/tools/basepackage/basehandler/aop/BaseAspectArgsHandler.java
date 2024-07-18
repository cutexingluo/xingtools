package top.cutexingluo.tools.basepackage.basehandler.aop;


import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * 切面参数处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/5 14:14
 * @since 1.0.4
 */
public interface BaseAspectArgsHandler<T> extends BaseAspectHandler<T> {

    void before(T t, @Nullable Map<String, Object> args) throws Exception;

    void after(T t, @Nullable Map<String, Object> args) throws Exception;

    @Override
    default void before(T t) throws Exception {
        before(t, null);
    }

    @Override
    default void after(T t) throws Exception {
        after(t, null);
    }
}
