package top.cutexingluo.tools.anno;

import org.springframework.context.annotation.Import;
import top.cutexingluo.tools.autoconfigure.server.security.XTSecurityImportSelector;

import java.lang.annotation.*;

/**
 * <p>开启 oauth2</p>
 * <p>开启系列配置</p>
 * 开启 oauth2 的 TokenStore Jwt 配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/12 19:34
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({XTSecurityImportSelector.class})
public @interface EnableXTCloudSecurity {
    /**
     * 默认的jwt加密的sign_key
     */
    String sign_key() default "xingtian";
}
