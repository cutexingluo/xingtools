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
    @Data
    public static class HuLog implements ILogProvider<Log> {

        public static final Log LOG = LogFactory.get(HuLog.class);
        protected Log logger;

        public HuLog() {
            logger = LOG;
        }

        public HuLog(Log log) {
            this.logger = log;
        }

        @Override
        public Log getLog() {
            return logger;
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
                    logger.debug(msg);
                }
                if ((levelCode & LogLevel.INFO) != 0) {
                    logger.info(msg);
                }
                if ((levelCode & LogLevel.WARN) != 0) {
                    logger.warn(msg);
                }
                if ((levelCode & LogLevel.ERROR) != 0) {
                    logger.error(msg);
                }
                if ((levelCode & LogLevel.TRACE) != 0) {
                    logger.trace(msg);
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
                    logger.debug(msg);
                    break;
                case LogLevel.INFO:
                    logger.info(msg);
                    break;
                case LogLevel.WARN:
                    logger.warn(msg);
                    break;
                case LogLevel.ERROR:
                    logger.error(msg);
                    break;
                case LogLevel.TRACE:
                    logger.trace(msg);
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
