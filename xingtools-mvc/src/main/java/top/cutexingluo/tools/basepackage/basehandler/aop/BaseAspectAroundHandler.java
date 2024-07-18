package top.cutexingluo.tools.basepackage.basehandler.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import top.cutexingluo.tools.designtools.method.ClassUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Aop around接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/16 15:01
 */
@FunctionalInterface
public interface BaseAspectAroundHandler<T> extends BaseJoinPointTaskHandler {

//    default Object around(@NotNull ProceedingJoinPoint joinPoint, T t) throws Throwable {
//        return joinPoint.proceed();
//    }

    Object around(@NotNull ProceedingJoinPoint joinPoint, T t) throws Throwable;


    /**
     * 重载方法
     * <p>适用其他情况</p>
     *
     * @since 1.0.4
     */
    default Object around(@NotNull ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    /**
     * 获得方法
     *
     * @see ClassUtil
     * @since 1.0.4
     */
    default Method getMethod(@NotNull ProceedingJoinPoint joinPoint) {
        return ClassUtil.getMethod(joinPoint);
    }

    /**
     * 获得注解
     *
     * @see ClassUtil
     * @since 1.0.4
     */
    default <A extends Annotation> A getAnnotation(Annotation annotation, Class<A> annotationType) {
        return ClassUtil.getAnnotation(annotation, annotationType);
    }

    /**
     * 获取方法上注解
     *
     * @see ClassUtil
     * @since 1.0.4
     */
    default <A extends Annotation> A getMethodAnnotation(Method method, Class<A> annotationType) {
        return ClassUtil.getAnnotation(method, annotationType);
    }

    /**
     * 获取类上的注解
     *
     * @param targetClass    目标类型
     * @param annotationType 注解类型
     * @return 注解对象
     * @see ClassUtil
     * @since 1.0.4
     */
    @Nullable
    default <A extends Annotation> A getClassAnnotation(@NotNull Class<?> targetClass, Class<A> annotationType) {
        return ClassUtil.getClassAnnotation(targetClass, annotationType);
    }


}
