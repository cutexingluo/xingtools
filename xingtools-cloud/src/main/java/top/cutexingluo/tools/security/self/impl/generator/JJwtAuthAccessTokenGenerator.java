package top.cutexingluo.tools.security.self.impl.generator;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.utils.se.character.UUIDUtils;
import top.cutexingluo.tools.security.self.base.AuthAccessToken;
import top.cutexingluo.tools.security.self.base.AuthTokenGenerator;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * jjwt jwt 生成器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/26 11:46
 * @since 1.1.2
 */
@Data
public class JJwtAuthAccessTokenGenerator implements AuthTokenGenerator {

    /**
     * jwt Builder
     */
    protected JwtBuilder jwtBuilder;


    public JJwtAuthAccessTokenGenerator(@NotNull JwtBuilder jwtBuilder) {
        this.jwtBuilder = jwtBuilder;
    }

    @Override
    public JJwtAuthAccessTokenGenerator initSelf() {
        return this;
    }

    /**
     * 调用前 AuthAccessToken 需要 refresh 保证 getAdditionalInformation 里面有值
     */
    @Override
    public String generate(@NotNull AuthAccessToken authAccessToken) {
        jwtBuilder.addClaims(authAccessToken.getAdditionalInformation());
        return jwtBuilder.compact();
    }

    /**
     * 生成加密后的秘钥 secretKey
     */
    public static SecretKey generalKey(String jwtKey) {
        byte[] encodedKey = Base64.getDecoder().decode(jwtKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }


    /**
     * 默认生成 JWT  Builder
     *
     * @param uuid      唯一 id
     * @param issuer    签发者
     * @param secretKey 加密秘钥
     */
    public static JwtBuilder getJwtBuilder(String uuid, String issuer, SecretKey secretKey) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//        SecretKey secretKey = generalKey(jwtKey);
//        long nowMillis = System.currentTimeMillis();
//        Date now = new Date(nowMillis);
//        if (ttlMillis == null) {
//            ttlMillis = JwtUtil.JWT_TTL; // 默认间隔
//        }
//        long expMillis = nowMillis + ttlMillis;
//        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
//                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer(issuer)     // 签发者
//                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey); //使用HS256对称加密算法签名, 第二个参数为秘钥
//                .setExpiration(expDate);
    }

    /**
     * 默认生成 JWT  Builder
     *
     * @param issuer    签发者
     * @param secretKey 加密秘钥
     */
    public static JwtBuilder getJwtBuilder(String issuer, SecretKey secretKey) {
        return getJwtBuilder(UUIDUtils.originSimpleUUID(), issuer, secretKey);
    }


}
