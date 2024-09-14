package top.cutexingluo.tools.common.utils;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.function.TriFunction;
import top.cutexingluo.tools.common.Constants;
import top.cutexingluo.tools.common.MSResult;
import top.cutexingluo.tools.common.R;
import top.cutexingluo.tools.common.Result;
import top.cutexingluo.tools.common.base.IResult;
import top.cutexingluo.tools.common.base.IResultData;
import top.cutexingluo.tools.common.base.IResultSource;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Result 工具类
 * <p>减少判断情况, 智能根据结果封装返回类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/13 20:11
 */
public class ResultUtil {

    //-------------common----------------

    /**
     * 根据结果返回对应数据
     * <p>该方法可适用于自定义IResult 实现类</p>
     * <p>（如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false） 则返回 error</p>
     * <p>其他情况返回 successResult , 注意data 为false 依然会返回 success</p>
     *
     * @param data                     数据
     * @param successResult            成功返回（其他情况）
     * @param errorResult              错误返回 （data 为null）
     * @param successResultWithIResult 成功返回 （data 是 IResult的实现类）
     * @param errorResultWithIResult   错误返回 （data 是 IResult的实现类并且getData 为 null 或者false）
     * @since 1.1.4
     */
    @NotNull
    public static <R extends IResult<C, O>, C, O> R select(Object data,
                                                           @NotNull Function<Object, R> successResult,
                                                           @NotNull Function<Object, R> errorResult,
                                                           @NotNull Function<IResult<C, O>, R> successResultWithIResult,
                                                           @NotNull Function<IResult<C, O>, R> errorResultWithIResult
    ) {
        if (data == null) {
            return errorResult.apply(null);
        } else if (data instanceof IResult) {
            IResult<C, O> result = (IResult<C, O>) data;
            if (result.getData() == null || Boolean.FALSE.equals(result.getData())) {
                return errorResultWithIResult.apply(result);
            } else {
                return successResultWithIResult.apply(result);
            }
        } else {
            return successResult.apply(data);
        }
    }

    /**
     * 根据结果返回对应数据
     * <p>*推荐使用</p>
     * <p>该方法可适用于自定义IResult 实现类</p>
     * <p>（如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false） 则返回 error</p>
     * <p>其他情况返回 successResult , 注意data 为false 依然会返回 success</p>
     *
     * @param data          数据
     * @param successResult 成功返回（其他情况）
     * @param errorResult   错误返回 （如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false）
     * @since 1.1.4
     */
    @NotNull
    public static <RS extends IResultSource<C, O>, C, O> RS select(Object data,
                                                                   @NotNull RS successResult,
                                                                   @NotNull RS errorResult
    ) {
        if (data == null) {
            return errorResult;
        } else if (data instanceof IResult) {
            IResult<C, O> result = (IResult<C, O>) data;
            if (result.getData() == null || Boolean.FALSE.equals(result.getData())) {
                if (errorResult.getMsg() == null) errorResult.setMsg(result.getMsg());
                errorResult.setData(result.getData());
                return errorResult;
            } else {
                if (successResult.getMsg() == null) successResult.setMsg(result.getMsg());
                successResult.setData(result.getData());
                return successResult;
            }
        } else {
            successResult.setData((O) data);
            return successResult;
        }
    }

    /**
     * 根据结果返回对应数据
     * <p>该方法可适用于自定义IResult 实现类</p>
     * <p>（如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false） 则返回 error</p>
     * <p>其他情况返回 successResult , 注意data 为false 依然会返回 success</p>
     *
     * @param data              数据
     * @param successResultData 成功返回（其他情况）
     * @param errorResultData   错误返回 （如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false）
     * @param resultFactory     结果工厂, 自行对结果进行组装 (三个参数分别为 resultData, data具体数据 ,传入的原data)
     * @since 1.1.4
     */
    @NotNull
    public static <R extends IResult<C, O>, RD extends IResultData<C>, C, O> R selectFill(Object data,
                                                                                          @NotNull RD successResultData,
                                                                                          @NotNull RD errorResultData,
                                                                                          @NotNull TriFunction<RD, Object, IResult<C, O>, R> resultFactory
    ) {
        if (data == null) {
            return resultFactory.apply(errorResultData, null, null);
        } else if (data instanceof IResult) {
            IResult<C, O> result = (IResult<C, O>) data;
            if (result.getData() == null || Boolean.FALSE.equals(result.getData())) {
                return resultFactory.apply(errorResultData, result.getData(), result);
            } else {
                return resultFactory.apply(successResultData, result.getData(), result);
            }
        } else {
            return resultFactory.apply(successResultData, data, null);
        }
    }

