package top.cutexingluo.tools.utils.log.strategy;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.log.XTLog;
import top.cutexingluo.tools.utils.log.base.BaseLogConfig;
import top.cutexingluo.tools.utils.log.handler.ILogHandler;
import top.cutexingluo.tools.utils.log.handler.LogHandler;
import top.cutexingluo.tools.utils.log.pkg.ILogProvider;

/**
 * 日志打印策略
 * <p>配合 ILogConfig  和 ILog 实现类 使用</p>
 *
 * <p>日志使用推荐：</p>
 * <p>1. 通过 {@link LogStrategy} 实现类 来控制日志输出, 通过调用实现类 或者 使用 {@link XTLog} 输出</p>
 * <p>2. 通过 {@link ILogHandler} 实现类 来控制日志输出, 通过调用实现类 或者 使用 {@link LogHandler} 输出</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/10 11:10
 * @since 1.0.4
 */
@FunctionalInterface
public interface LogStrategy<T extends BaseLogConfig> {

    /**
     * 修改配置
     */
    default void modifyConfig(@NotNull T config) {

    }

    /**
     * 打印入口
     *
     * <p>注意: 不能在才方法内调用 config 的 strategy , 否则会死循环</p>
     *
     * @param log 日志对象
     * @return 是否打印
     */
    boolean log(T config, ILogProvider<?> log, String msg);

    /**
     * 打印入口
     * <p>如果 config code 不存在, 则使用 HuLog</p>
     * <p>注意: 不能在才方法内调用 config 的 strategy , 否则会死循环</p>
     */
    default boolean log(T config, String msg) {
        return log(config, null, msg);
    }


    /**
     * 打印入口
     * <p>默认使用HuLog</p>
     */
    default boolean log(String msg) {
        return log(null, null, msg);
    }


}
