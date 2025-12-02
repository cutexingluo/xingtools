package top.cutexingluo.tools.utils.log;

import top.cutexingluo.core.common.base.IntCode;

/**
 * 日志类型
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/6 18:59
 */
public enum LogType implements IntCode {
    /**
     * System 类型, 输出到终端, 即 System.out.println
     */
    System(LogLevel.STR_SYSTEM, LogLevel.SYSTEM),
    /**
     * log.debug
     */
    Debug(LogLevel.STR_DEBUG, LogLevel.DEBUG),
    /**
     * log.info
     */
    Info(LogLevel.STR_INFO, LogLevel.INFO),
    /**
     * log.warn
     */
    Warn(LogLevel.STR_WARN, LogLevel.WARN),
    /**
     * log.error
     */
    Error(LogLevel.STR_ERROR, LogLevel.ERROR),
    /**
     * log trace
     */
    Trace(LogLevel.STR_TRACE, LogLevel.TRACE);


    private final String name;
    private final int code;

    LogType(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    @Override
    public int intCode() {
        return code;
    }
}
