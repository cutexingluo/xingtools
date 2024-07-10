package top.cutexingluo.tools.designtools.method.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * 方法对象类型信息
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/24 16:37
 * @since 1.0.5
 */
@Data
@AllArgsConstructor
@Builder
public class MethodClassInfo {


    /**
     * 方法信息
     */
    protected MethodInfo methodInfo;

    /**
     * 对象类型
     */
    protected Class<?> clazz;

    public MethodClassInfo(MethodInfo methodInfo) {
        this.methodInfo = methodInfo;
    }

    public MethodClassInfo(@NotNull Method method, Class<?> clazz) {
        this.methodInfo = new MethodInfo(method);
        this.clazz = clazz;
    }

    public MethodClassInfo(@NotNull Method method) {
        this(method, method.getDeclaringClass());
    }
}
