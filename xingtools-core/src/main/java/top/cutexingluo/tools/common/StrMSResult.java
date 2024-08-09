package top.cutexingluo.tools.common;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.cutexingluo.tools.common.base.IResult;
import top.cutexingluo.tools.common.base.IResultData;
import top.cutexingluo.tools.common.base.XTIntCode;
import top.cutexingluo.tools.designtools.method.ClassMaker;

import java.util.List;
import java.util.Map;

/**
 * <p>兼容 String 类型的 code</p>
 * <p>支持泛型</p>
 *
 * @author XingTian
 * @version 1.1.1
 * @date 2023/7/13 23:14
 */
@EqualsAndHashCode(callSuper = true) // 1.0.5 fixed
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StrMSResult<T> extends CommonResult<String, T> implements XTIntCode {

    protected String code;
    protected String msg;
    protected T data;

    @Override
    public int intCode() {
        return Integer.parseInt(code);
    }

    //-----------convertor---------------
    public StrMSResult(Result result) {
        this.code = result.strCode();
        this.msg = result.getMsg();
        this.data = (T) result.getData();
    }

    public StrMSResult(MSResult<T> result) {
        this.code = result.strCode();
        this.msg = result.getMsg();
        this.data = result.getData();
    }

    public StrMSResult(StrResult result) {
        this.code = result.getCode();
        this.msg = result.getMsg();
        this.data = (T) result.getData();
    }

    public Result toResult() {
        return new Result(this);
    }

    public MSResult<Object> toMSResult() {
        return new MSResult<>((StrMSResult<Object>) this);
    }


    public <T> MSResult<T> toMSResult(Class<T> dataType) {
        return new MSResult<>((StrMSResult<T>) this);
    }

    public StrResult toStrResult() {
        return new StrResult(this);
    }
//    public StrMSResult<Object> toStrMSResult() {
//        return new StrMSResult<>((StrMSResult<Object>) this);
//    }
//
//    public <T> StrMSResult<T> toStrMSResult(Class<T> dataType) {
//        return new StrMSResult<>((StrMSResult<T>) this);
//    }


    //------------静态方法区-------------------------
    //------------common-----------
    public static <T> StrMSResult<T> put(String otherCode, String otherMsg, T data) {
        return new StrMSResult<T>(otherCode, otherMsg, data);
    }

    public static <T> StrMSResult<T> put(Constants constants, T data) {
        return put(constants.getCode(), constants.getMsg(), data);
    }

    public static StrMSResult<String> put(Constants constants) {
        return put(constants, "");
    }


    /**
     * @since 1.1.2
     */
    public static <T> StrMSResult<T> put(HttpStatus httpStatus, T data) {
        return put(httpStatus.strCode(), httpStatus.getMsg(), data);
    }

    /**
     * @since 1.1.2
     */
    public static StrMSResult<String> put(HttpStatus httpStatus) {
        return put(httpStatus, "");
    }

    public static <C, T> StrMSResult<T> put(IResult<C, T> resultData) {
        return put(resultData, resultData.getData());
    }

    public static <C, T> StrMSResult<T> put(IResultData<C> resultData, T data) {
        C code = resultData.getCode();
        if (code instanceof Integer) {
            return put(ICommonResult.strCode((Integer) code), resultData.getMsg(), data);
        } else if (code instanceof String) {
            return put((String) code, resultData.getMsg(), data);
        }
        return put(Constants.CODE_600.getCode(), resultData.getMsg(), data);
    }


    //------------own-------------------
    public static StrMSResult<String> ok() {
        return put(Constants.CODE_200);
    }

    public static <T> StrMSResult<T> pass() {
        return put(Constants.CODE_200.getCode(), "", null);
    }

    public static <T> StrMSResult<T> notPass() {
        return put(Constants.CODE_500, null);
    }

    //---success---
    public static StrMSResult<Boolean> success() {
        return put(Constants.CODE_200, true);
    }

    public static <T> StrMSResult<T> success(T data) {
        return put(Constants.CODE_200, data);
    }

    public static <T> StrMSResult<T> success(String msg, T data) {
        return put(Constants.CODE_200.getCode(), msg, data);
    }

    //---error---
    public static StrMSResult<Boolean> error() {
        return put(Constants.CODE_500, false);
    }

    public static StrMSResult<Boolean> error(String msg) {
        return put(Constants.CODE_500.getCode(), msg, false);
    }

    public static <T> StrMSResult<T> error(T data) {
        return put(Constants.CODE_500, data);
    }

    public static StrMSResult<Boolean> error(String otherCode, String msg) {
        return put(otherCode, msg, false);
    }

    /**
     * 其他错误
     */
    public static <T> StrMSResult<T> error600(String msg, T data) {
        return put(Constants.CODE_600.getCode(), msg, data);
    }

    // 常用 StrMSResult.error(错误码)
    public static StrMSResult<Boolean> error(Constants constants) {
        return put(constants, false);
    }

    public static StrMSResult<Boolean> error(Constants constants, String msg) {
        return error(constants.getCode(), msg);
    }

    /**
     * @since 1.1.2
     */
    public static StrMSResult<Boolean> error(HttpStatus httpStatus) {
        return put(httpStatus, false);
    }

    /**
     * @since 1.1.2
     */
    public static StrMSResult<Boolean> error(HttpStatus httpStatus, String msg) {
        return error(httpStatus.strCode(), msg);
    }

    //----------------ext------------

    /**
     * 扩展，添加 list
     **/
    public StrMSResult<T> add(Object... items) {
        ICommonResult<T> commonResult = () -> (T) this.data;
        List<Object> list = commonResult.add(items);
        setData((T) list);
        return this;
    }

    /**
     * 扩展，添加 map
     */
    public StrMSResult<T> putAll(Object... pairs) {
        ICommonResult<T> commonResult = () -> (T) this.data;
        Map<Object, Object> map = commonResult.put(pairs);
        setData((T) map);
        return this;
    }

    //-----------convertor---------------

    public <C> StrMSResult(IResult<C, T> result) {
        if (result.getCode() == null) {
            this.code = Constants.CODE_200.getCode();
        } else if (result.getCode() instanceof Integer) {
            this.code = String.valueOf(result.getCode());
        } else {
            try {
                this.code = result.getCode().toString();
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
    public <P extends IResult<String, O>, O> P toCollect(Class<P> clazz) {
        return ClassMaker.castTarClass(this, clazz);
    }

    /**
     * 复制到自定义类型
     *
     * @param clazz 自定义类型，需继承自IResult
     * @return {@link P}
     */
    public <P extends IResult<String, O>, O> P toCollectByCopy(Class<P> clazz) {
        return BeanUtil.copyProperties(this, clazz);
    }

}
