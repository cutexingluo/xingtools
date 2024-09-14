package top.cutexingluo.tools.common.opt;

import top.cutexingluo.tools.common.base.IValue;

/**
 * Optional 扩展类接口 (返回对象和类型)
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 11:33
 */
public interface IOptRes<V> extends IValue<V> {

    /**
     * 获取类型
     */
    Class<V> getClazz();
}