    /**
     * 根据结果返回对应数据
     * <p>该方法可适用于自定义IResult 实现类</p>
     * <p>（如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false） 则返回 error</p>
     * <p>其他情况返回 successResult , 注意data 为false 依然会返回 success</p>
     *
     * @param data              数据
     * @param successResultData 成功返回（其他情况）
     * @param errorResultData   错误返回 （如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false）
     * @param resultFactory     结果工厂, 自行对结果进行组装 (三个参数分别为 resultData, data具体数据 )
     * @since 1.1.4
     */
    @NotNull
    public static <R extends IResult<C, O>, RD extends IResultData<C>, C, O> R selectFill(Object data,
                                                                                          @NotNull RD successResultData,
                                                                                          @NotNull RD errorResultData,
                                                                                          @NotNull BiFunction<RD, Object, R> resultFactory
    ) {
        if (data == null) {
            return resultFactory.apply(errorResultData, null);
        } else if (data instanceof IResult) {
            IResult<C, O> result = (IResult<C, O>) data;
            if (result.getData() == null || Boolean.FALSE.equals(result.getData())) {
                return resultFactory.apply(errorResultData, result.getData());
            } else {
                return resultFactory.apply(successResultData, result.getData());
            }
        } else {
            return resultFactory.apply(successResultData, data);
        }
    }

    /**
     * 根据结果返回对应数据
     * <p>*推荐使用</p>
     * <p>该方法可适用于自定义IResult 实现类</p>
     * <p>（如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false） 则返回 error</p>
     * <p>其他情况返回 returnResult , 注意data 为false 依然会返回 success</p>
     *
     * @param data            数据
     * @param returnResult    成功返回（其他情况）(错误返回会对该对象进行填充)
     * @param errorResultData 错误返回 （如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false）
     * @since 1.1.4
     */
    @NotNull
    public static <RS extends IResultSource<C, O>, RD extends IResultData<C>, C, O> RS selectFill(Object data,
                                                                                                  @NotNull RS returnResult,
                                                                                                  @NotNull RD errorResultData
    ) {
        if (data == null) {
            returnResult.setCode(errorResultData.getCode());
            returnResult.setMsg(errorResultData.getMsg());
            return returnResult;
        } else if (data instanceof IResult) {
            IResult<C, O> result = (IResult<C, O>) data;
            if (result.getData() == null || Boolean.FALSE.equals(result.getData())) {
                returnResult.setCode(errorResultData.getCode());
                returnResult.setMsg(errorResultData.getMsg());
                return returnResult;
            } else {
                returnResult.setData(result.getData());
                return returnResult;
            }
        } else {
            returnResult.setData((O) data);
            return returnResult;
        }
    }

    /**
     * 根据结果返回对应数据
     * <p>*推荐使用</p>
     * <p>该方法可适用于自定义IResult 实现类</p>
     * <p>（如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false） 则返回 error</p>
     * <p>其他情况返回 successResult , 注意data 为false 依然会返回 success</p>
     *
     * @param data              数据
     * @param successResultData 成功返回（其他情况）
     * @param errorResultData   错误返回 （如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false）
     * @param newResult         最终结果, 成功数据会存入 newResult.getData() , (可以传 errorResult, 因为错误不会填充数据)
     * @since 1.1.4
     */
    @NotNull
    public static <RS extends IResultSource<C, O>, RD extends IResultData<C>, C, O> RS selectFill(Object data,
                                                                                                  @NotNull RD successResultData,
                                                                                                  @NotNull RD errorResultData,
                                                                                                  @NotNull RS newResult
    ) {
        if (data == null) {
            newResult.setCode(errorResultData.getCode());
            newResult.setMsg(errorResultData.getMsg());
            return newResult;
        } else if (data instanceof IResult) {
            IResult<C, O> result = (IResult<C, O>) data;
            if (result.getData() == null || Boolean.FALSE.equals(result.getData())) {
                newResult.setCode(errorResultData.getCode());
                newResult.setMsg(errorResultData.getMsg());
                return newResult;
            } else {
                newResult.setCode(successResultData.getCode());
                newResult.setMsg(successResultData.getMsg());
                newResult.setData(result.getData());
                return newResult;
            }
        } else {
            newResult.setCode(successResultData.getCode());
            newResult.setMsg(successResultData.getMsg());
            newResult.setData((O) data);
            return newResult;
        }
    }

