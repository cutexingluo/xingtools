package top.cutexingluo.tools.designtools.method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * ClassUtil 工具类
 * <p>基于 spring 工具的 class 工具类</p>
 * <p>主要是注解工具类</p>
 * <p>与 {@link XTMethodUtil} 类似功能</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/20 14:19
 * @see XTMethodUtil
 * @see AnnotationUtils
 * @see AnnotatedElementUtils
 * @since 1.0.4
 */
public class ClassUtil extends ClassUtils {
    public static final ParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 获取方法参数信息
     *
     * @param constructor    构造器
     * @param parameterIndex 参数序号
     */
    @NotNull
    public static MethodParameter getMethodParameter(Constructor<?> constructor, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(constructor, parameterIndex);
        methodParameter.initParameterNameDiscovery(DISCOVERER);
        return methodParameter;
    }

    /**
     * 获取方法参数信息
     *
     * @param method         方法
     * @param parameterIndex 参数序号
     */
    @NotNull
    public static MethodParameter getMethodParameter(Method method, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(method, parameterIndex);
        methodParameter.initParameterNameDiscovery(DISCOVERER);
        return methodParameter;
    }


    /**
     * 获取桥接方法
     * <p>If we are dealing with method with generic parameters, find the original method.</p>
     * <p>如果我们处理的是带有泛型参数的方法，请找到原始方法。</p>
     *
     * @param specificMethod 精确方法
     */
    @NotNull
    public static Method findBridgedMethod(Method specificMethod) {
        return BridgeMethodResolver.findBridgedMethod(specificMethod);
    }

    /**
     * 获取更加精确的方法
     * <p>精确+ 桥接</p>
     *
     * @param method      方法
     * @param targetClass 目标类
     */
    @NotNull
    public static Method getBridgedSpecificMethod(@NotNull Method method, Class<?> targetClass) {
        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the original method.
        specificMethod = findBridgedMethod(specificMethod);
        return specificMethod;
    }

    /**
     * 获取更加精确的方法
     * <p>精确+ 桥接</p>
     *
     * @param method 方法
     */
    @NotNull
    public static Method getBridgedSpecificMethod(@NotNull Method method) {
        Class<?> targetClass = method.getDeclaringClass();
        return getBridgedSpecificMethod(method, targetClass);
    }


    /**
     * 获取组合注解Annotation
     * <p> 获取方法上面的组合注解，可能包含组合注解</p>
     *
     * @param method         Method
     * @param annotationType 注解类
     */
    @Nullable
    public static <A extends Annotation> A getMergedAnnotation(@NotNull Method method, Class<A> annotationType) {
        return AnnotatedElementUtils.findMergedAnnotation(method, annotationType);
    }

    /**
     * 获取组合注解Annotation
     * <p> 获取类上面的组合注解，可能包含组合注解</p>
     *
     * @param targetClass    目标类
     * @param annotationType 注解类
     */
    @Nullable
    public static <A extends Annotation> A getMergedAnnotation(@NotNull Class<?> targetClass, Class<A> annotationType) {
        return AnnotatedElementUtils.findMergedAnnotation(targetClass, annotationType);
    }

    /**
     * 获取组合注解Annotation
     * <p> 获取元素上面的组合注解，可能包含组合注解</p>
     *
     * @param element        目标元素
     * @param annotationType 注解类
     */
    @Nullable
    public static <A extends Annotation> A getMergedAnnotation(@NotNull AnnotatedElement element, Class<A> annotationType) {
        return AnnotatedElementUtils.findMergedAnnotation(element, annotationType);
    }


