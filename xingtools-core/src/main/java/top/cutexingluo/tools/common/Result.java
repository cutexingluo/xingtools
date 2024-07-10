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
        return put(constants, "");
    }

    public static <C> Result put(IResult<C, Object> resultData) {
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
        return put(Constants.CODE_500, null);
    }

    //---success---
    public static Result success() {
        return put(Constants.CODE_200, true);
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
        return put(Constants.CODE_500, false);
    }

    public static Result error(String msg) {
        return put(Constants.CODE_500.intCode(), msg, false);
    }

    public static Result error(Object data) {
        return put(Constants.CODE_500, data);
    }

    public static Result error(int otherCode, String msg) {
        return put(otherCode, msg, false);
    }

    /**
     * 其他错误
     */
    public static Result error600(String msg, Object data) {
        return put(Constants.CODE_600.intCode(), msg, data);
    }

    // 常用 Result.error(错误码)
    public static Result error(Constants constants) {
        return put(constants, false);
    }

    public static Result error(Constants constants, String msg) {
        return error(constants.intCode(), msg);
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

    public <C, T> Result(IResult<C, T> result) {
        if (result.getCode() == null) {
            this.code = Constants.CODE_200.intCode();
        } else if (result.getCode() instanceof Integer) {
            this.code = (Integer) result.getCode();
        } else {
            try {
                this.code = Integer.parseInt(result.getCode().toString());
            } catch (NumberFormatException e) {
                throw new NumberFormatException("code transform error ==> " + e.getMessage());
            }
        }
        this.msg = result.getMsg();
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