    /**
     * 根据结果返回对应数据
     * <p>*推荐使用</p>
     * <p>该方法可适用于自定义IResult 实现类</p>
     * <p>（如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false） 则返回 error</p>
     * <p>其他情况返回 successResult , 注意data 为false 依然会返回 success</p>
     *
     * @param data              数据
     * @param successResultData 成功返回（其他情况）
     * @param errorResultData   错误返回 （如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false）
     * @param newResult         最终结果, 成功数据会存入 newResult.getData() , 错误数据会填入 errorDefaultData
     * @param errorDefaultData  错误数据, 默认会填入的数据
     * @since 1.1.4
     */
    @NotNull
    public static <RS extends IResultSource<C, O>, RD extends IResultData<C>, C, O> RS selectFill(Object data,
                                                                                                  @NotNull RD successResultData,
                                                                                                  @NotNull RD errorResultData,
                                                                                                  @NotNull RS newResult,
                                                                                                  O errorDefaultData
    ) {
        if (data == null) {
            newResult.setCode(errorResultData.getCode());
            newResult.setMsg(errorResultData.getMsg());
            newResult.setData(errorDefaultData);
            return newResult;
        } else if (data instanceof IResult) {
            IResult<C, O> result = (IResult<C, O>) data;
            if (result.getData() == null || Boolean.FALSE.equals(result.getData())) {
                newResult.setCode(errorResultData.getCode());
                newResult.setMsg(errorResultData.getMsg());
                newResult.setData(errorDefaultData);
                return newResult;
            } else {
                newResult.setCode(successResultData.getCode());
                newResult.setMsg(successResultData.getMsg());
                newResult.setData(result.getData());
                return newResult;
            }
        } else {
            newResult.setCode(successResultData.getCode());
            newResult.setMsg(successResultData.getMsg());
            newResult.setData((O) data);
            return newResult;
        }
    }


    //-----------result-------------

    /**
     * 根据结果返回对应数据
     * <p>若是IResult的实现类，则会排除code</p>
     * <p>（如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false） 则返回 error</p>
     * <p>其他情况返回 successResult , 注意data 为false 依然会返回 success</p>
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
     * <p>（如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false） 则返回 error</p>
     * <p>其他情况返回 successResult , 注意data 为false 依然会返回 success</p>
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
     * <p>（如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false） 则返回 error</p>
     * <p>其他情况返回 successResult , 注意data 为false 依然会返回 success</p>
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
     * <p>若是IResult的实现类，则会排除code</p>
     * <p>（如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false） 则返回 error</p>
     * <p>其他情况返回 successResult , 注意data 为false 依然会返回 success</p>
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
     * <p>若是IResult的实现类，则会排除code</p>
     * <p>（如果 data 为 null ）或者 （data 是 IResult的实现类并且getData 为 null 或者false） 则返回 error</p>
     * <p>其他情况返回 successResult , 注意data 为false 依然会返回 success</p>
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


    //---------override------

    /**
     * 根据data 填写source
     * <p>万能方法，类似 selectResult 和 selectR</p>
     * <p>即 自定义返回接口 的自动装配数据方法</p>
     *
     * @param source   源 IResult 对象 需要继承 {@link IResultSource}
     * @param data     填入的数据
     * @param codeType source 的 code 类型
     * @return {@link T} result
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
