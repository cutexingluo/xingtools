package top.cutexingluo.tools.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.base.IResult;
import top.cutexingluo.tools.common.base.IResultData;
import top.cutexingluo.tools.common.base.IResultSource;

/**
 * IResultSource 实现类，基本封装类
 * <p>目的是统一四大 IResult 实现类，并且提供通用数据结构体</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 23:31
 * @since 1.0.3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<C, T> implements IResultSource<C, T> {

    protected C code;
    protected String msg;
    protected T data;

    /**
     * @since 1.1.4
     */
    public CommonResult(@NotNull IResultData<C> resultData) {
        this.code = resultData.getCode();
        this.msg = resultData.getMsg();
    }

    public CommonResult(@NotNull IResult<C, T> result) {
        this.code = result.getCode();
        this.msg = result.getMsg();
        this.data = result.getData();
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public C getCode() {
        return code;
    }

    @Override
    public CommonResult<C, T> setCode(C code) {
        this.code = code;
        return this;
    }

    @Override
    public CommonResult<C, T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public CommonResult<C, T> setData(T data) {
        this.data = data;
        return this;
    }
}
