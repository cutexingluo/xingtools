package top.cutexingluo.tools.exception;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;
import top.cutexingluo.tools.common.Constants;
import top.cutexingluo.tools.common.Result;
import top.cutexingluo.tools.common.base.IResult;
import top.cutexingluo.tools.common.utils.GlobalResultFactory;
import top.cutexingluo.tools.start.log.LogInfoAuto;

/**
 * 业务拦截返回异常并封装返回
 *
 * @author XingTian
 * @version 1.0.2
 * @updateFrom 1.0.3
 */
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "global-exception", havingValue = "true", matchIfMissing = false)
@Slf4j
@RestControllerAdvice
public class XTGlobalExceptionHandler {
    public static final String TAG = " XTGlobalExceptionHandler" + ":";

    @Autowired(required = false)
    GlobalResultFactory globalResultFactory;

    //    @PostConstruct //  移除支持jdk 17
    public void init() {
        if (LogInfoAuto.enabled) log.info("XingTool GlobalExceptionHandler is enabled ---> {}", "全局异常拦截，已开启");
    }

    public XTGlobalExceptionHandler() {
        init();
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public <C, T> IResult<C, T> duplicateKeyException(DuplicateKeyException e) {
        log.error(TAG + "数据添加错误", e);
        IResult<Object, Object> result = GlobalResultFactory.selectResult(globalResultFactory, Result.error(Constants.CODE_500, "数据重复"));
        return (IResult<C, T>) result;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public <C, T> IResult<C, T> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg;
        try {
            msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        } catch (Exception be) {
            msg = "";
        }
        log.warn(TAG + "参数校验错误 -> {}", msg);
        IResult<Object, Object> result = GlobalResultFactory.selectResult(globalResultFactory, Result.error("参数校验错误"));
        return (IResult<C, T>) result;
    }

    @ExceptionHandler(value = ServiceException.class)
    public <C, T> IResult<C, T> serviceExceptionError(ServiceException e) {
        String code = e.getCode();
        if (StrUtil.isNotBlank(code)) {
            Result error = Result.error(e.intCode(), e.getMessage());
            IResult<Object, Object> result = GlobalResultFactory.selectResult(globalResultFactory, error);
            return (IResult<C, T>) result;
        }
        log.warn(TAG + "服务错误: " + e.getMessage());
//        log.warn("服务错误", e); // 不用打印在控制台
        Result error = Result.error(e.getMsg());
        IResult<Object, Object> result = GlobalResultFactory.selectResult(globalResultFactory, error);
        return (IResult<C, T>) result;
    }


    @ExceptionHandler(value = Exception.class)
    public <C, T> IResult<C, T> exceptionError(Exception e) {
        log.error(TAG + "未知错误 ", e);
        Result error = Result.error("内部未知错误");
        IResult<Object, Object> result = GlobalResultFactory.selectResult(globalResultFactory, error);
        return (IResult<C, T>) result;
    }
    /**
     *  too many ServiceException.class will be error
     */
//    /**
//     * @param se 业务异常
//     * @return Result
//     */
//    @Deprecated
//    @ExceptionHandler({ServiceException.class})
////    @ResponseBody
//    public Result handle(ServiceException se) {
//        return Result.error(se.getCode(), se.getMessage());
//    }
}
