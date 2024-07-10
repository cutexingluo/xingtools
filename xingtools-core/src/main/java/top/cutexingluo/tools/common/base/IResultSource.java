package top.cutexingluo.tools.common.base;

/**
 * <p>综合接口, 拥有 msg , code , data 的get和set方法</p>
 * IResultSource 提供set方法
 * <p>* 设置数据接口</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/14 19:58
 */
public interface IResultSource<C, T> extends IResult<C, T>, IResultDataSource<C> {

    IResultSource<C, T> setData(T data);
}
