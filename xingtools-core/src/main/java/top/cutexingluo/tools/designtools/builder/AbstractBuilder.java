package top.cutexingluo.tools.designtools.builder;

/**
 * Builder 抽象类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/9 10:43
 * @since 1.1.4
 */
public abstract class AbstractBuilder<T> implements Builder<T> {

    /**
     * the target
     */
    protected T target;


    @Override
    public T build() {
        return target;
    }
}
