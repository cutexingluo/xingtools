package top.cutexingluo.tools.security.self.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Token 工具类封装
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/30 10:38
 */
public class XTAccessTokenUtil {

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
        List<GrantedAuthority> authorities = new ArrayList<>();
        scopes.stream().filter(Objects::nonNull).forEach(s -> authorities.add(new SimpleGrantedAuthority(s)));
        return authorities;
    }
}
