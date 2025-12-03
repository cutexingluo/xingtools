package top.cutexingluo.tools.utils.web.limit.submit.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.basehandler.aop.BaseAspectAroundHandler;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.designtools.method.ClassUtil;
import top.cutexingluo.tools.utils.ee.web.holder.HttpContextUtil;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimit;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitConfig;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitFactory;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitSetting;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.RequestLimitHandler;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LimitStrategyAspect
 * <p>限制策略切面抽象类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/5 17:38
 * @since 1.0.4
 */
@Aspect
public class RequestLimitAspect implements BaseAspectAroundHandler<RequestLimit> {

    /**
     * 可供外部使用配置 map (全局)
     */
    public Map<Class<?>, RequestLimitSetting> settingMap = new ConcurrentHashMap<>();

    // within is the class override the method, it's not excepted
    @Around("@annotation(requestLimit) || @within(requestLimit)")
    @Override
    public Object around(@NotNull ProceedingJoinPoint joinPoint, RequestLimit requestLimit) throws Throwable {
//        RequestLimit limit = getAnnotation(requestLimit, RequestLimit.class);
        Method method = getMethod(joinPoint);
        RequestLimit limit = ClassUtil.getMergedAnnotation(method, RequestLimit.class);
        if (limit == null) { // unreachable
            return joinPoint.proceed();
        }
        RequestLimitConfig requestLimitConfig = null;
        RequestLimitFactory limitFactory = new RequestLimitFactory(limit); // 配置工厂

        if (!limit.useSelf()) {
            Class<?> aClass = joinPoint.getTarget().getClass();
            RequestLimit classAnnotation = ClassUtil.getMergedAnnotation(aClass, RequestLimit.class);
            if (classAnnotation != null) {
                RequestLimitSetting setting = settingMap.get(aClass);
                if (setting != null) { // get setting
                    requestLimitConfig = limitFactory.getRequestLimitConfig(setting);
                } else {
                    limitFactory.setRequestLimit(classAnnotation); // 重新设置源注解
                    RequestLimitSetting limitSetting = new RequestLimitSetting(limitFactory.getRequestLimitConfig());
                    requestLimitConfig = limitFactory.getRequestLimitConfig(limitSetting);
                    settingMap.put(aClass, limitSetting);
                }
            }
        }
        if (requestLimitConfig == null) {
            requestLimitConfig = limitFactory.getRequestLimitConfig();
        }


        // handler
        RequestLimitHandler limitHandler = new RequestLimitHandler(requestLimitConfig);
        AspectBundle bundle = new AspectBundle(getMethod(joinPoint), HttpContextUtil.getHttpServletRequestData(), joinPoint);
        boolean pass = limitHandler.limit(bundle);
        Object result = null;
        if (pass) { // pass
            result = getTask(joinPoint).call();
            limitHandler.afterDone(bundle);
        }
        return result;
    }


}
