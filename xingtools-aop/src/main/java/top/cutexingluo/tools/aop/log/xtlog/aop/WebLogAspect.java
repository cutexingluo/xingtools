package top.cutexingluo.tools.aop.log.xtlog.aop;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.utils.se.string.XTPickUtil;
import top.cutexingluo.tools.aop.log.xtlog.LogKey;
import top.cutexingluo.tools.aop.log.xtlog.base.WebLog;
import top.cutexingluo.tools.aop.log.xtlog.base.WebLogConfig;
import top.cutexingluo.tools.aop.log.xtlog.base.WebLogSetting;
import top.cutexingluo.tools.aop.log.xtlog.pkg.WebLogHandler;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectAroundHandler;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.designtools.method.ClassUtil;
import top.cutexingluo.tools.utils.ee.web.holder.HttpContextUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebLog AOP
 * <p>可自行注册，也可开启配置</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/11 17:10
 * @since 1.0.4
 */
@Aspect
public class WebLogAspect implements BaseAspectAroundHandler<WebLog> {

    /**
     * 可供外部使用配置 map
     */
    public Map<Class<?>, WebLogSetting> settingMap = new ConcurrentHashMap<>();

    // within is the class override the method, it's not excepted
    @Around("@annotation(webLog) || @within(webLog)")
    @Override
    public Object around(@NotNull ProceedingJoinPoint joinPoint, WebLog webLog) throws Throwable {
        // method
//        WebLog log = getAnnotation(webLog, WebLog.class);
        Method method = getMethod(joinPoint);
        WebLog log = ClassUtil.getMergedAnnotation(method, WebLog.class);
        if (log == null) { // unreachable
            return joinPoint.proceed();
        }
        WebLogConfig webLogConfig = null;

        String ret = null;

        if (!log.useSelf()) {
            // class
            Class<?> aClass = joinPoint.getTarget().getClass();
            WebLog type = ClassUtil.getMergedAnnotation(aClass, WebLog.class);
            if (type != null) { // has annotation
                ret = type.ret(); // return msg
                WebLogSetting setting = settingMap.get(aClass);
                if (setting != null) { // get setting
                    webLogConfig = new WebLogConfig(log, setting);
                } else {
                    WebLogSetting logSetting = new WebLogSetting(new WebLogConfig(type));
                    webLogConfig = new WebLogConfig(log, logSetting);
                    settingMap.put(aClass, logSetting);
                }
            }
        }
        if (webLogConfig == null) {
            ret = log.ret();// return msg
            webLogConfig = new WebLogConfig(log);
        }

        WebLogHandler handler = new WebLogHandler(webLogConfig).initDefaultMap().modifyAll();
        Callable<Object> task = getTask(joinPoint);
        AspectBundle bundle = new AspectBundle(getMethod(joinPoint), HttpContextUtil.getHttpServletRequestData(), joinPoint);
        Object result;
        if (log.before()) {
            handler.send(bundle);
            result = task.call();
            bundle.setCheckResult(result);
            sendRet(log, ret, handler, webLogConfig, bundle);
        } else {
            result = task.call();
            bundle.setCheckResult(result);
            sendRet(log, ret, handler, webLogConfig, bundle);
            handler.send(bundle);
        }
        return result;
    }

    /**
     * 组装返回值
     */
    protected void sendRet(@NotNull WebLog webLog, @NotNull String ret, WebLogHandler handler, WebLogConfig config, @NotNull AspectBundle bundle) {
        if (StrUtil.isBlank(ret)) return;
        if (webLog.before()) {
            handler.getMsgMap().put(LogKey.RET_KEY, 1, JSONUtil.toJsonStr(bundle.getResult()));
            config.setMsg(ret);  // 重置消息
            config.setSpEL(null); // 清空 SpEL
            handler.send(bundle); // 继续调用策略
        } else {
            String msg = XTPickUtil.putAllValueFromBraces(ret, (s) -> LogKey.RET_KEY.equals(s.trim()), JSONUtil.toJsonStr(bundle.getResult()));
            handler.getMsgMap().put(LogKey.RET_KEY, 1, msg);
        }
    }
}
