package top.cutexingluo.tools.utils.log.base;

/**
 * <p> 基本 log 配置接口</p>
 * <p><b>最基本的日志配置接口</b></p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/11 11:55
 * @since 1.0.4
 */
public interface BaseLogConfig {

    /**
     * 日志级别
     */
    int getLevelCode();


    /**
     * 日志级别字符串
     */
    String getLevelStr();


    /**
     * 日志框架
     */
    int getLogPkg();
}
