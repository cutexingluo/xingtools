package top.cutexingluo.tools.security.base;

import org.springframework.security.core.Authentication;

import java.util.function.Function;

/**
 * 基本前身份验证过滤器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/29 16:36
 */
@FunctionalInterface
public interface BasePreAuthenticatedFilter extends Function<Authentication, Authentication> {
}
