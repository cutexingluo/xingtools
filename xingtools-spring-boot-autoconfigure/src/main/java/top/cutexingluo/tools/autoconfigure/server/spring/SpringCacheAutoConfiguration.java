package top.cutexingluo.tools.autoconfigure.server.spring;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;

/**
 * 开启springcache
 * <p>不如自行@EnableCaching</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/4 17:40
 */
@ConditionalOnBean(XingToolsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xingtools.enabled", name = "springcache", havingValue = "true", matchIfMissing = false)
@Configuration(proxyBeanMethods = false)
@EnableCaching
public class SpringCacheAutoConfiguration {

}
