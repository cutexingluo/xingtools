package top.cutexingluo.tools.common.utils;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.base.IResult;
import top.cutexingluo.tools.common.base.IResultSource;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 条件组合, 数据装配策略类
 * <p>1.初始条件和后置条件 组合</p>
 * <p>2.可以自定义装配策略</p>
 *
 * <pre>
 * 1.初始条件: 初始对象判定条件
 * 2.后置条件: 初始对象是 IResult 实现类 其中对 getData  值的判定条件
 * </pre>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/26 17:33
 * @since 1.1.5
 */
@Data
public class ResultConditionGroup {

    /**
     * 初始条件, 一般为判空条件
     */
    @NotNull
    private Predicate<Object> condition;
    /**
     * 后置条件, IResult  data 条件
     */
    @NotNull
    private Predicate<IResult<?, ?>> resultDataCondition;

    /**
     * 初始条件为 true 时 是否填充 返回对象的 data 值
     *
     * <p>默认为 true, 对结果进行填充</p>
     */
    private boolean conditionTrueDataFill = true;

    /**
     * 初始条件为 true 时 是否填充 返回对象的 data 值
     *
     * <p>默认为 false, 不对结果进行填充</p>
     */
    private boolean conditionFalseDataFill = false;

    /**
     * 后置条件为 true 时 是否填充 返回对象的 data 值
     *
     * <p>默认为 true, 对结果进行填充</p>
     */
    private boolean resultDataConditionTrueDataFill = true;

    /**
     * 后置条件为 false 时 是否填充 返回对象的 data 值
     *
     * <p>默认为 true, 对结果进行填充</p>
     */
    private boolean resultDataConditionFalseDataFill = true;

    /**
     * 后置条件为 true 时 是否填充 返回对象的 msg 值
     * <p>0 不填充, 1 填充 , 2 智能填充 (目标对象msg 为 null 才填充)</p>
     *
     * <p>默认为 2, 对结果进行智能填充</p>
     */
    private short resultDataConditionTrueMsgFill = 2;

    /**
     * 后置条件为 false 时 是否填充 返回对象的 msg 值
     * <p>0 不填充, 1 填充 , 2 智能填充 (目标对象msg 为 null 才填充)</p>
     *
     * <p>默认为 2, 对结果进行智能填充</p>
     */
    private short resultDataConditionFalseMsgFill = 2;

    /**
     * 后置条件为 true 时 是否填充 返回对象的 code 值
     * <p>0 不填充, 1 填充 , 2 智能填充 (目标对象 code 为 null 才填充)</p>
     *
     * <p>默认为 2, 对结果进行智能填充</p>
     */
    private short resultDataConditionTrueCodeFill = 2;

    /**
     * 后置条件为 false 时 是否填充 返回对象的 code 值
     * <p>0 不填充, 1 填充 , 2 智能填充 (目标对象 code 为 null 才填充)</p>
     *
     * <p>默认为 2, 对结果进行智能填充</p>
     */
    private short resultDataConditionFalseCodeFill = 2;


    public ResultConditionGroup(@NotNull Predicate<Object> condition, @NotNull Predicate<IResult<?, ?>> resultDataCondition) {
        this.condition = condition;
        this.resultDataCondition = resultDataCondition;
    }

    //------common---------

    /**
     * 填充数据
     */
    public <RS extends IResultSource<C, O>, C, O> ResultConditionGroup fillMsg(@NotNull RS result, C code) {
        result.setCode(code);
        return this;
    }

    /**
     * 填充数据
     */
    public <RS extends IResultSource<C, O>, C, O> ResultConditionGroup fillMsg(@NotNull RS result, String msg) {
        result.setMsg(msg);
        return this;
    }

    /**
     * 填充数据
     */
    public <RS extends IResultSource<C, O>, C, O> ResultConditionGroup fillData(@NotNull RS result, O data) {
        result.setData(data);
        return this;
    }

    //-------condition--------

    /**
     * 条件填充数据(构造)
     */
    public <R extends IResult<C, O>, C, O> R conditionTrueFillDataNew(O data, @NotNull Function<O, R> presentFunc, Supplier<R> defaultSupplier) {
        if (conditionTrueDataFill) return presentFunc.apply(data);
        return defaultSupplier.get();
    }

    /**
     * 条件填充数据(构造)
     */
    public <R extends IResult<C, O>, C, O> R conditionFalseFillDataNew(O data, @NotNull Function<O, R> presentFunc, Supplier<R> defaultSupplier) {
        if (conditionFalseDataFill) return presentFunc.apply(data);
        return defaultSupplier.get();
    }


    /**
     * 条件填充数据
     */
    public <RS extends IResultSource<C, O>, C, O> ResultConditionGroup conditionTrueFillData(@NotNull RS result, O data) {
        if (conditionTrueDataFill) result.setData(data);
        return this;
    }

    /**
     * 条件填充数据
     */
    public <RS extends IResultSource<C, O>, C, O> ResultConditionGroup conditionFalseFillData(@NotNull RS result, O data) {
        if (conditionFalseDataFill) result.setData(data);
        return this;
    }

    /**
     * 条件填充数据
     */
    public <RS extends IResultSource<C, O>, C, O> ResultConditionGroup resultDataConditionTrueFillData(@NotNull RS result, O data) {
        if (resultDataConditionTrueDataFill) result.setData(data);
        return this;
    }

    /**
     * 条件填充数据
     */
    public <RS extends IResultSource<C, O>, C, O> ResultConditionGroup resultDataConditionFalseFillData(@NotNull RS result, O data) {
        if (resultDataConditionFalseDataFill) result.setData(data);
        return this;
    }

    /**
     * 条件填充数据
     */
    public <RS extends IResultSource<C, O>, C, O> ResultConditionGroup resultDataConditionTrueFillMsg(@NotNull RS result, String msg) {
        switch (resultDataConditionTrueMsgFill) {
            case 0:
                break;
            case 1:
                result.setMsg(msg);
                break;
            case 2:
                if (result.getMsg() == null) result.setMsg(msg);
                break;
        }
        return this;
    }

    /**
     * 条件填充数据
     */
    public <RS extends IResultSource<C, O>, C, O> ResultConditionGroup resultDataConditionFalseFillMsg(@NotNull RS result, String msg) {
        switch (resultDataConditionFalseMsgFill) {
            case 0:
                break;
            case 1:
                result.setMsg(msg);
                break;
            case 2:
                if (result.getMsg() == null) result.setMsg(msg);
                break;
        }
        return this;
    }

    /**
     * 条件填充数据
     */
    public <RS extends IResultSource<C, O>, C, O> ResultConditionGroup resultDataConditionTrueFillCode(@NotNull RS result, C code) {
        switch (resultDataConditionTrueCodeFill) {
            case 0:
                break;
            case 1:
                result.setCode(code);
                break;
            case 2:
                if (result.getCode() == null) result.setCode(code);
                break;
        }
        return this;
    }

    /**
     * 条件填充数据
     */
    public <RS extends IResultSource<C, O>, C, O> ResultConditionGroup resultDataConditionFalseFillCode(@NotNull RS result, C code) {
        switch (resultDataConditionFalseCodeFill) {
            case 0:
                break;
            case 1:
                result.setCode(code);
                break;
            case 2:
                if (result.getCode() == null) result.setCode(code);
                break;
        }
        return this;
    }
}
