package top.cutexingluo.tools.basepackage.base;

/**
 * 刷新接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/26 15:56
 */
public interface Refreshable<T> {

    /**
     * 刷新同步数值
     *
     * <p>在执行重要方法时前，调用该方法同步数值</p>
     */
    T refresh();
}
