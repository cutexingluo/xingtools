package top.cutexingluo.tools.autoconfigure.server.security.controller;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.security.Principal;

/**
 * Controller 判定器，
 * 需要注入的Controller Bean的依赖
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/15 17:06
 */
@ConditionalOnClass({Principal.class})
public class SecurityControllerConfig {
}
