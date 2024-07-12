package top.cutexingluo.tools.aop.log.xtlog.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.aop.log.xtlog.strategy.WebLogStrategy;
import top.cutexingluo.tools.utils.log.pkg.ILogProvider;

/**
 * 类注解信息存储类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/17 16:44
 * @since 1.0.4
 */
@Data
@AllArgsConstructor
public class WebLogSetting {

    @NotNull
    protected WebLogConfig classLogConfig;

    protected WebLogStrategy logStrategy;

    protected ILogProvider<?> logProvider;

    public WebLogSetting(@NotNull WebLogConfig classLogConfig) {
        this.classLogConfig = classLogConfig;
    }
}
