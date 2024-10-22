package top.cutexingluo.tools.utils.log.pkg;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import top.cutexingluo.tools.basepackage.baseimpl.XTRunnable;
import top.cutexingluo.tools.utils.log.LogLevel;


/**
 * log4j 需要导入 log4j 包
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/10 10:49
 * @since 1.0.4
 */
@Log4j
public class LogLog4j implements ILogProvider<Logger> {
    public static final int TAG = 1;
    public static Logger LOG = Logger.getLogger(LogLog4j.class); // pass javadoc

    /**
     * LOG 对象
     * <p>于 1.1.4  变为面向对象</p>
     */
    protected Logger logger;

    public LogLog4j() {
        logger = LOG;
    }

    public LogLog4j(Logger log) {
        this.logger = log;
    }

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
