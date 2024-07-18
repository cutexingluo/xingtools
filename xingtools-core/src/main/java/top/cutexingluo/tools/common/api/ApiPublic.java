package top.cutexingluo.tools.common.api;


import java.lang.annotation.*;


/**
 * Api 开放接口
 * <p>声明接口为公开接口</p>
 * <p>可用于 apifox 配置</p>
 * <p>可以用Spring Security 的@PermitAll 代替</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/20 18:21
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ApiPublic {
}
