package top.cutexingluo.tools.designtools.builder;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 18:39
 */
public abstract class XTBuilder<T> implements Builder<T> {
    /**
     * the target
     */
    protected T target;

    @Override
    public Builder<T> getBuilder() {
        return this;
    }

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
