package top.cutexingluo.tools.security.base;

import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.function.Function;

/**
 * 基本访问令牌附加转换器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/29 16:32
 */
@FunctionalInterface
public interface BaseAccessTokenAdditionalConverter extends Function<Map<String, ?>, Authentication> {
}
