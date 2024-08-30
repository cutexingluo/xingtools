package top.cutexingluo.tools.auto.server;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 自动配置标识
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 20:02
 */


@Import({XingToolsImportSelector.class})
@Configuration
@Slf4j
public class XingToolsAutoConfiguration {
    public static final String ORIGIN_INFO = "@author XingTian\n" +
            "  @version 1.0.0\n" +
            " @since 2022/10/1";

    public static boolean printBanner = true;
    public static boolean printBannerLogInfo = true;


    public static final String BOOT_BANNER =
            "      _             _              _ \n" +
                    " __  _(_)_ __   __ _| |_ ___   ___ | |\n" +
                    " \\ \\/ / | '_ \\ / _` | __/ _ \\ / _ \\| |\n" +
                    "  >  <| | | | | (_| | || (_) | (_) | |\n" +
                    " /_/\\_\\_|_| |_|\\__, |\\__\\___/ \\___/|_|\n" +
                    "               |___/                  ";

    //    @PostConstruct //  移除支持jdk 17
    public void init() {
        if (printBanner) {
            if (printBannerLogInfo) log.info(" \n {}", BOOT_BANNER);
            else System.out.println(BOOT_BANNER);
        }
    }

    public XingToolsAutoConfiguration() {
        init();
    }
}
