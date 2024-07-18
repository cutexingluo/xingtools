package top.cutexingluo.tools.utils.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.log.base.BaseLogConfig;


/**
 * 基本提供 config 配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/11 11:47
 * @since 1.0.4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrategyLogConfig implements BaseLogConfig {

    /**
     * 标识符，用于strategy, 可自定义操作
     */
    protected int index = 0;


    /**
     * 标识符，用于strategy, 可自定义操作
     */
    protected String key = "";

    /**
     * 日志框架
     * <p>编号</p>
     *
     * @see LogPkg
     */
    protected int logPkg = 0;


    /**
     * 日志级别
     * <p>默认 debug </p>
     * <p>优先级比 {@code levelStr} 高</p>
     * <p>使用方式: {@code LogLevel.DEBUG | LogLevel.INFO} , 按低位到高位顺序依次打印 </p>
     *
     * @see LogLevel
     */
    protected int levelCode = LogLevel.DEBUG;

    /**
     * 日志级别
     * <p>字符串日志级别</p>
     * <p>1. 优先级比 {@code level} 低, 在 level 打印之后打印</p>
     * <p>2. 使用方式: {@code "error|debug|INFO"} , 不区分大小写，从左往右依次打印，比 level 灵活</p>
     */
    protected String levelStr = "";


    @Override
    public int getLevelCode() {
        return levelCode;
    }

    @Override
    public String getLevelStr() {
        return levelStr;
    }

    @Override
    public int getLogPkg() {
        return logPkg;
    }

    /**
     * @param logPkg    日志框架
     * @param levelCode 日志级别
     */
    public StrategyLogConfig(int logPkg, int levelCode) {
        this.logPkg = logPkg;
        this.levelCode = levelCode;
    }


    /**
     * 默认 debug 日志级别
     *
     * @param logPkg 日志框架
     */
    public StrategyLogConfig(int logPkg) {
        this.logPkg = logPkg;
    }

    /**
     * 默认 slf4j 日志框架
     *
     * @param levelCode 日志级别
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static StrategyLogConfig newSlf4j(int levelCode) {
        return new StrategyLogConfig(LogPkg.SLF4J, levelCode);
    }

    /**
     * 默认 log4j 日志框架
     *
     * @param levelCode 日志级别
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static StrategyLogConfig newLog4j(int levelCode) {
        return new StrategyLogConfig(LogPkg.LOG4J, levelCode);
    }

    /**
     * 默认 log4j2s 日志框架
     *
     * @param levelCode 日志级别
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static StrategyLogConfig newLog4j2(int levelCode) {
        return new StrategyLogConfig(LogPkg.LOG4J2, levelCode);
    }
}
