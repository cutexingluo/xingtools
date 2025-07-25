package top.cutexingluo.tools.common.base;

/**
 * get the value
 * <p>得到数据</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 11:35
 * @since 1.1.4
 */
@FunctionalInterface
public interface IValue<V> {

    /**
     * get the value
     * <p>得到数据</p>
     *
     * @return the value
     */
    V getValue();
}
