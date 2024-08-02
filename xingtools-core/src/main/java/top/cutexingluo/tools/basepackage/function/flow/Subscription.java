package top.cutexingluo.tools.basepackage.function.flow;

/**
 * 订阅关系
 *
 * <p>详见 jdk9  Flow.Subscription</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/1 16:15
 * @since 1.1.2
 */
public interface Subscription {

    void request(long n);

    void cancel();
}
