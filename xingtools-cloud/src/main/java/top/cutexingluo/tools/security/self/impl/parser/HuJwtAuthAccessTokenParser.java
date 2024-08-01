package top.cutexingluo.tools.security.self.impl.parser;


import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import lombok.Data;
import top.cutexingluo.tools.security.self.base.AuthAccessTokenParser;
import top.cutexingluo.tools.security.self.impl.token.HuJwtAuthAccessToken;

import java.util.Map;

/**
 * hutool jwt  parser 解析器
 *
 * <p>需要配合对应生成器使用</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/25 17:50
 * @since 1.1.2
 */
@Data
public class HuJwtAuthAccessTokenParser implements AuthAccessTokenParser {

    /**
     * 密钥
     */
    protected byte[] secretKey;

    public HuJwtAuthAccessTokenParser(byte[] secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public HuJwtAuthAccessToken parse(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(secretKey);

        Map<String, ?> payloads = jwt.getPayloads();
        HuJwtAuthAccessToken authAccessToken = HuJwtAuthAccessToken.of((Map<String, Object>) payloads);

        authAccessToken.setToken(token);
        return authAccessToken;
    }
}
