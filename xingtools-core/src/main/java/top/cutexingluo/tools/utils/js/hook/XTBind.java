package top.cutexingluo.tools.utils.js.hook;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>
 * 仿 js 的Bind类，利用方法名反射方法。<br>
 * 先获取方法名，再绑定对象
 * </p>
 * <ul>
 *     <li>
 *         <code>new XTBind("print", String.class).bind(target,"hello world“)    //绑定时调用</code>
 *     </li>
 * </ul>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/10 23:10
 */
@Data
//@NoArgsConstructor
@AllArgsConstructor
public class XTBind {
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] arguments;

    public XTBind(String methodName, Class<?>... paramTypes) {
        this.methodName = methodName;
        this.paramTypes = paramTypes;
    }

    public XTBind(String methodName, List<Class<?>> paramTypes) {
        this(methodName, XTApply.getClassesFromList(paramTypes, true));
    }

    public XTBind(String methodName, List<Class<?>> paramTypes, @NotNull List<Object> arguments) {
        this(methodName, paramTypes);
        this.arguments = arguments.toArray();
    }

    // apply
    public static <T, R> R apply(Method method, T target, List<Object> args) {
        return XTApply.apply(method, target, args);
    }

    // call
    public static <T, R> R call(Method method, T target, Object... args) {
        return XTCall.call(method, target, args);
    }

    // bind
    public <T, R> R bind(T target, Object... args) throws NoSuchMethodException {
        return XTBind.call(target.getClass().getMethod(methodName, paramTypes), target, args);
    }

    // bind
    public <T, R> R bind(T target, List<Object> args) throws NoSuchMethodException {
        return bind(target, args.toArray());
    }

    public <T, R> R bind(T target) throws NoSuchMethodException {
        return bind(target, arguments);
    }
}
