package top.cutexingluo.tools.utils.log.pkg;

import top.cutexingluo.tools.utils.log.handler.ILogHandler;

/**
 * Logger 服务提供接口
 * <p>泛型为 任意日志框架对象，通常为 ?</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/11 14:45
 * @since 1.0.4
 */
public interface ILogProvider<T> extends ILog<T>, ILogHandler {
}
