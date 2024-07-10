package top.cutexingluo.tools.common.utils;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.Constants;
import top.cutexingluo.tools.common.MSResult;
import top.cutexingluo.tools.common.R;
import top.cutexingluo.tools.common.Result;
import top.cutexingluo.tools.common.base.IResult;
import top.cutexingluo.tools.common.base.IResultSource;

/**
 * Result 工具类
 * <p>减少判断情况</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/13 20:11
 */
public class ResultUtil {

    /**
     * 根据结果返回对应数据
     * <p>若是IResult的实现类，则会排除code</p>
     *
     * @param data 数据
     * @return {@link Result}
     */
    @NotNull
    public static <C, O> Result selectResult(Object data) {
        if (data == null) {
            return Result.error();
        } else if (data instanceof IResult) {
            IResult<C, O> result = (IResult<C, O>) data;
            if (result.getData() == null || Boolean.FALSE.equals(result.getData())) {
                return Result.error(result.getMsg());
            } else {
                return Result.success(result.getMsg(), result.getData());
            }
        } else {
            return Result.success(data);
        }
    }

    /**
     * 根据结果返回对应数据
     * <p>若是IResult的实现类，则会排除code</p>
     *
     * @param data       数据
     * @param successMsg 成功msg
     * @param errorMsg   错误msg
     * @param errorCode  错误代码
     * @return {@link Result}
     */
    @NotNull
    public static <C, O> Result selectResult(Object data, String successMsg, String errorMsg, int errorCode) {
        if (data == null) {
            return Result.error(errorCode, errorMsg != null ? errorMsg : "获取失败");
        } else if (data instanceof IResult) {
            IResult<C, O> result = (IResult<C, O>) data;
            if (result.getData() == null || Boolean.FALSE.equals(result.getData())) {
                return Result.error(errorCode, errorMsg != null ? errorMsg : result.getMsg());
            } else {
                return Result.success(successMsg != null ? successMsg : result.getMsg(), result.getData());
            }
        } else {
            if (successMsg != null) {
                return Result.success(successMsg, data);
            }
            return Result.success(data);
        }
    }

    /**
     * 根据结果返回对应数据
     * <p>若是IResult的实现类，则会排除code</p>
     *
     * @param data       数据
     * @param successMsg 成功msg
     * @param errorMsg   错误msg
     * @return {@link Result}
     */
    @NotNull
    public static <C, O> Result selectResult(Object data, String successMsg, String errorMsg) {
        return selectResult(data, successMsg, errorMsg, Constants.CODE_500.intCode());
    }


    /**
     * 根据结果返回对应数据
     *
     * @param data 数据
     * @return {@link MSResult}
     * @since 1.0.4
     */
    @NotNull
    public static <C, T> MSResult<T> selectMSResult(T data) {
        if (data == null) {
            return (MSResult<T>) MSResult.error();
        } else if (data instanceof IResult) {
            IResult<C, T> result = (IResult<C, T>) data;
            if (result.getData() == null || Boolean.FALSE.equals(result.getData())) {
                return (MSResult<T>) MSResult.error(result.getMsg());
            } else {
                return MSResult.success(result.getMsg(), result.getData());
            }
        } else {
            return MSResult.success(data);
        }
    }

    /**
     * 根据结果返回对应数据
     *
     * @param data 数据
     * @return {@link MSResult}
     * @since 1.0.4
     */
    @NotNull
    public static <C, T> MSResult<T> selectMSResult(T data, String successMsg, String errorMsg, int errorCode) {
        if (data == null) {
            return (MSResult<T>) MSResult.error(errorCode, errorMsg != null ? errorMsg : "获取失败");
        } else if (data instanceof IResult) {
            IResult<C, T> result = (IResult<C, T>) data;
            if (result.getData() == null || Boolean.FALSE.equals(result.getData())) {
                return (MSResult<T>) MSResult.error(errorCode, errorMsg != null ? errorMsg : result.getMsg());
            } else {
                return MSResult.success(successMsg != null ? successMsg : result.getMsg(), result.getData());
            }
        } else {
            if (successMsg != null) {
                return MSResult.success(successMsg, data);
            }
            return MSResult.success(data);
        }
    }

    /**
     * 根据结果返回对应数据
     *
     * <p>1.0.4 版本修复bug</p>
     *
     * @param data 数据
     * @return {@link R}
     * @updateFrom 1.0.4
     */
    @NotNull
    public static <C, T> R<T> selectR(T data) {
        return selectMSResult(data).toR();
    }

    /**
     * 根据结果返回对应数据
     *
     * <p>1.0.4 版本修复bug</p>
     *
     * @param data 数据
     * @return {@link R}
     * @updateFrom 1.0.4
     */
    @NotNull
    public static <C, T> R<T> selectR(T data, String successMsg, String errorMsg, int errorCode) {
        return selectMSResult(data, successMsg, errorMsg, errorCode).toR();
    }

    @NotNull
    public static <C, T> R<T> selectR(T data, String successMsg, String errorMsg) {
        return selectR(data, successMsg, errorMsg, Constants.CODE_500.intCode());
    }


    /**
     * 根据data 填写source
     * <p>万能方法，类似 selectResult 和 selectR</p>
     * <p>即 自定义返回接口 的自动装配数据方法</p>
     *
     * @param source   源 IResult 对象 需要继承 {@link IResultSource}
     * @param data     填入的数据
     * @param codeType source 的 code 类型
     * @return {@link T}
     */
    @NotNull
    public static <T extends IResultSource<C, O>, C, O> T fillResult(T source, O data, Class<C> codeType) {
        R<O> r = selectR(data);
        if (codeType == Integer.class) {
            source.setCode((C) r.getCode());
        } else if (codeType == String.class) {
            source.setCode((C) r.getCode().toString());
        }
        source.setMsg(r.getMsg());
        source.setData(r.getData());
        return source;
    }

}
