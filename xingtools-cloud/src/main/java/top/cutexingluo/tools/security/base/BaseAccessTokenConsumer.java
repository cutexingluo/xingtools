package top.cutexingluo.tools.security.base;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import java.util.function.Consumer;

/**
 * 基本访问令牌消费者
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/29 16:31
 */
@FunctionalInterface
public interface BaseAccessTokenConsumer extends Consumer<DefaultOAuth2AccessToken> {
}
