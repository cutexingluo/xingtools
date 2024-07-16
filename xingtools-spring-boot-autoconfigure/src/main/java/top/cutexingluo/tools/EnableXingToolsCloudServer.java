package top.cutexingluo.tools;

import org.springframework.context.annotation.Import;
import top.cutexingluo.tools.auto.cloud.XTSpringCloudAutoConfiguration;

import java.lang.annotation.*;

/**
 * SpringCloud 自动配置启动注解
 * 在任意类上开启此注解
 * 从而开启自动注入其他非工具的服务
 * 启动微服务的一些配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/3 22:45
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({XTSpringCloudAutoConfiguration.class})
public @interface EnableXingToolsCloudServer {
}
