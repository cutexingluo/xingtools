package top.cutexingluo.tools.exception;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.cutexingluo.core.common.base.IResult;
import top.cutexingluo.core.common.result.Constants;
import top.cutexingluo.core.common.result.Result;
import top.cutexingluo.core.common.utils.GlobalResultFactory;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;


/**
 * Sa-token 异常拦截
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/26 17:54
 * @updateFrom 1.0.3
 */
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "global-exception-ext", havingValue = "true", matchIfMissing = false)
@ConditionalOnClass({NotPermissionException.class, SaTokenException.class})
@Slf4j
@RestControllerAdvice
public class XTGlobalExceptionHandlerExt {
    public static final String TAG = " XTGlobalExceptionHandlerExt" + ":";

    @Autowired(required = false)
    GlobalResultFactory globalResultFactory;

    //    @PostConstruct //  移除支持jdk 17
    public void init() {
        log.info("XingTool GlobalExceptionHandlerExt is enabled ---> {}", "全局异常扩展拦截，已开启，支持sa-token");
    }

    public XTGlobalExceptionHandlerExt() {
        init();
    }

    @ConditionalOnClass(NotPermissionException.class)
    @ExceptionHandler(value = NotPermissionException.class)
    public <C, T> IResult<C, T> notPermissionException(NotPermissionException e) {
        log.error(TAG + "权限验证错误", e);
        Result error = Result.errorBy(Constants.CODE_401, "无权限");
        IResult<Object, Object> result = GlobalResultFactory.selectResult(globalResultFactory, error);
        return (IResult<C, T>) result;
    }

    @ConditionalOnClass(SaTokenException.class)
    @ExceptionHandler(value = SaTokenException.class)
    public <C, T> IResult<C, T> notLoginException(SaTokenException e) {
        log.error(TAG + "权限验证错误", e);
        Result error = Result.errorBy(Constants.CODE_401, "请登录");
        IResult<Object, Object> result = GlobalResultFactory.selectResult(globalResultFactory, error);
        return (IResult<C, T>) result;
    }
}
