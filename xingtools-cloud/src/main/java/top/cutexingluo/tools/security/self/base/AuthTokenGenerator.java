package top.cutexingluo.tools.security.self.base;

import top.cutexingluo.tools.basepackage.base.ExtInitializable;
import top.cutexingluo.tools.basepackage.function.UniGenerator;

/**
 * token generator
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/26 10:18
 * @since 1.1.2
 */
public interface AuthTokenGenerator extends UniGenerator<AuthAccessToken, String>, ExtInitializable<AuthTokenGenerator> {

    @Override
    default AuthTokenGenerator initSelf() {
        return this;
    }

    /**
     * generate token
     */
    @Override
    String generate(AuthAccessToken authAccessToken);
}
