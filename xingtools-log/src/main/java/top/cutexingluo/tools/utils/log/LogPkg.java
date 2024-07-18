package top.cutexingluo.tools.utils.log;

import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.common.base.XTIntCode;
import top.cutexingluo.tools.utils.log.handler.ILogHandler;
import top.cutexingluo.tools.utils.log.pkg.*;

import java.util.function.Supplier;

/**
 * 日志框架包 类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/10 11:17
 */
public class LogPkg implements XTIntCode {

    /**
     * @see LogLog4j
     */
    public static final int LOG4J = 1;
    /**
     * @see LogLog4j2
     */
    public static final int LOG4J2 = 2;
    /**
     * @see LogSlf4j
     */
    public static final int SLF4J = 4;

    /**
     * @see XTLog.HuLog
     */
    public static final int HU_LOG = 8;

    public static final LogPkg LOG4J_PKG = new LogPkg(LOG4J, "LOG4J");
    public static final LogPkg LOG4J2_PKG = new LogPkg(LOG4J2, "LOG4J2");
    public static final LogPkg SLF4J_PKG = new LogPkg(SLF4J, "SLF4J");
    public static final LogPkg HU_LOG_PKG = new LogPkg(HU_LOG, "HU_LOG");
    protected int code;
    protected String name;

    public LogPkg(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public LogPkg(int code) {
        this.code = code;
        switch (code) {
            case 1:
                name = LOG4J_PKG.getName();
                break;
            case 2:
                name = LOG4J2_PKG.getName();
                break;
            case 4:
                name = SLF4J_PKG.getName();
                break;
            case 8:
                name = HU_LOG_PKG.getName();
                break;
            default:
                name = "CUSTOM_LOG";
                break;
        }
    }


    public LogPkg(String logPkg) {
        this(logPkg, 0);
    }


    public String getName() {
        return name;
    }

    @Override
    public int intCode() {
        return code;
    }

    public LogPkg(String logPkg, int notExistsFillCode) {
        if (LOG4J_PKG.getName().equalsIgnoreCase(logPkg)) {
            code = 1;
        } else if (LOG4J2_PKG.getName().equalsIgnoreCase(logPkg)) {
            code = 2;
        } else if (SLF4J_PKG.getName().equalsIgnoreCase(logPkg)) {
            code = 4;
        } else if (HU_LOG_PKG.getName().equalsIgnoreCase(logPkg)) {
            code = 8;
        } else {
            code = notExistsFillCode;
        }
    }

    @Nullable
    protected <T> T getLogData(Supplier<T> notExistsFillLog) {
        switch (code) {
            case 1:
                return (T) new LogLog4j();
            case 2:
                return (T) new LogLog4j2();
            case 4:
                return (T) new LogSlf4j();
            case 8:
                return (T) new XTLog.HuLog();
            default:
                if (notExistsFillLog != null) {
                    return notExistsFillLog.get();
                }
                return null;
        }
    }


    public <T extends ILog<?>> T getLog() {
        return getLog(null);
    }

    /**
     * @param notExistsFillLog 不存在默认对象，执行的回调，获取 log
     */
    public <T extends ILog<?>> T getLog(Supplier<T> notExistsFillLog) {
        return getLogData(notExistsFillLog);
    }

    public <T extends ILogHandler> T getLogHandler() {
        return getLogHandler(null);
    }

    /**
     * @param notExistsFillLog 不存在默认对象，执行的回调，获取 logHandler
     */
    public <T extends ILogHandler> T getLogHandler(Supplier<T> notExistsFillLog) {
        return getLogData(notExistsFillLog);
    }

    public <T extends ILogProvider<?>> T getLogProvider() {
        return getLogHandler(null);
    }

    /**
     * @param notExistsFillLog 不存在默认对象，执行的回调，获取 logProvider
     */
    public <T extends ILogProvider<?>> T getLogProvider(Supplier<T> notExistsFillLog) {
        return getLogData(notExistsFillLog);
    }
}
