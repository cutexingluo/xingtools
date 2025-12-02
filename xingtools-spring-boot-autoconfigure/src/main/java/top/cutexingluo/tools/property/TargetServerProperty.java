package top.cutexingluo.tools.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 基础服务数据
 * <p>1.方便使用配置文件</p>
 * <p>2.配置文件代码提示和复用</p>
 * <p>3.未来将会使用该属性</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/6 17:39
 * @since 1.1.2
 */
@Data
@ConfigurationProperties(prefix = "target")
@Component
public class TargetServerProperty {

    /**
     * 本机 ip
     */
    private String ip = "127.0.0.1";

    /**
     * 目标服务 ip
     */
    private String serverIp = "127.0.0.1";

    /**
     * 目标服务 domain
     */
    private String serverDomain = "localhost";
}
