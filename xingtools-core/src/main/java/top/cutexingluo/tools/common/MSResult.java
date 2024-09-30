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
import top.cutexingluo.tools.common.base.XTStrCode;
import top.cutexingluo.tools.designtools.method.ClassMaker;

import java.util.List;
import java.util.Map;

/**
 * <p>兼容 int 类型的 code</p>
 * <p>支持泛型</p>
 *
 * @author XingTian
 * @version 1.1.1
 * @date 2023/7/13 20:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MSResult<T> extends CommonResult<Integer, T> implements XTStrCode {

    protected int code;
    protected String msg;
    protected T data;

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public MSResult<T> setCode(Integer code) {
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
    public MSResult(Result result) {
        this.code = result.getCode();
        this.msg = result.getMsg();
        this.data = (T) result.getData();
    }

    public MSResult(StrResult result) {
        this.code = result.intCode();
        this.msg = result.getMsg();
        this.data = (T) result.getData();
    }

    public MSResult(StrMSResult<T> result) {
        this.code = result.intCode();
        this.msg = result.getMsg();
        this.data = result.getData();
    }

    public Result toResult() {
        return new Result(this);
    }

//    public MSResult<Object> toMSResult() {
//        return new MSResult<>(this);
//    }
//
//    public <T> MSResult<T> toMSResult(Class<T> dataType) {
//        return new MSResult<>(this);
//    }

    public StrResult toStrResult() {
        return new StrResult(this);
    }

    public StrMSResult<Object> toStrMSResult() {
        return new StrMSResult<>((MSResult<Object>) this);
    }

    public <T> StrMSResult<T> toStrMSResult(Class<T> dataType) {
        return new StrMSResult<>((MSResult<T>) this);
    }

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

    public static <T> MSResult<T> put(int otherCode, String otherMsg, T data) {
        return new MSResult<T>(otherCode, otherMsg, data);
    }

    public static <T> MSResult<T> put(Constants constants, T data) {
        return put(constants.intCode(), putMsgOrName(constants), data);
    }

    public static MSResult<String> put(Constants constants) {
        return put(constants, null);
    }

    /**
     * @since 1.1.2
     */
    public static <T> MSResult<T> put(HttpStatus httpStatus, T data) {
        return put(httpStatus.getCode(), putMsgOrName(httpStatus), data);
    }

    /**
     * @since 1.1.2
     */
    public static MSResult<String> put(HttpStatus httpStatus) {
        return put(httpStatus, null);
    }

    public static <C, T> MSResult<T> put(IResult<C, T> resultData) {
        return put(resultData, resultData.getData());
    }

    public static <C, T> MSResult<T> put(IResultData<C> resultData, T data) {
        C code = resultData.getCode();
        if (code instanceof Integer) {
            return put((Integer) code, resultData.getMsg(), data);
        } else if (code instanceof String) {
            return put(ICommonResult.intCode((String) code), resultData.getMsg(), data);
        }
        return put(Constants.CODE_600.intCode(), resultData.getMsg(), data);
    }


    //------------own-------------------
    public static MSResult<String> ok() {
        return put(Constants.CODE_200);
    }

    public static <T> MSResult<T> pass() {
        return put(Constants.CODE_200.intCode(), "", null);
    }

    public static <T> MSResult<T> notPass() {
        return put(Constants.CODE_500.intCode(), "", null);
    }

    //---success---
    public static MSResult<Boolean> success() {
        return put(Constants.CODE_200, null);
    }

    public static <T> MSResult<T> success(T data) {
        return put(Constants.CODE_200, data);
    }

    public static <T> MSResult<T> success(String msg, T data) {
        return put(Constants.CODE_200.intCode(), msg, data);
    }

    //---error---
    public static MSResult<Boolean> error() {
        return put(Constants.CODE_500, null);
    }

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     */
    public static MSResult<Boolean> errorMsg(String msg) {
        return put(Constants.CODE_500.intCode(), msg, null);
    }

    public static <T> MSResult<T> error(T data) {
        return put(Constants.CODE_500, data);
    }

    public static MSResult<Boolean> error(int otherCode, String msg) {
        return put(otherCode, msg, null);
    }

    /**
     * 其他错误
     */
    public static <T> MSResult<T> error600(String msg, T data) {
        return put(Constants.CODE_600.intCode(), msg, data);
    }

    // 常用 MSResult.error(错误码)

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     */
    public static MSResult<Boolean> errorBy(Constants constants) {
        return put(constants, null);
    }

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     */
    public static MSResult<Boolean> errorBy(Constants constants, String msg) {
        return error(constants.intCode(), msg);
    }

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     *
     * @since 1.1.2
     */
    public static MSResult<Boolean> errorBy(HttpStatus httpStatus) {
        return put(httpStatus, null);
    }

    /**
     * <p>于v1.1.5 更新为 errorMsg , 防止冲突</p>
     *
     * @since 1.1.2
     */
    public static MSResult<Boolean> errorBy(HttpStatus httpStatus, String msg) {
        return error(httpStatus.getCode(), msg);
    }

    //----------------ext------------

    /**
     * 扩展，添加 list
     **/
    public MSResult<T> add(Object... items) {
        ICommonResult<T> commonResult = () -> (T) this.data;
        List<Object> list = commonResult.add(items);
        setData((T) list);
        return this;
    }

    /**
     * 扩展，添加 map
     */
    public MSResult<T> putAll(Object... pairs) {
        ICommonResult<T> commonResult = () -> (T) this.data;
        Map<Object, Object> map = commonResult.put(pairs);
        setData((T) map);
        return this;
    }
    //-----------convertor---------------

    /**
     * @since 1.1.4
     */
    public <C> MSResult(IResultData<C> resultData) {
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


    public <C> MSResult(IResult<C, T> result) {
        this((IResultData<C>) result);
        this.data = result.getData();
    }

    /**
     * 转到R扩展类型
     * <p>fix bug</p>
     *
     * @since 1.0.3
     */
    public R<T> toR() {
        return new R<>(this);
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
