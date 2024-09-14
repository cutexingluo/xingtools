package top.cutexingluo.tools.utils.log.handler;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.baseimpl.XTRunnable;
import top.cutexingluo.tools.utils.log.LogType;
import top.cutexingluo.tools.utils.log.pkg.LogSlf4j;
import top.cutexingluo.tools.utils.log.utils.XTLogUtil;

/**
 * 日志输出Handler
 * <p>通过 new 得到新对象</p>
 *
 * <p>构造需要传入实现的 ILogHandler</p>
 * <p>于 1.1.4 更新</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/6 19:04
 * @updateFrom 1.0.4, 1.1.4
 * @update 2024/1/8 15:00, 2024/8/20 17:23
 */
@Data
public class LogHandler implements ILogHandler {

    /**
     * 日志执行器
     */
    protected ILogHandler logHandler;

    /**
     * 日志级别
     */
    protected int levelCode;


    /**
     * 默认 Slf4j
     */
    public LogHandler(int levelCode) {
        this(new LogSlf4j(), levelCode);
    }


    /**
     * @param logHandler 日志执行器
     * @param levelCode  日志级别
     */
    public LogHandler(@NotNull ILogHandler logHandler, int levelCode) {
        this.logHandler = logHandler;
        this.levelCode = levelCode;
    }

    /**
     * @param logHandler 日志执行器
     * @param type       日志级别
     */
    public LogHandler(@NotNull ILogHandler logHandler, LogType type) {
        this.logHandler = logHandler;
        if (type != null) {
            this.levelCode = type.intCode();
        }
    }

    /**
     * @param logPkg    日志框架包 /   框架包标号 例如 {@link LogSlf4j}
     * @param levelCode 日志级别
     */
    public LogHandler(int logPkg, int levelCode) {
        this(XTLogUtil.getLogHandlerDefault(logPkg), levelCode);
    }

    /**
     * @param logPkg          日志框架包 /   框架包标号 例如 {@link LogSlf4j}
     * @param logHandlerClass 如果 logPkg 为 0 时使用 logHandlerClass 从Spring 中获取
     * @param levelCode       日志级别
     */
    public LogHandler(int logPkg, Class<? extends ILogHandler> logHandlerClass, int levelCode) {
        this(XTLogUtil.getLogHandlerDefaultOrBean(logPkg, logHandlerClass), levelCode);
    }


    /**
     * 将字符串输出语句包装为Runnable
     */
    public XTRunnable getTask(String msg) {
        return getTask(this.levelCode, msg);
    }

    /**
     * 输出语句
     */
    public void send(String msg) {
        getTask(msg).run();
    }


    protected void checkLogHandler() {
        if (this.logHandler == null) {
            throw new NullPointerException("logHandler is null");
        }
    }

    @Override
    public XTRunnable getTask(int levelCode, String msg) {
        checkLogHandler();
        return new XTRunnable(() -> {
            this.logHandler.send(levelCode, msg);
        });
    }

    @Override
    public Runnable getTask(String levelStr, String msg) {
        checkLogHandler();
        return new XTRunnable(() -> {
            this.logHandler.send(levelStr, msg);
        });
    }

    @Override
    public void sendOne(int levelCode, String msg) {
        checkLogHandler();
        this.logHandler.sendOne(levelCode, msg);
    }

}
