package top.cutexingluo.tools.security.self.base.function;

import top.cutexingluo.tools.security.self.base.AuthAccessToken;

import java.util.function.Consumer;

/**
 * 基本访问令牌消费者
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/29 16:31
 * @since 1.1.2
 */
@FunctionalInterface
public interface BaseAccessTokenConsumer extends Consumer<AuthAccessToken> {
}
