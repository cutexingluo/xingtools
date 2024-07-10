package top.cutexingluo.tools.designtools.method.core;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * 方法信息
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/24 16:32
 * @since 1.0.5
 */
@Data
public class MethodInfo {

    /**
     * 方法名
     */
    protected String methodName;

    /**
     * 参数类型
     */
    protected Class<?>[] parameterTypes;

    /**
     * 返回类型
     */
    protected Class<?> returnType;


    /**
     * 异常类型
     */
    protected Class<?>[] exceptionTypes;


    public MethodInfo(String methodName) {
        this(methodName, new Class<?>[]{}, void.class, new Class<?>[]{});
    }

    public MethodInfo(String methodName, Class<?>[] parameterTypes) {
        this(methodName, parameterTypes, void.class, new Class<?>[]{});
    }

    public MethodInfo(String methodName, Class<?>[] parameterTypes, Class<?> returnType) {
        this(methodName, parameterTypes, returnType, new Class<?>[]{});
    }

    public MethodInfo(String methodName, Class<?>[] parameterTypes, Class<?> returnType, Class<?>[] exceptionTypes) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
        this.exceptionTypes = exceptionTypes;
    }

    public MethodInfo(@NotNull Method method) {
        this.methodName = method.getName();
        this.parameterTypes = method.getParameterTypes();
        this.returnType = method.getReturnType();
        this.exceptionTypes = method.getExceptionTypes();
    }

}
