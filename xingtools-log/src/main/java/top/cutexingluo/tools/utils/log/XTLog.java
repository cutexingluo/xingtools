package top.cutexingluo.tools.utils.log;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.baseimpl.XTRunnable;
import top.cutexingluo.tools.utils.log.pkg.ILog;
import top.cutexingluo.tools.utils.log.pkg.ILogProvider;
import top.cutexingluo.tools.utils.log.strategy.LogStrategy;

/**
 * XTLog 日志工具类
 * <p></p>
 *
 * <p>于 1.0.4 版本翻新</p>
 *
 * <p>不能在 logConfig 中 的 strategy 里面调用该类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 21:43
 * @update 1.0.4 翻新
 */
@Data
public class XTLog<T, C extends StrategyLogConfig> implements ILog<ILogProvider<T>> {

    /**
     * 推荐使用 {@link Slf4j}注解
     */
    public static class HuLog implements ILogProvider<Log> {

        public static Log LOG = LogFactory.get();

        @Override
        public Log getLog() {
            return LOG;
        }

        @Override
        public Class<Log> getLogClass() {
            return Log.class;
        }

        @Override
        public XTRunnable getTask(int levelCode, String msg) {
            return new XTRunnable(() -> {
                if ((levelCode & LogLevel.SYSTEM) != 0) {
                    System.out.println(msg);
                }
                if ((levelCode & LogLevel.DEBUG) != 0) {
                    LOG.debug(msg);
                }
                if ((levelCode & LogLevel.INFO) != 0) {
                    LOG.info(msg);
                }
                if ((levelCode & LogLevel.WARN) != 0) {
                    LOG.warn(msg);
                }
                if ((levelCode & LogLevel.ERROR) != 0) {
                    LOG.error(msg);
                }
                if ((levelCode & LogLevel.TRACE) != 0) {
                    LOG.trace(msg);
                }
            });
        }

        @Override
        public XTRunnable getTask(String levelStr, String msg) {
            return new XTRunnable(() -> {
                int[] codes = LogLevel.getLevelCodesByBit(levelStr);
                for (int code : codes) {
                    sendOne(code, msg);
                }
            });
        }

        @Override
        public void sendOne(int levelCode, String msg) {
            switch (levelCode) {
                case LogLevel.SYSTEM:
                    System.out.println(msg);
                    break;
                case LogLevel.DEBUG:
                    LOG.debug(msg);
                    break;
                case LogLevel.INFO:
                    LOG.info(msg);
                    break;
                case LogLevel.WARN:
                    LOG.warn(msg);
                    break;
                case LogLevel.ERROR:
                    LOG.error(msg);
                    break;
                case LogLevel.TRACE:
                    LOG.trace(msg);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 配置
     */
    protected XTLogConfig<?, C> logConfig;

    /**
     * log / logger 对象
     * <p> 推荐使用 </p>
     */
    protected ILogProvider<T> log;

    @Override
    public Class<?> getLogClass() {
        return log.getLog().getClass();
    }

    public XTLog(@NotNull XTLogConfig<?, C> logConfig) {
        this.logConfig = logConfig;
        this.log = new LogPkg(logConfig.getLogPkg()).getLog();
    }

    public XTLog(XTLogConfig<?, C> logConfig, ILogProvider<T> log) {
        this.logConfig = logConfig;
        this.log = log;
    }


    /**
     * 打印日志
     *
     * <p>调用 config 里面的 logStrategy 的 log 方法</p>
     */
    public void log(String msg) {
        if (!checkStrategy()) {
            throw new NullPointerException("logConfig or strategy is null");
        }
        LogStrategy<C> strategy = logConfig.getLogStrategy();
        // XTConfig -> 转型为 StrategyLogConfig
        strategy.log((C) logConfig, log, msg);
    }

    public boolean checkStrategy() {
        return logConfig != null && logConfig.getLogStrategy() != null;
    }
}
