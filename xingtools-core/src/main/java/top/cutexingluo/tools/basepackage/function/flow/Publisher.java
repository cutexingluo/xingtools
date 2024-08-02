package top.cutexingluo.tools.basepackage.function.flow;

/**
 * 发布者
 *
 * <p>详见 jdk9  Flow.Publisher</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/1 16:13
 * @since 1.1.2
 */
public interface Publisher<T> {


    void subscribe(Subscriber<? super T> subscriber);
}
