package top.cutexingluo.tools.aop.log.xtlog.pkg;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.aop.log.xtlog.base.WebLogConfig;
import top.cutexingluo.tools.aop.log.xtlog.strategy.WebLogStrategy;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;

import java.util.Objects;

/**
 * WebLogStrategy工具类
 * <p>需要 strategy 策略 和 key - value 键值对数据</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/11 17:17
 * @since 1.0.4
 */
@Data
public class WebLogHandler {

    protected WebLogConfig webLogConfig;

    /**
     * k-v 映射键值对
     */
    protected WebLogKeyMap msgMap;


    public WebLogHandler(@NotNull WebLogConfig webLogConfig) {
        Objects.requireNonNull(webLogConfig);
        Objects.requireNonNull(webLogConfig.getLogStrategy());
        this.webLogConfig = webLogConfig;
        this.msgMap = new WebLogKeyMap();
    }

    public WebLogHandler(WebLogConfig webLogConfig, WebLogKeyMap msgMap) {
        Objects.requireNonNull(webLogConfig);
        Objects.requireNonNull(webLogConfig.getLogStrategy());
        this.webLogConfig = webLogConfig;
        this.msgMap = msgMap;
    }

    /**
     * 利用策略 初始化默认Map
     * <p>初始化操作</p>
     */
    public WebLogHandler initDefaultMap() {
        Objects.requireNonNull(webLogConfig.getLogStrategy());
        webLogConfig.getLogStrategy().initMap(webLogConfig, msgMap);
        return this;
    }

    /**
     * 利用策略 修改 config
     * <p>更改性操作</p>
     */
    public WebLogHandler modifyAll() {
        WebLogStrategy strategy = webLogConfig.getLogStrategy();
        Objects.requireNonNull(strategy);
        strategy.modifyMap(webLogConfig, msgMap);
        return this;
    }

    /**
     * 获取处理后的消息
     */
    public String getMsg(AspectBundle bundle) {
        Objects.requireNonNull(webLogConfig.getLogStrategy());
        return webLogConfig.getLogStrategy().getMsg(webLogConfig, msgMap, bundle);
    }

//    /**
//     * 利用策略发送 log
//     *
//     * @return 是否发送成功
//     */
//    public boolean send(Method method, HttpServletRequest request, @Nullable ProceedingJoinPoint joinPoint) {
//        AspectBundle bundle = new AspectBundle(method, request, joinPoint);
//        return send(bundle);
//    }

    /**
     * 利用直接发送 log
     *
     * @return 是否发送成功
     */
    public boolean send(AspectBundle bundle) {
        String msg = getMsg(bundle);
        return send(msg);
    }

    /**
     * 利用策略发送 log
     *
     * @return 是否发送成功
     */
    public boolean send(String msg) {
        return webLogConfig.getLogStrategy().log(webLogConfig, webLogConfig.getLogProvider(), msg);
    }


}
