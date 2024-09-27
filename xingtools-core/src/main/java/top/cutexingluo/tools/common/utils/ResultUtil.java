package top.cutexingluo.tools.common.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.basepackage.function.TriFunction;
import top.cutexingluo.tools.common.Constants;
import top.cutexingluo.tools.common.MSResult;
import top.cutexingluo.tools.common.R;
import top.cutexingluo.tools.common.Result;
import top.cutexingluo.tools.common.base.IResult;
import top.cutexingluo.tools.common.base.IResultData;
import top.cutexingluo.tools.common.base.IResultSource;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Result 工具类
 * <p>减少判断情况, 智能根据结果封装返回类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/13 20:11
 */
public class ResultUtil {

    /**
     * 判断是否为null
     *
     * @since 1.1.5
     */
    public static final Predicate<Object> IS_NULL = Objects::isNull;

    /**
     * 判断是否不为 null
     *
     * @since 1.1.5
     */
    public static final Predicate<Object> IS_NOT_NULL = Objects::nonNull;

    /**
     * 判断是否为null或者false
     *
     * @since 1.1.5
     */
    public static final Predicate<Object> IS_NULL_OR_FALSE = (obj) -> obj == null || Boolean.FALSE.equals(obj);

    /**
     * 判断是否不为null或者false
     *
     * @since 1.1.5
     */
    public static final Predicate<Object> IS_NOT_NULL_OR_FALSE = (obj) -> !(obj == null || Boolean.FALSE.equals(obj));


    /**
     * 判断是否为IResult 实现类
     *
     * @since 1.1.5
     */
    public static final Predicate<Object> IS_RESULT = (obj) -> obj instanceof IResult;

    /**
     * 判断是否不为为IResult 实现类
     *
     * @since 1.1.5
     */
    public static final Predicate<Object> IS_NOT_RESULT = (obj) -> !(obj instanceof IResult);


    /**
     * 初始条件判 null , 后置条件判 null 和false
     *
     * <p>v1.1.5 版本之前的默认策略</p>
     *
     * @since 1.1.5
     */
    public static final ResultConditionGroup STRATEGY_NON_AND_NON_FALSE = new ResultConditionGroup(IS_NOT_NULL, (result) -> IS_NOT_NULL_OR_FALSE.test(result.getData()));

    /**
     * 初始条件判 null , 后置条件判 null
     *
     * @since 1.1.5
     */
    public static final ResultConditionGroup STRATEGY_BOTH_NON = new ResultConditionGroup(IS_NOT_NULL, (result) -> IS_NOT_NULL.test(result.getData()));

    /**
     * 初始条件判 null 和false , 后置条件判 null
     *
     * @since 1.1.5
     */
    public static final ResultConditionGroup STRATEGY_NON_FALSE_AND_NON = new ResultConditionGroup(IS_NOT_NULL_OR_FALSE, (result) -> IS_NOT_NULL.test(result.getData()));

    /**
     * 初始条件判 null 和false , 后置条件判 null 和false
     *
     * <p>v1.1.5 版本开始的默认策略</p>
     *
     * @since 1.1.5
     */
    public static final ResultConditionGroup STRATEGY_BOTH_NON_FALSE = new ResultConditionGroup(IS_NOT_NULL_OR_FALSE, (result) -> IS_NOT_NULL_OR_FALSE.test(result.getData()));


    /**
     * 默认条件组
     * <p>可以自定义, 增加灵活性</p>
     *
     * @since 1.1.5
     */
    @NotNull
    public static ResultConditionGroup nowGroup = STRATEGY_BOTH_NON_FALSE;

    //-------------base----------------


    /**
     * 根据结果返回对应数据
     * <p>基础条件判断, 基础方法</p>
     *
     * @param data       数据
     * @param resultFunc 结果工厂, 自行对结果进行组装 (data具体数据)
     * @since 1.1.4
     */
    @NotNull
    public static <R extends IResult<C, O>, C, O, T> R choose(T data,
                                                              @NotNull Function<T, R> resultFunc
    ) {
        return resultFunc.apply(data);
    }

