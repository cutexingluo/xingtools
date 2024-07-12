package top.cutexingluo.tools.aop.log.xtlog.strategy;


import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.aop.log.xtlog.base.WebLogConfig;
import top.cutexingluo.tools.aop.log.xtlog.pkg.WebLogKeyMap;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.utils.log.strategy.LogStrategy;

/**
 * WebLog 策略
 * <p>web log 策略 接口</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/8 15:18
 * @since 1.0.4
 */
public interface WebLogStrategy extends LogStrategy<WebLogConfig> {


    /**
     * 默认初始化WebLogKeyMap
     */
    default void initMap(WebLogConfig config, @NotNull WebLogKeyMap msgMap) {
        WebLogKeyMap.defaultKeyMap(msgMap.getKeyMap());
        WebLogKeyMap.defaultKeyConverterMap(msgMap.getKeyConverterMap());
        WebLogKeyMap.defaultKeyHttpConverterMap(msgMap.getKeyHttpConverterMap());
        WebLogKeyMap.defaultKeyWebHttpConverterMap(msgMap.getKeyWebHttpConverterMap());
    }

    default void modifyMap(WebLogConfig config, @NotNull WebLogKeyMap msgMap) {
        this.modifyConfig(config);
    }


    String getMsg(WebLogConfig config, @NotNull WebLogKeyMap msgMap, AspectBundle bundle);


}
