package top.cutexingluo.tools.designtools.method;

import java.lang.reflect.InvocationTargetException;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 18:41
 */
public interface BaseMethod {
    <T> Object invoke(T item, Object... values) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
    <T> Object invoke(T item) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
