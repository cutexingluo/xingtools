package top.cutexingluo.tools.utils.log.strategy.impl;

import cn.hutool.core.util.StrUtil;
import top.cutexingluo.tools.utils.log.LogLevel;
import top.cutexingluo.tools.utils.log.StrategyLogConfig;
import top.cutexingluo.tools.utils.log.pkg.ILogProvider;
import top.cutexingluo.tools.utils.log.pkg.LogSlf4j;
import top.cutexingluo.tools.utils.log.strategy.LogStrategy;

/**
 * 默认日志打印策略
 *
 * <p> 如果需要其他操作，例如保存到数据库，可以继承该类，然后重写 log 方法并调用 super 方法来使用该方法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/11 10:43
 * @since 1.0.4
 */
public class DefaultLogStrategy<T extends StrategyLogConfig> implements LogStrategy<T> {

    /**
     * 默认打印日志方法
     */
    @Override
    public boolean log(T config, ILogProvider<?> log, String msg) {
        int levelCode = LogLevel.DEBUG; // 默认 debug
        String levelStr = null;
        if (config != null) {
            levelCode = config.getLevelCode();
            levelStr = config.getLevelStr();
        }
        // 没级别不打印
        if (levelCode == LogLevel.NONE && StrUtil.isBlank(levelStr)) return false;
        if (log != null) {
            log.send(levelCode, msg);
            if (StrUtil.isNotBlank(levelStr)) log.send(levelStr, msg);
        } else { // 如果不存在则使用默认的slf4j
            LogSlf4j logSlf4j = new LogSlf4j();
            logSlf4j.send(levelCode, msg);
            if (StrUtil.isNotBlank(levelStr)) logSlf4j.send(levelStr, msg);
        }
        return true;
    }
}
