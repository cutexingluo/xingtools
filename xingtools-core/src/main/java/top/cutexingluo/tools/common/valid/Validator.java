package top.cutexingluo.tools.common.valid;

/**
 * 检验器接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/17 11:02
 * @since  1.1.1
 */
public interface Validator<T>{

    /**
     * 检验值是否合法
     * @return boolean 是否合法
     */
    boolean isValid(T value);
}