    /**
     * 根据结果返回对应数据
     * <p>基础条件判断, 基础方法</p>
     *
     * @param data      数据
     * @param condition 条件
     * @param trueFunc  为true时返回的数据
     * @param falseFunc 为false时返回的数据
     * @since 1.1.4
     */
    @NotNull
    public static <R extends IResult<C, O>, C, O, T> R choose(T data,
                                                              @NotNull Predicate<T> condition,
                                                              @NotNull Function<T, R> trueFunc,
                                                              @NotNull Function<T, R> falseFunc
    ) {
        if (condition.test(data)) {
            return trueFunc.apply(data);
        } else {
            return falseFunc.apply(data);
        }
    }

    /**
     * 根据结果返回对应数据
     *
     * <p>全局基础方法</p>
     *
     * @param data                  数据
     * @param condition             初始条件
     * @param conditionFalseResult  初始条件false（condition -> false 返回）
     * @param resultFalseResult     初始条件true 但不为 IResult 实现类（condition -> true 并且 不为IResult实现类 返回）
     * @param resultDataCondition   IResult 实现类的后置条件
     * @param resultDataFalseResult 错误返回 （condition -> true , 为IResult实现类, 并且 resultDataCondition -> false 返回）
     * @param resultDataTrueResult  成功返回 （condition -> true , 为IResult实现类, 并且 resultDataCondition -> true 返回）
     * @return {@link R } 结果
     */
    @NotNull
    public static <R extends IResult<C, O>, C, O, T> R chooseSelect(T data,
                                                                    @NotNull Predicate<T> condition,
                                                                    @NotNull Function<T, R> conditionFalseResult,
                                                                    @NotNull Function<T, R> resultFalseResult,
                                                                    @NotNull Predicate<IResult<C, O>> resultDataCondition,
                                                                    @NotNull Function<IResult<C, O>, R> resultDataFalseResult,
                                                                    @NotNull Function<IResult<C, O>, R> resultDataTrueResult
    ) {
        if (!condition.test(data)) { // 初始条件
            return conditionFalseResult.apply(data);
        } else if (IS_NOT_RESULT.test(data)) { // 不为 IResult 实现类
            return resultFalseResult.apply(data);
        }
        IResult<C, O> result = (IResult<C, O>) data;
        if (!resultDataCondition.test(result)) { // 判断结果数据
            return resultDataFalseResult.apply(result);
        } else {
            return resultDataTrueResult.apply(result);
        }
    }

    //-------------common----------------

    /**
     * 根据结果返回对应数据
     * <p>该方法可适用于自定义IResult 实现类</p>
     * <p>select 数据填充方式 (select 基础判断)</p>
     *
     * @param data                     数据
     * @param conditionGroup           条件组合(含下面每两个函数 判定的条件, 仅使用两个条件, 其他配置失效)
     * @param successResult            成功返回（condition -> true 并且 不为IResult实现类 返回）
     * @param errorResult              错误返回 （condition -> false 返回）
     * @param successResultWithIResult 成功返回 （condition -> true , 为IResult实现类, 并且 resultDataCondition -> true 返回）
     * @param errorResultWithIResult   错误返回 （condition -> true , 为IResult实现类, 并且 resultDataCondition -> false 返回）
     * @since 1.1.5
     */
    @NotNull
    public static <R extends IResult<C, O>, C, O, T> R select(T data,
                                                              @NotNull ResultConditionGroup conditionGroup,
                                                              @NotNull Function<T, R> successResult,
                                                              @NotNull Function<T, R> errorResult,
                                                              @NotNull Function<IResult<C, O>, R> successResultWithIResult,
                                                              @NotNull Function<IResult<C, O>, R> errorResultWithIResult
    ) {
        return chooseSelect(data,
                conditionGroup.getCondition()::test,
                errorResult, successResult,
                conditionGroup.getResultDataCondition()::test,
                errorResultWithIResult, successResultWithIResult
        );
    }

    /**
     * 根据结果返回对应数据
     * <p>该方法可适用于自定义IResult 实现类</p>
     * <p>默认 nowGroup 策略</p>
     *
     * @see #select(Object, ResultConditionGroup, Function, Function, Function, Function)
     * @since 1.1.4
     */
    @NotNull
    public static <R extends IResult<C, O>, C, O, T> R select(T data,
                                                              @NotNull Function<T, R> successResult,
                                                              @NotNull Function<T, R> errorResult,
                                                              @NotNull Function<IResult<C, O>, R> successResultWithIResult,
                                                              @NotNull Function<IResult<C, O>, R> errorResultWithIResult
    ) {
        return select(data, nowGroup, successResult, errorResult, successResultWithIResult, errorResultWithIResult);
    }

