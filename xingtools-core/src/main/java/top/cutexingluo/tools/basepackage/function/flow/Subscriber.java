package top.cutexingluo.tools.basepackage.function.flow;

/**
 * 订阅者
 *
 * <p>详见 jdk9  Flow.Subscriber</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/1 16:13
 * @since 1.1.2
 */
public interface Subscriber<T> {

    void onSubscribe(Subscription subscription);

    void onNext(T item);

    void onError(Throwable throwable);


    void onComplete();
}
