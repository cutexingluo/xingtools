package top.cutexingluo.tools.security.self.base;


import top.cutexingluo.core.basepackage.function.Parser;

/**
 * token  parser
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/25 14:10
 * @since 1.1.2
 */
public interface AuthAccessTokenParser extends Parser<String, AuthAccessToken> {

    /**
     * parse token
     */
    @Override
    AuthAccessToken parse(String token) throws Exception;
}
