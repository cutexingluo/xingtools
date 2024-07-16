package top.cutexingluo.tools.autoconfigure.server.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.cutexingluo.tools.utils.spring.SpringUtils;

/**
 * SpringUtil注入
 * <p> 默认开启 </p>
 * <p>v1.1.1 移除 RYSpringUtils</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 13:59
 */
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "springutils", havingValue = "true", matchIfMissing = true)
@Component
public class SpringUtilsAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }

}