    /**
     * 根据结果返回对应数据
     * <p>*推荐使用</p>
     * <p>该方法可适用于自定义IResult 实现类</p>
     *
     * @param data          数据
     * @param successResult 成功返回（其他情况）
     * @param errorResult   错误返回
     * @since 1.1.5
     */
    @NotNull
    public static <RS extends IResultSource<C, O>, C, O> RS select(Object data,
                                                                   @NotNull ResultConditionGroup conditionGroup,
                                                                   @NotNull RS successResult,
                                                                   @NotNull RS errorResult
    ) {
        RS ret = chooseSelect(data,
                conditionGroup.getCondition(),
                (falseData) -> {
                    conditionGroup.conditionFalseFillData(errorResult, (O) falseData);
                    return errorResult;
                }, (trueData) -> {
                    conditionGroup.conditionTrueFillData(successResult, (O) trueData);
                    return successResult;
                },
                conditionGroup.getResultDataCondition()::test,
                (falseResult) -> {
                    conditionGroup.resultDataConditionFalseFillData(errorResult, falseResult.getData());
                    conditionGroup.resultDataConditionFalseFillMsg(errorResult, falseResult.getMsg());
                    conditionGroup.resultDataConditionFalseFillCode(errorResult, falseResult.getCode());
                    return errorResult;
                }, (trueResult) -> {
                    conditionGroup.resultDataConditionTrueFillData(successResult, trueResult.getData());
                    conditionGroup.resultDataConditionTrueFillMsg(successResult, trueResult.getMsg());
                    conditionGroup.resultDataConditionTrueFillCode(successResult, trueResult.getCode());
                    return successResult;
                }
        );
        return ret;
    }

    /**
     * 根据结果返回对应数据
     * <p>*推荐使用</p>
     * <p>该方法可适用于自定义IResult 实现类</p>
     *
     * @param data          数据
     * @param successResult 成功返回（其他情况）
     * @param errorResult   错误返回
     * @since 1.1.4
     */
    @NotNull
    public static <RS extends IResultSource<C, O>, C, O> RS select(Object data,
                                                                   @NotNull RS successResult,
                                                                   @NotNull RS errorResult
    ) {
        return select(data, nowGroup, successResult, errorResult);
    }

    /**
     * 根据结果返回对应数据
     * <p>该方法可适用于自定义IResult 实现类</p>
     *
     * @param data              数据
     * @param conditionGroup    条件组合(判定的条件, 仅使用两个条件, 其他配置失效)
     * @param successResultData 成功返回
     * @param errorResultData   错误返回
     * @param resultFactory     结果工厂, 自行对结果进行组装 (三个参数分别为 resultData, data具体数据 ,传入的原data)
     * @since 1.1.5
     */
    @NotNull
    public static <R extends IResult<C, O>, RD extends IResultData<C>, C, O> R selectFill(Object data,
                                                                                          @NotNull ResultConditionGroup conditionGroup,
                                                                                          @NotNull RD successResultData,
                                                                                          @NotNull RD errorResultData,
                                                                                          @NotNull TriFunction<RD, Object, IResult<C, O>, R> resultFactory
    ) {
        R ret = chooseSelect(data,
                conditionGroup.getCondition(),
                (falseData) -> resultFactory.apply(errorResultData, falseData, null),
                (trueData) -> resultFactory.apply(successResultData, trueData, null),
                conditionGroup.getResultDataCondition()::test,
                (falseResult) -> resultFactory.apply(errorResultData, falseResult.getData(), falseResult),
                (trueResult) -> resultFactory.apply(errorResultData, trueResult.getData(), trueResult)
        );
        return ret;
    }

    /**
     * 根据结果返回对应数据
     * <p>该方法可适用于自定义IResult 实现类</p>
     *
     * @see #selectFill(Object, ResultConditionGroup, IResultData, IResultData, TriFunction)
     * @since 1.1.4
     */
    @NotNull
    public static <R extends IResult<C, O>, RD extends IResultData<C>, C, O> R selectFill(Object data,
                                                                                          @NotNull RD successResultData,
                                                                                          @NotNull RD errorResultData,
                                                                                          @NotNull TriFunction<RD, Object, IResult<C, O>, R> resultFactory
    ) {
        return selectFill(data, nowGroup, successResultData, errorResultData, resultFactory);
    }

