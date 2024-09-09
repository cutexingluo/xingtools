package top.cutexingluo.tools.designtools.builder;

/**
 * Builder 抽象类 (提供 set 方法)
 *
 * <p>于 1.1.4 继承 {@link AbstractBuilder}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 18:39
 */
public abstract class XTBuilder<T> extends AbstractBuilder<T> {


    /**
     * set the target
     */
    public Builder<T> setTarget(T target) {
        this.target = target;
        return this;
    }

    @Override
    public T build() {
        return target;
    }
}
