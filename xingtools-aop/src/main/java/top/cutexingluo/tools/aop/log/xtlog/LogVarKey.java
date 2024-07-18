package top.cutexingluo.tools.aop.log.xtlog;

import cn.hutool.json.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import top.cutexingluo.tools.aop.log.xtlog.pkg.LogKeyHttpConvertor;
import top.cutexingluo.tools.designtools.convert.KeyHttpManager;


/**
 * WebLog 常量转化器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/12 10:31
 * @since 1.0.4
 */
public class LogVarKey extends KeyHttpManager {


    /**
     * bName
     */
    public static final LogKeyHttpConvertor B_NAME = new LogKeyHttpConvertor("bName", (config, bundle) -> config == null ? "" : config.getBusinessName());

    /**
     * sName -> getSignature getName
     */
    public static final LogKeyHttpConvertor S_NAME = new LogKeyHttpConvertor("sName", (config, bundle) -> {
        if (bundle == null) return "";
        ProceedingJoinPoint joinPoint = bundle.getJoinPoint();
        if (joinPoint == null || joinPoint.getSignature() == null) {
            return "";
        }
        return joinPoint.getSignature().getName();
    });

    /**
     * sName -> getSignature getDeclaringTypeName
     */
    public static final LogKeyHttpConvertor S_TYPE_NAME = new LogKeyHttpConvertor("sTypeName", (config, bundle) -> {
        if (bundle == null) return "";
        ProceedingJoinPoint joinPoint = bundle.getJoinPoint();
        if (joinPoint == null || joinPoint.getSignature() == null) {
            return "";
        }
        return joinPoint.getSignature().getDeclaringTypeName();
    });

    /**
     * ClassName -> getSignature getDeclaringTypeName + getName
     */
    public static final LogKeyHttpConvertor CLASS_METHOD = new LogKeyHttpConvertor("classMethod", (config, bundle) -> {
        if (bundle == null) return "";
        ProceedingJoinPoint joinPoint = bundle.getJoinPoint();
        if (joinPoint == null || joinPoint.getSignature() == null) {
            return "";
        }
        Signature signature = joinPoint.getSignature();
        return signature.getDeclaringTypeName() + ". " + signature.getName();
    });

    /**
     * args -> getArgs
     */
    public static final LogKeyHttpConvertor ARGS = new LogKeyHttpConvertor("args", (config, bundle) -> {
        if (bundle == null) return "";
        ProceedingJoinPoint joinPoint = bundle.getJoinPoint();
        if (joinPoint == null) {
            return "";
        }
        return JSONUtil.toJsonStr(joinPoint.getArgs());
    });

}
