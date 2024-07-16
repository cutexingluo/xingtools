package top.cutexingluo.tools.security.base;

import org.springframework.security.core.Authentication;

import java.util.function.Consumer;

/**
 * 基本身份验证消费者
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/29 16:35
 */
@FunctionalInterface
public interface BaseAuthenticationConsumer extends Consumer<Authentication> {
}
