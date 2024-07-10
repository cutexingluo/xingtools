package top.cutexingluo.tools.common.base;

/**
 * <p>综合接口, 拥有 msg , code  的get和set方法</p>
 * IResultDataSource 提供 set方法
 * <p>* 设置数据接口</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/14 20:00
 */
public interface IResultDataSource<C> extends IResultData<C> {
    IResultDataSource<C> setCode(C code);

    IResultDataSource<C> setMsg(String msg);

}
