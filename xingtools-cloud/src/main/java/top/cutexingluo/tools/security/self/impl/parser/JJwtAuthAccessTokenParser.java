package top.cutexingluo.tools.security.self.impl.parser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import top.cutexingluo.tools.security.self.base.AuthAccessTokenParser;
import top.cutexingluo.tools.security.self.impl.token.JJwtAuthAccessToken;

import javax.crypto.SecretKey;

/**
 * jjwt token parser 解析器
 *
 * <p>需要配合对应生成器使用</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/26 11:48
 * @since 1.1.2
 */
@Data
public class JJwtAuthAccessTokenParser implements AuthAccessTokenParser {

    /**
     * 密钥
     */
    protected SecretKey secretKey;

    public JJwtAuthAccessTokenParser(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public JJwtAuthAccessToken parse(String token) {
        Claims claims = parseJWT(secretKey, token);
        JJwtAuthAccessToken authAccessToken = new JJwtAuthAccessToken(claims);


        authAccessToken.setToken(token);
        return authAccessToken;
    }

    /**
     * 解析
     */
    public static Claims parseJWT(SecretKey secretKey, String jwt) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
