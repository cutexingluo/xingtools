package top.cutexingluo.tools.designtools.method.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * 方法信息数据 含对象
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/24 16:57
 * @since 1.0.5
 */
@Data
@AllArgsConstructor
@Builder
public class MethodObjBundle {

    /**
     * 方法
     */
    protected Method method;

    /**
     * 目标对象
     */
    protected Object target;

}
