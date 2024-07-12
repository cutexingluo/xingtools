package top.cutexingluo.tools.utils.log.pkg;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import top.cutexingluo.tools.basepackage.baseimpl.XTRunnable;
import top.cutexingluo.tools.utils.log.LogLevel;

/**
 * log4j2
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/10 10:46
 * @since 1.0.4
 */
@Log4j2
public class LogLog4j2 implements ILogProvider<Logger> {
    public static final int TAG = 2;
    public static final Logger LOG = org.apache.logging.log4j.LogManager.getLogger(LogLog4j2.class); // pass javadoc

    @Override
    public Logger getLog() {
        return LOG;
    }

    @Override
    public Class<?> getLogClass() {
        return Logger.class;
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