    /**
     * 根据结果返回对应数据
     * <p>该方法可适用于自定义IResult 实现类</p>
     *
     * @param data              数据
     * @param conditionGroup    条件组合(判定的条件, 仅使用两个条件, 其他配置失效)
     * @param successResultData 成功返回
     * @param errorResultData   错误返回
     * @param resultFactory     结果工厂, 自行对结果进行组装 (三个参数分别为 resultData, data具体数据 )
     * @since 1.1.5
     */
    @NotNull
    public static <R extends IResult<C, O>, RD extends IResultData<C>, C, O> R selectFill(Object data,
                                                                                          @NotNull ResultConditionGroup conditionGroup,
                                                                                          @NotNull RD successResultData,
                                                                                          @NotNull RD errorResultData,
                                                                                          @NotNull BiFunction<RD, Object, R> resultFactory
    ) {
        R ret = chooseSelect(data,
                conditionGroup.getCondition(),
                (falseData) -> resultFactory.apply(errorResultData, falseData),
                (trueData) -> resultFactory.apply(successResultData, trueData),
                conditionGroup.getResultDataCondition()::test,
                (falseResult) -> resultFactory.apply(errorResultData, falseResult.getData()),
                (trueResult) -> resultFactory.apply(errorResultData, trueResult.getData())
        );
        return ret;
    }

    /**
     * 根据结果返回对应数据
     * <p>该方法可适用于自定义IResult 实现类</p>
     *
     * @see #selectFill(Object, ResultConditionGroup, IResultData, IResultData, BiFunction)
     * @since 1.1.4
     */
    @NotNull
    public static <R extends IResult<C, O>, RD extends IResultData<C>, C, O> R selectFill(Object data,
                                                                                          @NotNull RD successResultData,
                                                                                          @NotNull RD errorResultData,
                                                                                          @NotNull BiFunction<RD, Object, R> resultFactory
    ) {
        return selectFill(data, nowGroup, successResultData, errorResultData, resultFactory);
    }

    /**
     * 根据结果返回对应数据
     * <p>*推荐使用</p>
     * <p>该方法可适用于自定义IResult 实现类</p>
     *
     * @param data            数据
     * @param conditionGroup  条件组合(判定的条件)
     * @param returnResult    成功返回
     * @param errorResultData 错误返回 （优先级填充 errorResultData> data( IResult 实现类) / returnResult）
     * @since 1.1.5
     */
    @NotNull
    public static <RS extends IResultSource<C, O>, RD extends IResultData<C>, C, O> RS selectFill(Object data,
                                                                                                  @NotNull ResultConditionGroup conditionGroup,
                                                                                                  @NotNull RS returnResult,
                                                                                                  @NotNull RD errorResultData
    ) {
        RS ret = chooseSelect(data,
                conditionGroup.getCondition(),
                (falseData) -> {
                    // 强制填充错误返回数据
                    returnResult.setCode(errorResultData.getCode());
                    returnResult.setMsg(errorResultData.getMsg());
                    conditionGroup.conditionFalseFillData(returnResult, (O) falseData);
                    return returnResult;
                }, (trueData) -> {
                    conditionGroup.conditionTrueFillData(returnResult, (O) trueData);
                    return returnResult;
                },
                conditionGroup.getResultDataCondition()::test,
                (falseResult) -> {
                    conditionGroup.resultDataConditionFalseFillData(returnResult, falseResult.getData());
                    // 填充错误返回数据
                    if (errorResultData.getCode() != null) {
                        returnResult.setCode(errorResultData.getCode());
                    } else {
                        conditionGroup.resultDataConditionFalseFillCode(returnResult, falseResult.getCode());
                    }
                    if (errorResultData.getMsg() != null) {
                        returnResult.setMsg(errorResultData.getMsg());
                    } else {
                        conditionGroup.resultDataConditionFalseFillMsg(returnResult, falseResult.getMsg());
                    }
                    return returnResult;
                }, (trueResult) -> {
                    conditionGroup.resultDataConditionTrueFillData(returnResult, trueResult.getData());
                    conditionGroup.resultDataConditionTrueFillMsg(returnResult, trueResult.getMsg());
                    conditionGroup.resultDataConditionTrueFillCode(returnResult, trueResult.getCode());
                    return returnResult;
                }
        );
        return ret;
    }

