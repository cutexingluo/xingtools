package top.cutexingluo.tools.common;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.cutexingluo.tools.common.base.IRName;
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

    //------------配置区-------------------------

    /**
     * 默认的 msg 是否使用 msg
     *
     * <p>msg 即中文, name 即英文</p>
     *
     * @since 1.1.5
     */
    public static boolean useMsgOrName = true;

    //------------静态方法区-------------------------
    //------------common-----------

    /**
     * 得到 msg 或者 name
     *
     * @since 1.1.5
     */
    public static String putMsgOrName(IRName rn) {
        return useMsgOrName ? rn.getMsg() : rn.getName();
    }

    public static <T> StrMSResult<T> put(String otherCode, String otherMsg, T data) {
        return new StrMSResult<T>(otherCode, otherMsg, data);
    }

    public static <T> StrMSResult<T> put(Constants constants, T data) {
        return put(constants.getCode(), putMsgOrName(constants), data);
    }

    public static StrMSResult<String> put(Constants constants) {
        return put(constants, null);
    }


    /**
     * @since 1.1.2
     */
    public static <T> StrMSResult<T> put(HttpStatus httpStatus, T data) {
        return put(httpStatus.strCode(), putMsgOrName(httpStatus), data);
    }

    /**
     * @since 1.1.2
     */
    public static StrMSResult<String> put(HttpStatus httpStatus) {
        return put(httpStatus, null);
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
        return put(Constants.CODE_500.getCode(), "", null);
    }

    //---success---
    public static StrMSResult<Boolean> success() {
        return put(Constants.CODE_200, null);
    }

    public static <T> StrMSResult<T> success(T data) {
        return put(Constants.CODE_200, data);
    }

    public static <T> StrMSResult<T> success(String msg, T data) {
        return put(Constants.CODE_200.getCode(), msg, data);
    }

    //---error---
    public static StrMSResult<Boolean> error() {
        return put(Constants.CODE_500, null);
    }

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     */
    public static StrMSResult<Boolean> errorMsg(String msg) {
        return put(Constants.CODE_500.getCode(), msg, null);
    }

    public static <T> StrMSResult<T> error(T data) {
        return put(Constants.CODE_500, data);
    }

    public static StrMSResult<Boolean> error(String otherCode, String msg) {
        return put(otherCode, msg, null);
    }

    /**
     * 其他错误
     */
    public static <T> StrMSResult<T> error600(String msg, T data) {
        return put(Constants.CODE_600.getCode(), msg, data);
    }

    // 常用 StrMSResult.error(错误码)

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     */
    public static StrMSResult<Boolean> errorBy(Constants constants) {
        return put(constants, null);
    }

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     */
    public static StrMSResult<Boolean> errorBy(Constants constants, String msg) {
        return error(constants.getCode(), msg);
    }

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     *
     * @since 1.1.2
     */
    public static StrMSResult<Boolean> errorBy(HttpStatus httpStatus) {
        return put(httpStatus, null);
    }

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     *
     * @since 1.1.2
     */
    public static StrMSResult<Boolean> errorBy(HttpStatus httpStatus, String msg) {
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

    /**
     * @since 1.1.4
     */
    public <C> StrMSResult(IResultData<C> resultData) {
        if (resultData.getCode() == null) {
            this.code = Constants.CODE_200.getCode();
        } else if (resultData.getCode() instanceof Integer) {
            this.code = String.valueOf(resultData.getCode());
        } else {
            try {
                this.code = resultData.getCode().toString();
            } catch (NullPointerException e) {
                throw new NullPointerException("code transform error : " + e.getMessage());
            }
        }
        this.msg = resultData.getMsg();
    }

    public <C> StrMSResult(IResult<C, T> result) {
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
