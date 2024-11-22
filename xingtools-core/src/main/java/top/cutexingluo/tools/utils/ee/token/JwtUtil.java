package top.cutexingluo.tools.utils.ee.token;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;


/**
 * JWT工具类
 *
 * <p>需要导入 io.jsonwebtoken:jjwt 包</p>
 * <p>jdk 17 还需导入 javax.xml.bind:jaxb-api 包</p>
 */
public class JwtUtil {


    /**
     * 签发者
     */
    public static String ISSUER;

    /**
     * 有效期为 (毫秒)
     */
    public static Long JWT_TTL = 24 * 60 * 60 * 1000L;// 60 * 60 *1000  一个小时

    /**
     * 秘钥对象
     */
    public static SecretKey SECRET_KEY;

    /**
     * 加签算法 (默认 HS256)
     */
    public static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    /**
     * 无分隔符 "-" 的UUID
     */
    public static String getUUID() {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }

    /**
     * 生成jwt
     *
     * @param subject token中要存放的数据（json格式）
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成jtw
     *
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SIGNATURE_ALGORITHM;
        SecretKey secretKey = SECRET_KEY;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuedAt(now)      // 签发时间
                .setExpiration(expDate);
        if (ISSUER != null) {
            jwtBuilder.setIssuer(ISSUER);     // 签发者
        }
        if (secretKey != null) {
            jwtBuilder.signWith(signatureAlgorithm, secretKey); //使用算法签名, 第二个参数为秘钥
        }
        return jwtBuilder;
    }

    /**
     * 创建token
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    public static void main(String[] args) throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjYWM2ZDVhZi1mNjVlLTQ0MDAtYjcxMi0zYWEwOGIyOTIwYjQiLCJzdWIiOiJzZyIsImlzcyI6InNnIiwiaWF0IjoxNjM4MTA2NzEyLCJleHAiOjE2MzgxMTAzMTJ9.JVsSbkP94wuczb4QryQbAke3ysBDIL5ou8fWsbt_ebg";
        Claims claims = parseJWT(token);
        System.out.println(claims);
    }

    /**
     * 生成秘钥 secretKey
     */
    @NotNull
    public static SecretKey generalKey(String base64JwtKey, String algo) {
        byte[] encodedKey = Base64.getDecoder().decode(base64JwtKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, algo);
        return key;
    }


    /**
     * 解析
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = SECRET_KEY;
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }


}