package top.cutexingluo.tools.utils.js.hook;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * <p>
 * 仿 js 的Call类，反射调用方法
 * </p>
 * <ul>
 *     <li>
 *         <code>new XTCall(PrintClass.class, "print", String.class).call(target,"hello world");</code>
 *     </li>
 * </ul>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/10 22:29
 */
@Data
//@NoArgsConstructor
@AllArgsConstructor
public class XTCall {
    private Method method;

    public XTCall(Class<?> clazz, String methodName, Class<?>... args) throws NoSuchMethodException {
        this.method = clazz.getMethod(methodName, args);
    }

    // call
    public static <T, R> R call(Method method, T target, Object... args) {
        if (method == null || target == null) return null;
        try {
            return (R) method.invoke(target, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T, R> R call(String methodName, T target, Object... args) throws NoSuchMethodException {
        return XTCall.call(target.getClass().getMethod(methodName, args.getClass()), target, args);
    }

    public <T, R> R call(T target, Object... args) {
        return XTCall.call(method, target, args);
    }
}
