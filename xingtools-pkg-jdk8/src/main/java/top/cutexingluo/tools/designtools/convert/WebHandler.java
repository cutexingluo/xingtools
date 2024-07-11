package top.cutexingluo.tools.designtools.convert;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.function.BiFunction;

/**
 * Web 处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/13 13:16
 * @since 1.0.4
 */
public interface WebHandler extends BiFunction<Method, HttpServletRequest, String> {
}
