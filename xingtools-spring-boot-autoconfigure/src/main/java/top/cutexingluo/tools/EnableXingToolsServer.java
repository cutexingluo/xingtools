package top.cutexingluo.tools;

import org.springframework.context.annotation.Import;
import top.cutexingluo.tools.auto.server.XingToolsImportConfig;

import java.lang.annotation.*;

/**
 * 一般服务启动注解
 * 在任意类上开启此注解
 * 从而开启自动注入其他非工具的服务
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 20:03
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({XingToolsImportConfig.class})
public @interface EnableXingToolsServer {

}
