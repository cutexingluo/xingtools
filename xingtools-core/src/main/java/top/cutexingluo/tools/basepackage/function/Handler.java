package top.cutexingluo.tools.basepackage.function;

/**
 * 处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/25 15:42
 * @since 1.0.5
 */
@FunctionalInterface
public interface Handler<E> {
    void handle(E event);
}
