package top.cutexingluo.tools.utils.log;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 日志类型 / 级别
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/8 14:02
 * @since 1.0.4
 */
public class LogLevel {

    /**
     * 分隔符
     * <p>可以更改</p>
     */
    public static char splitChar = '|';

    /**
     * System 类型, 输出到终端, 即 System.out.println
     */
    public static final int NONE = 0b000000;

    /**
     * System 类型, 输出到终端, 即 System.out.println
     */
    public static final int SYSTEM = 0b000001;
    /**
     * log.debug
     */
    public static final int DEBUG = 0b000010;
    /**
     * log.info
     */
    public static final int INFO = 0b000100;
    /**
     * log.warn
     */
    public static final int WARN = 0b001000;
    /**
     * log.error
     */
    public static final int ERROR = 0b010000;
    /**
     * log.trace
     */
    public static final int TRACE = 0b100000;


    /**
     * System 类型, 输出到终端, 即 System.out.println
     */
    public static final String STR_SYSTEM = "SYSTEM";
    /**
     * log.debug
     */
    public static final String STR_DEBUG = "DEBUG";
    /**
     * log.info
     */
    public static final String STR_INFO = "INFO";
    /**
     * log.warn
     */
    public static final String STR_WARN = "WARN";
    /**
     * log.error
     */
    public static final String STR_ERROR = "ERROR";
    /**
     * log.trace
     */
    public static final String STR_TRACE = "TRACE";


    /**
     * 获取日志级别字符串
     */
    @Nullable
    @Contract(pure = true)
    public static String getLevelStr(int levelCode) {
        switch (levelCode) {
            case SYSTEM:
                return STR_SYSTEM;
            case DEBUG:
                return STR_DEBUG;
            case INFO:
                return STR_INFO;
            case WARN:
                return STR_WARN;
            case ERROR:
                return STR_ERROR;
            case TRACE:
                return STR_TRACE;
            default:
                return null;
        }
    }

    /**
     * 获取日志级别字符串
     * <p>通过位获取</p>
     */
    @NotNull
    @Contract(pure = true)
    public static String getLevelStrByBit(int levelCode) {
        StringBuilder sb = new StringBuilder();
        if ((levelCode & SYSTEM) != 0) {
            sb.append(STR_SYSTEM);
        } else if ((levelCode & DEBUG) != 0) {
            if (sb.length() != 0) sb.append(splitChar);
            sb.append(STR_DEBUG);
        } else if ((levelCode & INFO) != 0) {
            if (sb.length() != 0) sb.append(splitChar);
            sb.append(STR_INFO);
        } else if ((levelCode & WARN) != 0) {
            if (sb.length() != 0) sb.append(splitChar);
            sb.append(STR_WARN);
        } else if ((levelCode & ERROR) != 0) {
            if (sb.length() != 0) sb.append(splitChar);
            sb.append(STR_ERROR);
        } else if ((levelCode & TRACE) != 0) {
            if (sb.length() != 0) sb.append(splitChar);
            sb.append(STR_TRACE);
        }
        return sb.toString();
    }

    /**
     * 获取日志级别码
     */
    public static int getLevelCode(@NotNull String levelStr) {
        if (STR_SYSTEM.equalsIgnoreCase(levelStr)) {
            return SYSTEM;
        } else if (STR_DEBUG.equalsIgnoreCase(levelStr)) {
            return DEBUG;
        } else if (STR_INFO.equalsIgnoreCase(levelStr)) {
            return INFO;
        } else if (STR_WARN.equalsIgnoreCase(levelStr)) {
            return WARN;
        } else if (STR_ERROR.equalsIgnoreCase(levelStr)) {
            return ERROR;
        } else if (STR_TRACE.equalsIgnoreCase(levelStr)) {
            return TRACE;
        } else {
            return 0;
        }
    }

    /**
     * 获取日志级别码
     * <p>通过位获取</p>
     */
    public static int getLevelCodeByBit(@NotNull String levelStr) {
        String[] split = levelStr.split("\\" + splitChar);
        int code = 0;
        for (String s : split) {
            code = code | getLevelCode(s.trim());
        }
        return code;
    }

    /**
     * 获取日志级别码
     * <p>通过位获取</p>
     */
    @NotNull
    public static int[] getLevelCodesByBit(@NotNull String levelStr) {
        String[] split = levelStr.split("\\" + splitChar);
        int[] code = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            code[i] = getLevelCode(split[i].trim());
        }
        return code;
    }

    /**
     * 获取日志级别字符串
     */
    @Nullable
    @Contract(pure = true)
    public static LogType getLogType(int levelCode) {
        switch (levelCode) {
            case SYSTEM:
                return LogType.System;
            case DEBUG:
                return LogType.Debug;
            case INFO:
                return LogType.Info;
            case WARN:
                return LogType.Warn;
            case ERROR:
                return LogType.Error;
            case TRACE:
                return LogType.Trace;
            default:
                return null;
        }
    }

    /**
     * 获取日志级别码
     */
    public static LogType getLogType(@NotNull String levelStr) {
        switch (levelStr) {
            case STR_SYSTEM:
                return LogType.System;
            case STR_DEBUG:
                return LogType.Debug;
            case STR_INFO:
                return LogType.Info;
            case STR_WARN:
                return LogType.Warn;
            case STR_ERROR:
                return LogType.Error;
            case STR_TRACE:
                return LogType.Trace;
            default:
                return null;
        }
    }
}
