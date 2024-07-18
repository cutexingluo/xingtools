package top.cutexingluo.tools.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 基础服务数据
 * <p>1.用于配合 server.port 等</p>
 * <p>2.配置文件代码提示</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 17:08
 */
@Data
@ConfigurationProperties(prefix = "server")
@Configuration
public class ServerProperty {
    private String ip = "localhost";
    private String targetUrl = "/**";
}
