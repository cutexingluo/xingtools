package top.cutexingluo.tools.auto.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.cutexingluo.tools.auto.server.XingToolsAutoConfiguration;

/**
 * 自动配置标识
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/3 22:50
 */
@AutoConfigureAfter(XingToolsAutoConfiguration.class)
@Import({XTCloudImportSelector.class})
@Slf4j
@Configuration
public class XTSpringCloudAutoConfiguration {
    public static final String ORIGIN_INFO = "@author XingTian\n" +
            "  @version 1.0.0\n" +
            " @since 2023/5/3";

    public static boolean printBanner = true;
    public static boolean printBannerLogInfo = true;

    public static final String CLOUD_BANNER =
            "      _             _              _        _                 _ \n" +
                    " __  _(_)_ __   __ _| |_ ___   ___ | |   ___| | ___  _   _  __| |\n" +
                    " \\ \\/ / | '_ \\ / _` | __/ _ \\ / _ \\| |  / __| |/ _ \\| | | |/ _` |\n" +
                    "  >  <| | | | | (_| | || (_) | (_) | | | (__| | (_) | |_| | (_| |\n" +
                    " /_/\\_\\_|_| |_|\\__, |\\__\\___/ \\___/|_|  \\___|_|\\___/ \\__,_|\\__,_|\n" +
                    "               |___/                                             ";


    //    @PostConstruct //  移除支持jdk 17
    public void init() {
        if (printBanner) {
            if (printBannerLogInfo) log.info(" \n {}", CLOUD_BANNER);
            else System.out.println(CLOUD_BANNER);
        }
    }

    public XTSpringCloudAutoConfiguration() {
        init();
    }
}
