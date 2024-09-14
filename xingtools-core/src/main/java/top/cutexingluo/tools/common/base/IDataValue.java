package top.cutexingluo.tools.common.base;

/**
 * Unified implementation of data methods, unifying the types of two interfaces
 * <p>统一实现数据方法，统一两种接口类型</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 11:39
 */
public interface IDataValue<V> extends IData<V>, IValue<V> {

    /**
     * @return the data ( the value )
     */
    @Override
    default V data() {
        return getValue();
    }
}
