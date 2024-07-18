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
 *
 * @author XingTian
 * @version 1.1.1
 * @date 2023/7/13 23:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StrResult extends CommonResult<String, Object> implements XTIntCode {
    protected String code;
    protected String msg;
    protected Object data;

    @Override
    public int intCode() {
        return Integer.parseInt(code);
    }
    //-----------convertor---------------

    public StrResult(Result result) {
        this.code = result.strCode();
        this.msg = result.getMsg();
        this.data = result.getData();
    }

    public <T> StrResult(MSResult<T> result) {
        this.code = result.strCode();
        this.msg = result.getMsg();
        this.data = result.getData();
    }

    public <T> StrResult(StrMSResult<T> result) {
        this.code = result.getCode();
        this.msg = result.getMsg();
        this.data = result.getData();
    }

    public Result toResult() {
        return new Result(this);
    }

    public MSResult<Object> toMSResult() {
        return new MSResult<>(this);
    }

    public <T> MSResult<T> toMSResult(Class<T> dataType) {
        return new MSResult<>(this);
    }

//    public StrResult toStrResult() {
//        return new StrResult(this);
//    }

    public StrMSResult<Object> toStrMSResult() {
        return new StrMSResult<>(this);
    }

    public <T> StrMSResult<T> toStrMSResult(Class<T> dataType) {
        return new StrMSResult<>(this);
    }

    //------------静态方法区-------------------------
    //------------common-----------
    public static StrResult put(String otherCode, String otherMsg, Object data) {
        return new StrResult(otherCode, otherMsg, data);
    }

    public static StrResult put(Constants constants, Object data) {
        return put(constants.getCode(), constants.getMsg(), data);
    }

    public static StrResult put(Constants constants) {
        return put(constants, "");
    }

    public static <C> StrResult put(IResult<C, Object> resultData) {
        return put(resultData, resultData.getData());
    }

    public static <C, T> StrResult put(IResultData<C> resultData, Object data) {
        C code = resultData.getCode();
        if (code instanceof Integer) {
            return put(ICommonResult.strCode((Integer) code), resultData.getMsg(), data);
        } else if (code instanceof String) {
            return put((String) code, resultData.getMsg(), data);
        }
        return put(Constants.CODE_600.getCode(), resultData.getMsg(), data);
    }


    //------------own-------------------
    public static StrResult ok() {
        return put(Constants.CODE_200);
    }

    public static StrResult pass() {
        return put(Constants.CODE_200.getCode(), "", null);
    }

    public static StrResult notPass() {
        return put(Constants.CODE_500, null);
    }

    //---success---
    public static StrResult success() {
        return put(Constants.CODE_200, true);
    }

    public static StrResult success(Object data) {
        return put(Constants.CODE_200, data);
    }

    public static StrResult success(String msg, Object data) {
        return put(Constants.CODE_200.getCode(), msg, data);
    }

    //---error---
    public static StrResult error() {
        return put(Constants.CODE_500, false);
    }

    public static StrResult error(String msg) {
        return put(Constants.CODE_500.getCode(), msg, false);
    }

    public static StrResult error(Object data) {
        return put(Constants.CODE_500, data);
    }

    public static StrResult error(String otherCode, String msg) {
        return put(otherCode, msg, false);
    }

    /**
     * 其他错误
     */
    public static StrResult error600(String msg, Object data) {
        return put(Constants.CODE_600.getCode(), msg, data);
    }

    // 常用 StrResult.error(错误码)
    public static StrResult error(Constants constants) {
        return put(constants, false);
    }

    public static StrResult error(Constants constants, String msg) {
        return error(constants.getCode(), msg);
    }

    //----------------ext------------

    /**
     * 扩展，添加 list
     **/
    public StrResult add(Object... items) {
        ICommonResult<Object> commonResult = () -> (Object) this.data;
        List<Object> list = commonResult.add(items);
        setData(list);
        return this;
    }

    /**
     * 扩展，添加 map
     */
    public StrResult putAll(Object... pairs) {
        ICommonResult<Object> commonResult = () -> (Object) this.data;
        Map<Object, Object> map = commonResult.put(pairs);
        setData(map);
        return this;
    }

    //-----------convertor---------------

    public <C, T> StrResult(IResult<C, T> result) {
        if (result.getCode() == null) {
            this.code = Constants.CODE_200.getCode();
        } else if (result.getCode() instanceof Integer) {
            this.code = String.valueOf(result.getCode());
        } else {
            try {
                this.code = result.getCode().toString();
            } catch (NumberFormatException e) {
                throw new NumberFormatException("code transform eror ==> " + e.getMessage());
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
