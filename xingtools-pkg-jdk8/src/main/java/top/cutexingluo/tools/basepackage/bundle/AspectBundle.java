package top.cutexingluo.tools.basepackage.bundle;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.basepackage.bundle.base.IAspectBundle;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 关于 web 切面 或者基于方法 等一些列的捆绑集合结构体, 适用于很多 handler 操作
 * <p>web方法切面配置</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/15 15:41
 * @since 1.0.4
 */
@Data
@AllArgsConstructor
public class AspectBundle implements IAspectBundle {
    protected Method method;
    protected HttpServletRequest request;
    @Nullable
    protected ProceedingJoinPoint joinPoint;
    @Nullable
    protected Object result;
    @Nullable
    protected Class<?> resultClass;

    public AspectBundle(Method method, HttpServletRequest request) {
        this.method = method;
        this.request = request;
    }

    public AspectBundle(Method method, HttpServletRequest request, @Nullable ProceedingJoinPoint joinPoint) {
        this.method = method;
        this.request = request;
        this.joinPoint = joinPoint;
    }

    public AspectBundle(Method method, HttpServletRequest request, @Nullable ProceedingJoinPoint joinPoint, @Nullable Object result) {
        this.method = method;
        this.request = request;
        this.joinPoint = joinPoint;
        this.result = result;
    }

    /**
     * 自动设置 class
     */
    public void setCheckResult(@Nullable Object result) {
        this.result = result;
        if (result != null) {
            this.resultClass = result.getClass();
        }
    }

    /**
     * 手动设置class
     */
    public void setCheckResult(@Nullable Object result, @Nullable Class<?> clazz) {
        this.result = result;
        if (result != null) {
            this.resultClass = clazz != null ? clazz : result.getClass();
        }
    }
}
