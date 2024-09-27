package top.cutexingluo.tools.common.base;

/**
 * IResult 接口
 * <p>* 返回数据接口</p>
 * <p>综合接口，拥有 msg , code , data 的所有get方法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/13 20:34
 */
public interface IResult<C, T> extends IResultData<C> {
    T getData();
}
