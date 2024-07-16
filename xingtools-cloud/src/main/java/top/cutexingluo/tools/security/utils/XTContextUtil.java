package top.cutexingluo.tools.security.utils;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 上下文工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/19 15:59
 */
public class XTContextUtil {


    /**
     * 从SecurityContext 中 设置身份验证
     *
     * @param authentication 身份验证
     */
    public static void setAuthentication(Authentication authentication) {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
    }

    /**
     * 从SecurityContext 中得到认证
     *
     * @return {@link Authentication}
     */
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }

    /**
     * 从SecurityContext 中得到对象
     *
     * @param clazz 转化的类型
     * @return {@link T}  类型
     * @updateFrom 1.0.4
     */
    @Nullable
    public static <T> T getPrincipal(@NonNull Class<T> clazz) {
        Authentication authentication = getAuthentication();
        if (authentication != null) { // 1.0.4 增加非空判断
            Object principal = authentication.getPrincipal();
            if (clazz == principal.getClass()) {
                return (T) principal;
            }
        }
        return null;
    }


}
