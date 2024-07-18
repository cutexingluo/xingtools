package top.cutexingluo.tools.aop.log.xtlog.strategy;

import java.util.Map;

/**
 * WebLog 工厂接口
 * <p>需要实现并注册到容器</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/8 15:22
 * @since 1.0.4
 */
@FunctionalInterface
public interface WebLogFactory {

    Map<String, WebLogStrategy> webLogStrategyConfig();
}
