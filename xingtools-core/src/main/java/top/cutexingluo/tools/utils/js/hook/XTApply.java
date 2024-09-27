package top.cutexingluo.tools.utils.js.hook;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;


/**
 * <p>
 * 仿 js 的Apply类，反射调用方法 <br>
 * <p>XTApply 拥有 XTCall 方法</p>
 * </p>
 * <br>
 * <h3>使用方法</h3>
 * <ul>
 *     <li>
 *         <code>new XTApply(PrintClass.class, "print", String.class).apply(target,"hello world");</code>
 *     </li>
 * </ul>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/10 22:39
 */
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class XTApply {
    private Method method;

    public XTApply(Method method) {
        this.method = method;
    }

    public static boolean printTrace = true;
    public static Consumer<Exception> exceptionHandler = null;

    public XTApply(Class<?> clazz, String methodName, @NotNull Class<?>... args) throws NoSuchMethodException {
        this.method = clazz.getMethod(methodName, args);
    }

    public XTApply(Class<?> clazz, String methodName, @NotNull List<Class<?>> args) throws NoSuchMethodException {
        Class<?>[] clazzArray = XTApply.getClassesFromList(args, true);
        this.method = clazz.getMethod(methodName, clazzArray);
    }

    public static <T> Class<?>[] getClassesFromList(List<T> args, boolean isClass) throws ClassCastException {
//        List<? extends Class<?>> classList = null;
        Class<?>[] classArray = null;
        if (!isClass) classArray = (Class<?>[]) args.stream().map(Object::getClass).toArray();
        return classArray != null ? classArray : (Class<?>[]) args.toArray();
    }

    // apply
    public static <T, R> R apply(Method method, T target, List<Object> args) {
        if (method == null || target == null) return null;
        try {
            return (R) method.invoke(target, args.toArray());
        } catch (Exception e) {
            if (exceptionHandler != null) exceptionHandler.accept(e);
            else if (printTrace) e.printStackTrace();
            return null;
        }
    }

    public static <T, R> R apply(String methodName, T target, List<Object> args) throws NoSuchMethodException {
        Class<?>[] classArray = XTApply.getClassesFromList(args, false);
        return XTApply.apply(target.getClass().getMethod(methodName, classArray), target, args);
    }

    public <T, R> R apply(T target, List<Object> args) {
        return XTApply.apply(method, target, args);
    }
}