    /**
     * 根据结果返回对应数据
     * <p>*推荐使用</p>
     * <p>该方法可适用于自定义IResult 实现类</p>
     *
     * @see #selectFill(Object, ResultConditionGroup, IResultSource, IResultData)
     * @since 1.1.4
     */
    @NotNull
    public static <RS extends IResultSource<C, O>, RD extends IResultData<C>, C, O> RS selectFill(Object data,
                                                                                                  @NotNull RS returnResult,
                                                                                                  @NotNull RD errorResultData
    ) {
        return selectFill(data, nowGroup, returnResult, errorResultData);
    }

    /**
     * 根据结果返回对应数据
     * <p>*推荐使用</p>
     * <p>该方法可适用于自定义IResult 实现类</p>
     *
     * @param data              数据
     * @param successResultData 成功返回
     * @param errorResultData   错误返回
     * @param newResult         最终结果, 最好是一个没有值的新对象
     * @since 1.1.5
     */
    @NotNull
    public static <RS extends IResultSource<C, O>, RD extends IResultData<C>, C, O> RS selectFill(Object data,
                                                                                                  @NotNull ResultConditionGroup conditionGroup,
                                                                                                  @NotNull RD successResultData,
                                                                                                  @NotNull RD errorResultData,
                                                                                                  @NotNull RS newResult
    ) {
        RS ret = selectFill(data, conditionGroup, successResultData, errorResultData, newResult, null);
        return ret;
    }

    /**
     * 根据结果返回对应数据
     * <p>*推荐使用</p>
     * <p>该方法可适用于自定义IResult 实现类</p>
     *
     * @see #selectFill(Object, ResultConditionGroup, IResultData, IResultData, IResultSource)
     * @since 1.1.4
     */
    @NotNull
    public static <RS extends IResultSource<C, O>, RD extends IResultData<C>, C, O> RS selectFill(Object data,
                                                                                                  @NotNull RD successResultData,
                                                                                                  @NotNull RD errorResultData,
                                                                                                  @NotNull RS newResult
    ) {
        return selectFill(data, nowGroup, successResultData, errorResultData, newResult);
    }

    /**
     * 根据结果返回对应数据
     * <p>*推荐使用</p>
     * <p>该方法可适用于自定义IResult 实现类</p>
     *
     * @param data              数据
     * @param conditionGroup    条件组合(判定的条件)
     * @param successResultData 成功返回
     * @param errorResultData   错误返回
     * @param newResult         最终结果, 成功数据会存入 newResult.getData() , 错误数据会填入 errorDefaultData(如果存在)
     * @param errorDefaultData  错误数据, 默认会填入的数据
     * @since 1.1.5
     */
    @NotNull
    public static <RS extends IResultSource<C, O>, RD extends IResultData<C>, C, O> RS selectFill(Object data,
                                                                                                  @NotNull ResultConditionGroup conditionGroup,
                                                                                                  @NotNull RD successResultData,
                                                                                                  @NotNull RD errorResultData,
                                                                                                  @NotNull RS newResult,
                                                                                                  @Nullable O errorDefaultData
    ) {
        RS ret = chooseSelect(data,
                conditionGroup.getCondition(),
                (falseData) -> {
                    // 强制填充错误返回数据
                    newResult.setCode(errorResultData.getCode());
                    newResult.setMsg(errorResultData.getMsg());
                    if (errorDefaultData != null) {
                        newResult.setData(errorDefaultData);
                    } else {
                        conditionGroup.conditionFalseFillData(newResult, (O) falseData);
                    }
                    return newResult;
                }, (trueData) -> {
                    // 强制填充成功返回数据
                    newResult.setCode(successResultData.getCode());
                    newResult.setMsg(successResultData.getMsg());
                    conditionGroup.conditionTrueFillData(newResult, (O) trueData);
                    return newResult;
                },
                conditionGroup.getResultDataCondition()::test,
                (falseResult) -> {
                    if (errorDefaultData != null) {
                        newResult.setData(errorDefaultData);
                    } else {
                        conditionGroup.resultDataConditionFalseFillData(newResult, falseResult.getData());
                    }
                    // 填充错误返回数据
                    if (errorResultData.getCode() != null) {
                        newResult.setCode(errorResultData.getCode());
                    } else {
                        conditionGroup.resultDataConditionFalseFillCode(newResult, falseResult.getCode());
                    }
                    if (errorResultData.getMsg() != null) {
                        newResult.setMsg(errorResultData.getMsg());
                    } else {
                        conditionGroup.resultDataConditionFalseFillMsg(newResult, falseResult.getMsg());
                    }
                    return newResult;
                }, (trueResult) -> {
                    conditionGroup.resultDataConditionTrueFillData(newResult, trueResult.getData());
                    // 填充成功返回数据
                    if (errorResultData.getCode() != null) {
                        newResult.setCode(successResultData.getCode());
                    } else {
                        conditionGroup.resultDataConditionFalseFillCode(newResult, trueResult.getCode());
                    }
                    if (errorResultData.getMsg() != null) {
                        newResult.setMsg(successResultData.getMsg());
                    } else {
                        conditionGroup.resultDataConditionFalseFillMsg(newResult, trueResult.getMsg());
                    }
                    return newResult;
                }
        );
        return ret;
    }

