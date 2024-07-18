package top.cutexingluo.tools.designtools.method.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * 方法信息数据 含实参
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/24 16:55
 * @since 1.0.5
 */
@Data
@AllArgsConstructor
@Builder
public class MethodInfoData {

    /**
     * 方法信息
     */
    protected MethodInfo methodInfo;

    /**
     * 实参
     */
    protected Object[] args;

    public MethodInfoData(MethodInfo methodInfo) {
        this.methodInfo = methodInfo;
    }

    public MethodInfoData(@NotNull Method method, Object[] args) {
        this.methodInfo = new MethodInfo(method);
        this.args = args;
    }
}
