package top.cutexingluo.tools.utils.ee.web.ip.ipregion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ip拦截注解
 *
 * @className: Ip
 * @author: woniu
 * @date: 2023/4/5
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ip {
}