    /**
     * 获得方法
     */
    @Nullable
    public static Method getMethod(@NotNull ProceedingJoinPoint joinPoint) {
        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            return methodSignature.getMethod();
        }
        return null;
    }

    /**
     * 直接获得方法
     * <p>直接转型</p>
     */
    @Nullable
    public static Method getCheckMethod(@NotNull ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod();
    }

    /**
     * 获得解析后的注解
     * <p>spring 解析通用方法</p>
     */
    public static <A extends Annotation> A getAnnotation(Annotation annotation, Class<A> annotationType) {
        return AnnotationUtils.getAnnotation(annotation, annotationType);
    }


    /**
     * 获取解析后的注解
     *
     * @param annotatedElement 目标可注解类型
     * @param annotationType   注解类型
     * @return 注解对象
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(@NotNull AnnotatedElement annotatedElement, Class<A> annotationType) {
        A annotation = annotatedElement.getAnnotation(annotationType);
        return annotation != null ? getAnnotation(annotation, annotationType) : null;
    }

    /**
     * 获取类上的解析后的注解
     *
     * @param targetClass    目标类型
     * @param annotationType 注解类型
     * @return 注解对象
     */
    @Nullable
    public static <A extends Annotation> A getClassAnnotation(@NotNull Class<?> targetClass, Class<A> annotationType) {
        A annotation = targetClass.getAnnotation(annotationType);
        return annotation != null ? getAnnotation(annotation, annotationType) : null;
    }

    /**
     * 获取类上的解析后的注解
     * <p>调用 getClassAnnotation</p>
     *
     * @param targetClass    目标类型
     * @param annotationType 注解类型
     * @return 注解对象
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(@NotNull Class<?> targetClass, Class<A> annotationType) {
        return getClassAnnotation(targetClass, annotationType);
    }

    /**
     * 获取方法上的解析后的注解
     *
     * @param method         目标类型
     * @param annotationType 注解类型
     * @return 注解对象
     */
    @Nullable
    public static <A extends Annotation> A getMethodAnnotation(@NotNull Method method, Class<A> annotationType) {
        A annotation = method.getAnnotation(annotationType);
        return annotation != null ? getAnnotation(annotation, annotationType) : null;
    }

    /**
     * 获取方法上的解析后的注解
     * <p>调用 getMethodAnnotation</p>
     *
     * @param method         目标类型
     * @param annotationType 注解类型
     * @return 注解对象
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(@NotNull Method method, Class<A> annotationType) {
        return getMethodAnnotation(method, annotationType);
    }


    /**
     * 获取参数上的解析后的注解
     *
     * @param param          目标参数
     * @param annotationType 注解类型
     * @return 注解对象
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(@NotNull Parameter param, Class<A> annotationType) {
        A annotation = param.getAnnotation(annotationType);
        return annotation != null ? getAnnotation(annotation, annotationType) : null;
    }

    /**
     * 获取字段上的解析后的注解
     *
     * @param field          目标字段
     * @param annotationType 注解类型
     * @return 注解对象
     */
    @Nullable
    public static <A extends Annotation> A getAnnotation(@NotNull Field field, Class<A> annotationType) {
        A annotation = field.getAnnotation(annotationType);
        return annotation != null ? getAnnotation(annotation, annotationType) : null;
    }


    //--------------组合方法-------------------------


    /**
     * 直接从 joinPoint 获得方法上的注解
     */
    @Nullable
    public static <A extends Annotation> A getMethodAnnotation(@NotNull ProceedingJoinPoint joinPoint, Class<A> annotationType) {
        Method method = getMethod(joinPoint);
        if (method != null) {
            return getMethodAnnotation(method, annotationType);
        }
        return null;
    }


    /**
     * 直接从 joinPoint 获得类上的注解
     */
    @Nullable
    public static <A extends Annotation> A getClassAnnotation(@NotNull ProceedingJoinPoint joinPoint, Class<A> annotationType) {
        Class<?> aClass = joinPoint.getTarget().getClass();
        return getClassAnnotation(aClass, annotationType);
    }


    /**
     * 获取确切的Annotation
     * <p> 如果方法上没有组合注解，则查找类上注解</p>
     *
     * @param method         Method
     * @param annotationType 注解类
     */
    public static <A extends Annotation> A getDetailMergedAnnotation(@NotNull Method method, Class<A> annotationType) {
        Method specificMethod = getBridgedSpecificMethod(method);
        A annotation = getMergedAnnotation(specificMethod, annotationType);
        return annotation != null ? annotation : getMergedAnnotation(specificMethod.getDeclaringClass(), annotationType);
    }

    /**
     * 获取确切的Annotation
     * <p> 如果方法上没有组合注解，则查找类上注解</p>
     *
     * @param handlerMethod  HandlerMethod
     * @param annotationType 注解类
     */
    public static <A extends Annotation> A getHandlerMethodAnnotation(@NotNull HandlerMethod handlerMethod, Class<A> annotationType) {
        A annotation = handlerMethod.getMethodAnnotation(annotationType);
        if (null != annotation) {
            return annotation;
        }
        Class<?> beanType = handlerMethod.getBeanType();
        return getMergedAnnotation(beanType, annotationType);
    }

}
