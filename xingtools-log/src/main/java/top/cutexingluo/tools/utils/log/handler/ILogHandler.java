package top.cutexingluo.tools.utils.log.handler;


import top.cutexingluo.tools.utils.log.LogLevel;
import top.cutexingluo.tools.utils.log.XTLog;
import top.cutexingluo.tools.utils.log.strategy.LogStrategy;

/**
 * 日志执行器
 * <p>可以配合 LogHandler 使用</p>
 *
 * <p>日志使用推荐：</p>
 * <p>1. 通过 {@link LogStrategy} 实现类 来控制日志输出, 通过调用实现类 或者 使用 {@link XTLog} 输出</p>
 * <p>2. 通过 {@link ILogHandler} 实现类 来控制日志输出, 通过调用实现类 或者 使用 {@link LogHandler} 输出</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/10 15:12
 * @since 1.0.4
 */
public interface ILogHandler {


    Runnable getTask(int levelCode, String msg);

    /**
     * <p>于1.1.4 转为默认方法</p>
     */
    default Runnable getTask(String levelStr, String msg) {
        return () -> {
            int[] codes = LogLevel.getLevelCodesByBit(levelStr);
            for (int code : codes) {
                sendOne(code, msg);
            }
        };
    }


    /**
     * 输出一条语句
     */
    void sendOne(int levelCode, String msg);

    /**
     * 输出语句
     */
    default void send(int levelCode, String msg) {
        getTask(levelCode, msg).run();
    }


    /**
     * 输出语句
     * <p>字符串日志级别</p>
     */
    default void send(String levelStr, String msg) {
        getTask(levelStr, msg).run();
    }
}
