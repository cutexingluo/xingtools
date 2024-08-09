package top.cutexingluo.tools.basepackage.base;

/**
 * 检查初始化接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/25 16:38
 * @since 1.1.2
 */
public interface Checkable<T> {


    /**
     * 检查初始化
     *
     * <p>对参数进行初始化检查</p>
     * <p>1.可以不用在构造方法里面检验属性，在外部调用该方法，保证构造方法的完整性和可移植性</p>
     * <p>2.也可以直接使用在构造方法里，外部就无需调用了</p>
     */
    T checkInit();
}
