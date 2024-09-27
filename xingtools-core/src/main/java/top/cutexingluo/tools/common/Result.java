package top.cutexingluo.tools.common;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.cutexingluo.tools.common.base.IResult;
import top.cutexingluo.tools.common.base.IResultData;
import top.cutexingluo.tools.common.base.XTStrCode;
import top.cutexingluo.tools.designtools.method.ClassMaker;

import java.util.List;
import java.util.Map;

/**
 * 接口统一返回包装类
 * <p>
 * 适用于Controller层与前端数据传输
 * 或者其他工具类的异常处理
 * </p>
 * <p>
 * 提供一个泛型MSResult , 不需要泛型则使用Result
 * <p>兼容 int 类型的 code</p>
 *
 * @author XingTian
 * @version 1.1.1
 * @date 2023/7/13 20:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result extends CommonResult<Integer, Object> implements XTStrCode {

    protected int code;
    protected String msg;
    protected Object data;

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public Result setCode(Integer code) {
        this.code = code;
        return this;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String strCode() {
        return String.valueOf(code);
    }
    //-----------convertor---------------


    public <T> Result(MSResult<T> result) {
        this.code = result.getCode();
        this.msg = result.getMsg();
        this.data = result.getData();
    }

    public Result(StrResult result) {
        this.code = result.intCode();
        this.msg = result.getMsg();
        this.data = result.getData();
    }

    public <T> Result(StrMSResult<T> result) {
        this.code = result.intCode();
        this.msg = result.getMsg();
        this.data = result.getData();
    }

//    public Result toResult() {
//        return new Result(this);
//    }

    public MSResult<Object> toMSResult() {
        return new MSResult<>(this);
    }

    public <T> MSResult<T> toMSResult(Class<T> dataType) {
        return new MSResult<>(this);
    }

    public StrResult toStrResult() {
        return new StrResult(this);
    }

    public StrMSResult<Object> toStrMSResult() {
        return new StrMSResult<>(this);
    }

    public <T> StrMSResult<T> toStrMSResult(Class<T> dataType) {
        return new StrMSResult<>(this);
    }


    //------------静态方法区-------------------------
    //------------common-----------
    public static Result put(int otherCode, String otherMsg, Object data) {
        return new Result(otherCode, otherMsg, data);
    }

    public static Result put(Constants constants, Object data) {
        return put(constants.intCode(), constants.getMsg(), data);
    }

    public static Result put(Constants constants) {
        return put(constants, null);
    }


    /**
     * @since 1.1.2
     */
    public static Result put(HttpStatus httpStatus, Object data) {
        return put(httpStatus.getCode(), httpStatus.getMsg(), data);
    }

    /**
     * @since 1.1.2
     */
    public static Result put(HttpStatus httpStatus) {
        return put(httpStatus, null);
    }


    public static <C> Result put(IResult<C, ?> resultData) {
        return put(resultData, resultData.getData());
    }

    public static <C, T> Result put(IResultData<C> resultData, Object data) {
        C code = resultData.getCode();
        if (code instanceof Integer) {
            return put((Integer) code, resultData.getMsg(), data);
        } else if (code instanceof String) {
            return put(ICommonResult.intCode((String) code), resultData.getMsg(), data);
        }
        return put(Constants.CODE_600.intCode(), resultData.getMsg(), data);
    }


    //------------own-------------------
    public static Result ok() {
        return put(Constants.CODE_200);
    }

    public static Result pass() {
        return put(Constants.CODE_200.intCode(), "", null);
    }

    public static Result notPass() {
        return put(Constants.CODE_500.intCode(), "", null);
    }

    //---success---
    public static Result success() {
        return put(Constants.CODE_200, null);
    }


    /**
     * 成功数据
     * <p>不重载String参数，以免混淆</p>
     */
    public static Result success(Object data) {
        return put(Constants.CODE_200, data);
    }

    public static Result success(String msg, Object data) {
        return put(Constants.CODE_200.intCode(), msg, data);
    }

    //---error---
    public static Result error() {
        return put(Constants.CODE_500, null);
    }

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     */
    public static Result errorMsg(String msg) {
        return put(Constants.CODE_500.intCode(), msg, null);
    }

    public static Result error(Object data) {
        return put(Constants.CODE_500, data);
    }

    public static Result error(int otherCode, String msg) {
        return put(otherCode, msg, null);
    }

    /**
     * 其他错误
     */
    public static Result error600(String msg, Object data) {
        return put(Constants.CODE_600.intCode(), msg, data);
    }

    // 常用 Result.error(错误码)

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     */
    public static Result errorBy(Constants constants) {
        return put(constants, null);
    }

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     */
    public static Result errorBy(Constants constants, String msg) {
        return error(constants.intCode(), msg);
    }

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     *
     * @since 1.1.2
     */
    public static Result errorBy(HttpStatus httpStatus) {
        return put(httpStatus, null);
    }

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     *
     * @since 1.1.2
     */
    public static Result errorBy(HttpStatus httpStatus, String msg) {
        return error(httpStatus.getCode(), msg);
    }

    //----------------ext------------

    /**
     * 扩展，添加 list
     **/
    public Result add(Object... items) {
        ICommonResult<Object> commonResult = () -> (Object) this.data;
        List<Object> list = commonResult.add(items);
        setData(list);
        return this;
    }

    /**
     * 扩展，添加 map
     */
    public Result putAll(Object... pairs) {
        ICommonResult<Object> commonResult = () -> (Object) this.data;
        Map<Object, Object> map = commonResult.put(pairs);
        setData(map);
        return this;
    }

    //-----------convertor---------------

    //------convertor 2------

    /**
     * @since 1.1.4
     */
    public <C> Result(IResultData<C> resultData) {
        if (resultData.getCode() == null) {
            this.code = Constants.CODE_200.intCode();
        } else if (resultData.getCode() instanceof Integer) {
            this.code = (Integer) resultData.getCode();
        } else {
            try {
                this.code = Integer.parseInt(resultData.getCode().toString());
            } catch (NumberFormatException e) {
                throw new NumberFormatException("code transform error : " + e.getMessage());
            }
        }
        this.msg = resultData.getMsg();
    }


    public <C, T> Result(IResult<C, T> result) {
        this((IResultData<C>) result);
        this.data = result.getData();
    }

    /**
     * 转到自定义类型
     * <p>fix bug</p>
     *
     * @param clazz 自定义类型，需继承自IResult
     * @return {@link P}
     * @updateFrom 1.0.3
     */
    public <P extends IResult<Integer, O>, O> P toCollect(Class<P> clazz) {
        return ClassMaker.castTarClass(this, clazz);
    }

    /**
     * 复制到自定义类型
     *
     * @param clazz 自定义类型，需继承自IResult
     * @return {@link P}
     */
    public <P extends IResult<Integer, O>, O> P toCollectByCopy(Class<P> clazz) {
        return BeanUtil.copyProperties(this, clazz);
    }


}
