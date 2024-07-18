package top.cutexingluo.tools.security.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import top.cutexingluo.tools.utils.ee.token.XTTokenUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Token 工具类封装 <br>
 * 推荐使用的工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/18 14:38
 */
public class XTAccessTokenUtil extends XTTokenUtil {


    /**
     * 验证令牌格式 由 jwt 法校验
     * <p> 里面包含非空验证, 格式验证, sign验证</p>
     *
     * @param token   令牌
     * @param signKey signKey
     * @return boolean 是否校验成功 (sign 验证)
     * @throws InvalidTokenException 无效token异常
     */
    public static boolean verifyTokenByJwt(String token, byte[] signKey) throws InvalidTokenException {
        boolean result = false;
        try {
            // 验证 jwt token
            if (StrUtil.isBlank(token)) {
                throw new InvalidTokenException("token is null or empty !  --> token 为空 !");
            }
            // 验证jwt是否正确 判断 token 格式是否正确
            result = JWTUtil.verify(token, signKey);
        } catch (JWTException | JSONException e) {
            throw new InvalidTokenException("Invalid token --> token 格式错误");
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token --> token 其他未知错误");
        }
        return result;
    }

    public static void verifyTokenByJwtAllException(String token, byte[] signKey) throws InvalidTokenException {
        if (!verifyTokenByJwt(token, signKey)) {
            throw new InvalidTokenException("Invalid token --> token 无效或者格式错误");
        }
    }

    /**
     * 验证 jwt 是否过期
     *
     * @param token 令牌
     * @return boolean 是否过期
     */
    public static boolean isExpiredByJwt(String token) {
        // token 是否过期
        // 解析token
        DefaultOAuth2AccessToken accessToken = XTAccessTokenUtil.getAccessToken(token);
        return isExpired(accessToken);
    }


    /**
     * 验证 jwt 是否过期
     *
     * @param accessToken 访问令牌
     * @return boolean 是否过期
     */
    public static boolean isExpired(DefaultOAuth2AccessToken accessToken) {
        // token 是否过期
        // 自定义验证逻辑，例如验证token是否过期等
        Date expiration = accessToken.getExpiration();
        //            throw new AuthenticationServiceException("Token has expired, token已过期");
        return expiration != null && new Date().after(expiration);
    }


    /**
     * 获取访问令牌封装类
     *
     * @param token      令牌
     * @param tokenStore 令牌存储
     * @return {@link DefaultOAuth2AccessToken}
     */
    public static DefaultOAuth2AccessToken getAccessToken(String token, TokenStore tokenStore) {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        return new DefaultOAuth2AccessToken(accessToken);
    }


    public static Map<String, String> convert(Map<String, Object> originalMap) {
        Map<String, String> convertedMap = new HashMap<>();

        for (Map.Entry<String, Object> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value != null) {
                String stringValue = value.toString();
                convertedMap.put(key, stringValue);
            }
        }

        return convertedMap;
    }

    /**
     * 使用JWTUtil获取访问令牌封装类
     *
     * @param token 令牌
     * @return {@link DefaultOAuth2AccessToken}
     */
    public static DefaultOAuth2AccessToken getAccessToken(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(token);
        JSONObject payloads = jwt.getPayloads();
        Map<String, String> stringMap = convert(payloads);
        DefaultOAuth2AccessToken auth2AccessToken =
                (DefaultOAuth2AccessToken) DefaultOAuth2AccessToken.valueOf(stringMap);
        return auth2AccessToken;
//        accessToken.setTokenType(OAuth2AccessToken.TOKEN_TYPE);
//
//
//
//        // 使用 Hutool 解析 JWT 令牌
////        JWT jwt = JWTUtil.parseToken(jwtToken);
//
//// 提取 JWT 中的相关信息
////        String subject = jwt.getPayload("sub", String.class); // 获取用户ID或用户名
//        List<String> scopes = (List<String>) jwt.getPayload("scope"); // 获取授权范围
//        Long expirationTime = (Long) jwt.getPayload("exp"); // 获取过期时间
//
//// 创建 DefaultOAuth2AccessToken 对象
//        DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(token);
//
//// 设置相关信息到 DefaultOAuth2AccessToken 对象中
//        accessToken.setExpiration(new Date(expirationTime * 1000)); // 设置过期时间
////        accessToken.setScope(scopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())); // 设置授权范围
//        accessToken.setScope(new HashSet<>(scopes)); // 设置授权范围
////        OAuth2Authentication authentication = new OAuth2Authentication(...); // 假设您有相应的 OAuth2Authentication 对象
////        authentication.setPrincipal(subject); // 设置用户ID或用户名
////        accessToken.setAuthentication(authentication);
//
////        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);

    }


    /**
     * 得到权限列表
     *
     * @param additionalInformation Token 额外信息 Map
     * @param authorityString       权限字符串
     * @return {@link List}<{@link GrantedAuthority}> 权限列表
     */
    public static List<GrantedAuthority> getGrantedAuthorities(Map<String, ?> additionalInformation, String authorityString) {
        List<GrantedAuthority> authorities = null;
        Object tar = additionalInformation.get(authorityString);
        if (tar instanceof String) {
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(tar.toString());
        } else if (tar instanceof Collection) {
            Collection<String> scopes = (Collection<String>) tar;
            authorities = scopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return authorities;
    }

    /**
     * 得到权限列表
     *
     * @param scopes 作用域
     * @return {@link List}<{@link GrantedAuthority}>
     */
    public static List<GrantedAuthority> getGrantedAuthorities(Set<String> scopes) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        scopes.stream().filter(Objects::nonNull).forEach(s -> authorities.add(new SimpleGrantedAuthority(s)));
        return authorities;
    }


}
