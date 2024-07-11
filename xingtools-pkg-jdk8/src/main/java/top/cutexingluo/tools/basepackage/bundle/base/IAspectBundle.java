package top.cutexingluo.tools.basepackage.bundle.base;

import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 关于 web 切面 或者基于方法 等一些列的捆绑集合 接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/15 15:25
 * @since 1.0.4
 */
public interface IAspectBundle {

    /**
     * Returns the HTTP request.
     */
    HttpServletRequest getRequest();

    /**
     * Returns the intercepted method or normal method.
     */
    Method getMethod();

    /**
     * Returns the intercepted join point.
     */
    ProceedingJoinPoint getJoinPoint();

    /**
     * Returns the  result.
     */
    Object getResult();
}
