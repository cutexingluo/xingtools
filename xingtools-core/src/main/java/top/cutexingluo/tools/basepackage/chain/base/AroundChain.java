package top.cutexingluo.tools.basepackage.chain.base;

import java.util.function.Consumer;

/**
 * 围绕执行链
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/5 18:07
 * @since 1.1.4
 */
@FunctionalInterface
public interface AroundChain<T> extends Consumer<T> {

    /**
     * 执行过滤器
     */
    void doFilter(T source);

    /**
     * 执行过滤器
     * <p>默认直接调用 doFilter </p>
     */
    @Override
    default void accept(T t) {
        doFilter(t);
    }
}