    /**
     * 根据结果返回对应数据
     * <p>*推荐使用</p>
     * <p>该方法可适用于自定义IResult 实现类</p>
     *
     * @see #selectFill(Object, ResultConditionGroup, IResultData, IResultData, IResultSource, Object)
     * @since 1.1.4
     */
    @NotNull
    public static <RS extends IResultSource<C, O>, RD extends IResultData<C>, C, O> RS selectFill(Object data,
                                                                                                  @NotNull RD successResultData,
                                                                                                  @NotNull RD errorResultData,
                                                                                                  @NotNull RS newResult,
                                                                                                  O errorDefaultData
    ) {
        return selectFill(data, nowGroup, successResultData, errorResultData, newResult, errorDefaultData);
    }


    //-----------result-------------


    /**
     * 根据结果返回对应数据
     *
     * @param data           数据
     * @param conditionGroup 条件组合(判定的条件)
     * @param successMsg     成功msg
     * @param errorMsg       错误msg
     * @param errorCode      错误代码
     * @return {@link Result}
     * @since 1.1.5
     */
    @NotNull
    public static Result selectResult(Object data,
                                      @NotNull ResultConditionGroup conditionGroup,
                                      String successMsg, String errorMsg, Integer errorCode) {
        Result ret = chooseSelect(data,
                conditionGroup.getCondition(),
                (falseData) -> {
                    Result newResult = Result.error(null);
                    if (errorCode != null) newResult.setCode(errorCode);
                    if (errorMsg != null) newResult.setMsg(errorMsg);
                    conditionGroup.conditionFalseFillData(newResult, falseData);
                    return newResult;
                }, (trueData) -> {
                    // 强制填充成功返回数据
                    Result newResult = Result.success(null);
                    if (successMsg != null) newResult.setMsg(successMsg);
                    conditionGroup.conditionTrueFillData(newResult, trueData);
                    return newResult;
                },
                conditionGroup.getResultDataCondition()::test,
                (falseResult) -> {
                    Result newResult = Result.error(null);
                    conditionGroup.resultDataConditionFalseFillData(newResult, falseResult.getData());
                    // 填充错误返回数据
                    if (errorCode != null) newResult.setCode(errorCode);
                    else conditionGroup.resultDataConditionFalseFillCode(newResult, falseResult.getCode());
                    if (errorMsg != null) newResult.setMsg(errorMsg);
                    else conditionGroup.resultDataConditionFalseFillMsg(newResult, falseResult.getMsg());
                    return newResult;
                }, (trueResult) -> {
                    Result newResult = Result.success(null);
                    conditionGroup.resultDataConditionTrueFillData(newResult, trueResult.getData());
                    // 填充成功返回数据
                    conditionGroup.resultDataConditionTrueFillCode(newResult, trueResult.getCode());
                    if (successMsg != null) newResult.setMsg(successMsg);
                    else conditionGroup.resultDataConditionTrueFillMsg(newResult, trueResult.getMsg());
                    return newResult;
                }
        );
        return ret;
    }

    /**
     * 根据结果返回对应数据
     *
     * @param data           数据
     * @param conditionGroup 条件组合(判定的条件)
     * @return {@link Result}
     */
    @NotNull
    public static Result selectResult(Object data,
                                      @NotNull ResultConditionGroup conditionGroup) {
        return selectResult(data, conditionGroup, null, null, null);
    }

    /**
     * 根据结果返回对应数据
     *
     * @param data 数据
     * @return {@link Result}
     */
    @NotNull
    public static Result selectResult(Object data) {
        return selectResult(data, nowGroup);
    }


