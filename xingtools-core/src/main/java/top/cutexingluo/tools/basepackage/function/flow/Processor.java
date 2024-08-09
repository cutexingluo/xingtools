package top.cutexingluo.tools.basepackage.function.flow;

/**
 * 处理器
 *
 * <p>详见 jdk9  Flow.Processor</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/1 16:15
 * @since 1.1.2
 */
public interface Processor<T, R> extends Subscriber<T>, Publisher<R> {
}
