package top.cutexingluo.tools.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.Constants;
import top.cutexingluo.tools.common.Result;
import top.cutexingluo.tools.common.StrResult;
import top.cutexingluo.tools.common.base.IResultData;
import top.cutexingluo.tools.common.base.XTIntCode;

/**
 * 配置空指针异常
 * <p>一般用于配置项为Null, 导致的空指针异常</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/12 10:34
 * @since 1.1.1
 */
@Getter
public class ConfigNullPointerException extends NullPointerException  implements IResultData<String>, XTIntCode {
    /**
     * 错误码
     */
    protected String code;

    /**
     * 配置空指针异常
     *
     * @param code 代码
     * @param msg  消息
     */
    public ConfigNullPointerException(String code,String msg) {
        super(msg);
        this.code = code;
    }

    /**
     * 配置空指针异常
     *
     * @param code 代码
     * @param msg  消息
     */
    public ConfigNullPointerException(int code,String msg) {
        super(msg);
        this.code =  String.valueOf(code);
    }
    /**
     * 配置空指针异常
     *
     * @param constantsCode 错误常量
     * @param msg  消息
     */
    public ConfigNullPointerException(@NotNull Constants constantsCode, String msg) {
        super(msg);
        this.code = constantsCode.getCode();
    }
    /**
     * 配置空指针异常
     *
     * @param resultData 数据接口
     */
    public <T> ConfigNullPointerException(@NotNull IResultData<T> resultData) {
        super(resultData.getMsg());
        T code = resultData.getCode();
        this.code = code == null ? Constants.CODE_500.getCode() : code.toString();
    }


    @Override
    public String getMsg() {
        return super.getMessage();
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int intCode() {
        return Integer.parseInt(code);
    }

    /**
     * 获取两个常用的 StrResult 和  Result 对象
     *
     * @param codeType code 类型
     * @since 1.0.4
     */
    public <T> IResultData<T> getData(Class<T> codeType) {
        if (codeType == null || String.class.equals(codeType)) {
            StrResult result = new StrResult(this.getCode(), this.getMsg(), null);
            return (IResultData<T>) result;
        } else if (Integer.class.equals(codeType)) {
            Result result = new Result(this.intCode(), this.getMsg(), null);
            return (IResultData<T>) result;
        }
        return null;
    }


}