    /**
     * 根据结果返回对应数据
     */
    @NotNull
    public static Result selectResult(Object data, String successMsg, String errorMsg, Integer errorCode) {
        return selectResult(data, nowGroup, successMsg, errorMsg, errorCode);
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
    public static Result selectResult(Object data, String successMsg, String errorMsg) {
        return selectResult(data, nowGroup, successMsg, errorMsg, Constants.CODE_500.intCode());
    }

    /**
     * 根据结果返回对应数据
     *
     * @param data           数据
     * @param conditionGroup 条件组合(判定的条件)
     * @return {@link MSResult}
     * @since 1.1.5
     */
    @NotNull
    public static <C, O> MSResult<O> selectMSResult(Object data,
                                                    @NotNull ResultConditionGroup conditionGroup,
                                                    String successMsg, String errorMsg, Integer errorCode) {
        MSResult<O> ret = chooseSelect(data,
                conditionGroup.getCondition(),
                (falseData) -> {
                    MSResult<O> newResult = MSResult.error(null);
                    if (errorCode != null) newResult.setCode(errorCode);
                    if (errorMsg != null) newResult.setMsg(errorMsg);
                    conditionGroup.conditionTrueFillData(newResult, (O) falseData);
                    return newResult;
                }, (trueData) -> {
                    // 强制填充成功返回数据
                    MSResult<O> newResult = MSResult.success(null);
                    if (successMsg != null) newResult.setMsg(successMsg);
                    conditionGroup.conditionTrueFillData(newResult, (O) trueData);
                    return newResult;
                },
                conditionGroup.getResultDataCondition()::test,
                (falseResult) -> {
                    MSResult<O> newResult = MSResult.error(null);
                    conditionGroup.resultDataConditionFalseFillData(newResult, falseResult.getData());
                    // 填充错误返回数据
                    if (errorCode != null) newResult.setCode(errorCode);
                    else conditionGroup.resultDataConditionFalseFillCode(newResult, falseResult.getCode());
                    if (errorMsg != null) newResult.setMsg(errorMsg);
                    else conditionGroup.resultDataConditionFalseFillMsg(newResult, falseResult.getMsg());
                    return newResult;
                }, (trueResult) -> {
                    MSResult<O> newResult = MSResult.success(null);
                    conditionGroup.resultDataConditionTrueFillData(newResult, trueResult.getData());
                    // 填充成功返回数据
                    conditionGroup.resultDataConditionFalseFillCode(newResult, trueResult.getCode());
                    if (successMsg != null) newResult.setMsg(successMsg);
                    else conditionGroup.resultDataConditionFalseFillMsg(newResult, trueResult.getMsg());
                    return newResult;
                }
        );
        return ret;
    }

    /**
     * 根据结果返回对应数据
     *
     * @param data           数据
     * @param conditionGroup 条件组合(判定的条件)
     * @return {@link MSResult}
     * @since 1.1.5
     */
    @NotNull
    public static <C, T> MSResult<T> selectMSResult(Object data,
                                                    @NotNull ResultConditionGroup conditionGroup) {
        return selectMSResult(data, conditionGroup, null, null, null);
    }

    /**
     * 根据结果返回对应数据
     *
     * @param data 数据
     * @return {@link MSResult}
     * @since 1.0.4
     */
    @NotNull
    public static <C, T> MSResult<T> selectMSResult(Object data) {
        return selectMSResult(data, nowGroup);
    }

    /**
     * 根据结果返回对应数据
     *
     * @param data 数据
     * @return {@link MSResult}
     * @since 1.0.4
     */
    @NotNull
    public static <C, T> MSResult<T> selectMSResult(Object data, String successMsg, String errorMsg, Integer errorCode) {
        return selectMSResult(data, nowGroup, successMsg, errorMsg, errorCode);
    }

    /**
     * 根据结果返回对应数据
     *
     * @param data 数据
     * @return {@link R}
     * @updateFrom 1.0.4
     */
    @NotNull
    public static <C, T> R<T> selectR(Object data) {
        MSResult<T> result = selectMSResult(data);
        return result.toR();
    }

    /**
     * 根据结果返回对应数据
     *
     * @param data 数据
     * @return {@link R}
     * @updateFrom 1.0.4
     */
    @NotNull
    public static <C, T> R<T> selectR(Object data, String successMsg, String errorMsg, int errorCode) {
        MSResult<T> result = selectMSResult(data, successMsg, errorMsg, errorCode);
        return result.toR();
    }

    @NotNull
    public static <C, T> R<T> selectR(Object data, String successMsg, String errorMsg) {
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
