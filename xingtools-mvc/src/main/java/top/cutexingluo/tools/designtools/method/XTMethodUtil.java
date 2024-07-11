package top.cutexingluo.tools.designtools.method;

import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 方法工具类
 * <p>对象操作器</p>
 * <p>与 {@link top.cutexingluo.tools.designtools.method.ClassUtil} 类似功能</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/21 18:48
 * @updateFrom 1.0.4
 * @see top.cutexingluo.tools.designtools.method.ClassUtil
 */
public class XTMethodUtil {

    /**
     * 获取 handler 上是否存在该注解
     * <p> 如果 目标对象不是HandlerMethod 或 不存在注解 则返回false</p>
     * <br>
     * 常用于拦截器
     *
     * @param handler         处理器 (instanceof HandlerMethod)
     * @param annotationClass 注释类
     * @return boolean
     * @updateFrom 1.0.4
     * @update 2024/1/5
     */
    public static <T> boolean isHandlerMethodAnnotationPresent(T handler, Class<? extends Annotation> annotationClass) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            return method.isAnnotationPresent(annotationClass);
        }
        return false;
    }

    /**
     * 获取 method 上是否存在该注解
     *
     * @param method          方法
     * @param annotationClass 注释类
     * @return boolean
     */
    @Contract(pure = true)
    public static <T> boolean isAnnotationPresent(@NotNull Method method, Class<? extends Annotation> annotationClass) {
        return method.isAnnotationPresent(annotationClass);
    }

    /**
     * 获得参数
     *
     * @since 1.0.4
     */
    public static Parameter[] getParameters(@NotNull Method method) {
        return method.getParameters();
    }


    /**
     * 获得方法所有参数注解
     *
     * @since 1.0.4
     */
    @NotNull
    @Contract(pure = true)
    public static Annotation[][] getParameterAnnotations(@NotNull Method method) {
        return method.getParameterAnnotations();
    }

    /**
     * 处理方法的参数和注解
     *
     * @param params           方法参数
     * @param annotationClass  注解类型
     * @param parameterHandler 参数处理器
     * @since 1.0.4
     */
    public static <A extends Annotation> void methodParamsHandler(@NotNull Parameter[] params, Class<A> annotationClass, TriConsumer<Parameter[], Integer, A> parameterHandler) {
        for (int i = 0; i < params.length; i++) {
            A paramAnno = top.cutexingluo.tools.designtools.method.ClassUtil.getAnnotation(params[i], annotationClass);
            if (parameterHandler != null) {
                parameterHandler.accept(params, i, paramAnno);
            }
        }
    }

    /**
     * 处理对象的字段的注解
     *
     * @param annotationClass 注解类型
     * @param fieldHandler    字段处理器
     * @since 1.0.4
     */
    public static <T, A extends Annotation> void objFieldHandler(@NotNull T obj, Class<A> annotationClass, TriConsumer<Field[], Integer, A> fieldHandler) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            A fieldAnno = ClassUtil.getAnnotation(fields[i], annotationClass);
            if (fieldHandler != null) {
                fieldHandler.accept(fields, i, fieldAnno);
            }
        }
    }
}
