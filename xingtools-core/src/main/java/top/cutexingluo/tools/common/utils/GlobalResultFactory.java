package top.cutexingluo.tools.common.utils;

import top.cutexingluo.tools.common.base.IResult;

/**
 * 全局 IResult 接口
 * <p>全局返回封装类</p>
 *
 * <p> 由于原来的很多操作返回Result ,  不能满足自定义返回情况，所以这里也使用IResult接口来返回自定义结果</p>
 * <p>需要自行注入到 Spring 容器</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/9 12:47
 * @since 1.0.3
 */
public interface GlobalResultFactory {

    /**
     * 返回新的全局封装类
     *
     * @param code 提供的code , 需要自行处理
     * @param msg  提示信息
     * @param data 数据
     * @return 全局的新对象，需要new
     */
    <C, T> IResult<C, T> newResult(int code, String msg, T data);


    static <C, T> IResult<C, T> selectResult(GlobalResultFactory factory, IResult<Integer, T> result) {
        if (factory == null) {
            return (IResult<C, T>) result;
        } else {
            return factory.newResult(result.getCode(), result.getMsg(), result.getData());
        }
    }
}
