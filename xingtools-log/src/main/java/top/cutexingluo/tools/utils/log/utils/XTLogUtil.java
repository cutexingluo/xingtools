package top.cutexingluo.tools.utils.log.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.utils.log.LogPkg;
import top.cutexingluo.tools.utils.log.XTLog;
import top.cutexingluo.tools.utils.log.base.BaseLogConfig;
import top.cutexingluo.tools.utils.log.handler.ILogHandler;
import top.cutexingluo.tools.utils.log.pkg.ILog;
import top.cutexingluo.tools.utils.log.pkg.ILogProvider;
import top.cutexingluo.tools.utils.spring.SpringUtils;

/**
 * 提供给系列 Log 接口和实现类 的工具类
 *
 * <p>该类部分方法需要使用 SpringUtils ，所以需要导入 spring-boot-starter, xingcore-extra</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/10 18:09
 */
public class XTLogUtil {

    /**
     * 从配置获取 log 对象
     * <p>优先获取 config 里面的 logPkg</p>
     *
     * @param config     config 对象,
     * @param defaultLog log 对象, 如果为空，则从 config 里面生成
     */
    @NotNull
    public static <C extends BaseLogConfig> ILog<?> getLog(@Nullable C config, @Nullable ILog<?> defaultLog) {
        int code = config == null ? 0 : config.getLogPkg();
        ILog<?> returnLog = new LogPkg(code).getLog(() -> defaultLog);
        if (returnLog == null) {
            returnLog = new XTLog.HuLog();
        }
        return returnLog;
    }


    /**
     * 获取默认 logProvider 对象
     *
     * @param logPkgCode 日志包编码, 如 LOG4J = 1, LOG4J2 = 2, SLF4J = 4, 如果为0, 则从spring 获取 logHandler Bean
     */
    public static <T> ILogProvider<T> getLogProviderDefault(int logPkgCode) {
        return new LogPkg(logPkgCode).getLogProvider();
    }

    /**
     * 获取默认logHandler 对象 或者 logHandler  Bean对象
     *
     * @param logPkgCode 日志包编码, 如 LOG4J = 1, LOG4J2 = 2, SLF4J = 4, 如果为0, 则从spring 获取 logHandler Bean
     * @param clazz      logHandler Bean的class
     */
    public static <T> ILogProvider<T> getLogProviderDefaultOrBean(int logPkgCode, Class<? extends ILogProvider<?>> clazz) {
        return (ILogProvider<T>) new LogPkg(logPkgCode).getLogProvider(() -> SpringUtils.getBeanNoExc(clazz));
    }


    /**
     * 获取默认log 对象
     *
     * @param logPkgCode 日志包编码, 如 LOG4J = 1, LOG4J2 = 2, SLF4J = 4, 如果为0, 则从spring 获取 log Bean
     */
    public static <T> ILog<T> getLogDefault(int logPkgCode) {
        return new LogPkg(logPkgCode).getLog();
    }

    /**
     * 获取默认log 对象 或者 log Bean对象
     *
     * @param logPkgCode 日志包编码, 如 LOG4J = 1, LOG4J2 = 2, SLF4J = 4, 如果为0, 则从spring 获取 log Bean
     * @param clazz      log Bean的class
     */
    public static <T> ILog<T> getLogDefaultOrBean(int logPkgCode, Class<? extends ILog<T>> clazz) {
        return new LogPkg(logPkgCode).getLog(() -> SpringUtils.getBeanNoExc(clazz));
    }


    /**
     * 获取默认logHandler 对象
     *
     * @param logPkgCode 日志包编码, 如 LOG4J = 1, LOG4J2 = 2, SLF4J = 4, 如果为0, 则从spring 获取 logHandler Bean
     */
    public static ILogHandler getLogHandlerDefault(int logPkgCode) {
        return new LogPkg(logPkgCode).getLogHandler();
    }

    /**
     * 获取默认logHandler 对象 或者 logHandler  Bean对象
     *
     * @param logPkgCode 日志包编码, 如 LOG4J = 1, LOG4J2 = 2, SLF4J = 4, 如果为0, 则从spring 获取 logHandler Bean
     * @param clazz      logHandler Bean的class
     */
    public static ILogHandler getLogHandlerDefaultOrBean(int logPkgCode, Class<? extends ILogHandler> clazz) {
        return new LogPkg(logPkgCode).getLogHandler(() -> SpringUtils.getBeanNoExc(clazz));
    }


}
