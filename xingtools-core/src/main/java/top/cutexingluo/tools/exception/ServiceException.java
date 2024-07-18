package top.cutexingluo.tools.exception;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.Constants;
import top.cutexingluo.tools.common.Result;
import top.cutexingluo.tools.common.StrResult;
import top.cutexingluo.tools.common.base.IResultData;
import top.cutexingluo.tools.common.base.XTIntCode;

/**
 * <p> xingtool 通用异常 </p>
 * <p>1. 自定义异常，抛出对象，会被拦截封装</p>
 * <p>2. 实现了IResultData, 可以直接转为 Result</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/16
 * @since 1.0.0
 */
@Getter
public class ServiceException extends RuntimeException implements IResultData<String>, XTIntCode {
    protected static final long serialVersionUID = 1L;
    /**
     * 错误码
     */
    protected String code;

    /**
     * 服务异常
     *
     * @param code 代码
     * @param msg  消息
     */
    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    /**
     * 服务异常
     *
     * @param code 代码
     * @param msg  消息
     */
    public ServiceException(int code, String msg) {
        super(msg);
        this.code = String.valueOf(code);
    }


    /**
     * 服务异常
     *
     * @param constantsCode 错误常量
     * @param msg           消息
     */
    public ServiceException(@NotNull Constants constantsCode, String msg) {
        super(msg);
        this.code = constantsCode.getCode();
    }

    /**
     * 服务异常
     *
     * @param resultData 数据接口
     */
    public <T> ServiceException(@NotNull IResultData<T> resultData) {
        super(resultData.getMsg());
        T code = resultData.getCode();
        if (code instanceof Integer) {
            this.code = String.valueOf(code);
        } else {
            this.code = code.toString();
        }
    }

    /**
     * 需要指定异常码
     */
    @Deprecated
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 需要指定异常码
     */
    @Deprecated
    public ServiceException(String message) {
        super(message);
    }

    @Override
    public String getMsg() {
        return super.getMessage();
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
