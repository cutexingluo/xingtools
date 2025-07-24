package top.cutexingluo.tools.common.base;

/**
 * get the value and set the value
 * <p>得到数据 和 设置数据</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/6/6 14:25
 * @since 1.1.7
 */
public interface IValueSource<V> extends IValue<V> {

    /**
     * set the value
     * <p>设置数据</p>
     *
     * @param value the value
     * @return old value
     */
    V setValue(V value);
}
