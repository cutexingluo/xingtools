package top.cutexingluo.tools.common.base;

/**
 * IResult 数据接口
 * <p>* 返回数据接口</p>
 * <p>提供 getCode, getMsg 方法接口</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/13 20:36
 */
public interface IResultData<T> extends IR {
    /**
     * 获取返回码/编码/代号
     *
     * @return the code
     */
    T getCode();

}
