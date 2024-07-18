package top.cutexingluo.tools.common.base;

/**
 * IResult 数据接口
 * <p>* 返回数据接口</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/13 20:36
 */
public interface IResultData<T> extends IR {
    T getCode();

}
