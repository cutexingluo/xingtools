package top.cutexingluo.tools.utils.log.base;


import top.cutexingluo.tools.utils.log.strategy.LogStrategy;

/**
 * 基础日志配置接口
 * <p>含策略</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/10 14:09
 */
public interface ILogConfig<T extends BaseLogConfig> extends BaseLogConfig {


    /**
     * 日志打印策略
     */
    LogStrategy<T> getLogStrategy();
}
