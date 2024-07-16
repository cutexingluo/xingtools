package top.cutexingluo.tools.basepackage.base;

/**
 * <p>扩展 可初始化的 接口</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 9:56
 * @since  1.1.1
 */
public interface ExtInitializable<T> {

    /**
     * 调用此方法可初始化
     * <p>启用此名防止与 Initializable 冲突</p>
     * @return  T - 多是初始化的对象/自身
     */
    T initSelf();
}
