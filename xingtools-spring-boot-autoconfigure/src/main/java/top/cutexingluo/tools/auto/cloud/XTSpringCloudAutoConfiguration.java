package top.cutexingluo.tools.auto.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;

import javax.annotation.PostConstruct;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/3 22:50
 */
@AutoConfigureAfter(XingToolsAutoConfiguration.class)
@Import({XTCloudImportSelector.class})
@Slf4j
@Configuration
public class XTSpringCloudAutoConfiguration {
    public static final String INFO = "@author XingTian\n" +
            "  @version 1.0.0\n" +
            " @since 2023/5/3";

    public static final String CLOUD_BANNER =
            "      _             _              _        _                 _ \n" +
                    " __  _(_)_ __   __ _| |_ ___   ___ | |   ___| | ___  _   _  __| |\n" +
                    " \\ \\/ / | '_ \\ / _` | __/ _ \\ / _ \\| |  / __| |/ _ \\| | | |/ _` |\n" +
                    "  >  <| | | | | (_| | || (_) | (_) | | | (__| | (_) | |_| | (_| |\n" +
                    " /_/\\_\\_|_| |_|\\__, |\\__\\___/ \\___/|_|  \\___|_|\\___/ \\__,_|\\__,_|\n" +
                    "               |___/                                             ";

    @PostConstruct
    public void init() {
        log.info(" \n {}", CLOUD_BANNER);
    }
}
