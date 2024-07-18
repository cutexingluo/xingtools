package top.cutexingluo.tools.aop.log.xtlog.base;


import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.aop.log.xtlog.pkg.WebLogKeyMap;
import top.cutexingluo.tools.aop.log.xtlog.strategy.WebLogStrategy;
import top.cutexingluo.tools.aop.log.xtlog.strategy.impl.DefaultWebLogStrategy;
import top.cutexingluo.tools.aop.systemlog.XTSystemLog;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.utils.log.LogPkg;
import top.cutexingluo.tools.utils.log.XTLogConfig;
import top.cutexingluo.tools.utils.log.pkg.ILogProvider;
import top.cutexingluo.tools.utils.log.pkg.LogSlf4j;
import top.cutexingluo.tools.utils.log.utils.XTLogUtil;
import top.cutexingluo.tools.utils.se.obj.ChooseUtil;

/**
 * WebLogConfig 配置
 *
 * <p>可提供的配置为 WebLogConfig </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/8 14:00
 * @since 1..0.4
 */

public class WebLogConfig extends XTLogConfig<WebLogStrategy, WebLogConfig> {


    /**
     * 匹配打印模式
     * <p>使用方式 {@code "ip+httpMethod:args"} ( 默认+组装key , : 换行)</p>
     * <p>可以实现 strategy 增加修改 key-value 对</p>
     * <p>会被  msg 覆盖</p>
     *
     * @see WebLogKeyMap
     * @see top.cutexingluo.tools.aop.log.xtlog.LogKey
     * @see top.cutexingluo.tools.aop.log.xtlog.LogVarKey
     */
    protected String match;
    /**
     * 精确打印模式
     * <p>使用方式 {@code "IP地址:    ${ip}, 参数:  ${args}"} (常规模式串 ${key}进行替换) 也可直接使用常量 {@code LogKey.ALL}</p>
     * <p>可以实现 strategy 增加修改 key-value 对</p>
     * <p>会覆盖 match</p>
     *
     * @see WebLogKeyMap
     * @see top.cutexingluo.tools.aop.log.xtlog.LogKey
     * @see top.cutexingluo.tools.aop.log.xtlog.LogVarKey
     */
    protected String msg;

    /**
     * SpEL打印模式
     * <p>SpEL 表达式打印，方法返回值必须为 String.class </p>
     * <p>1. 该项会覆盖msg, 并且 keyMap 设置的值会失效</p>
     * <p>2. 使用bean调用, 例如 {@code  "@testService.print( #msgMap, 'hello world')"}   其中
     * msgMap 为 {@link WebLogKeyMap}对象</p>
     * <p>3. rootObject 为 切点对象(可以直接调用当前类所在的方法)，若为空则为 strategy() 对象</p>
     * <p>4. 提供三种基本变量 {@link WebLogKeyMap} #msgMap,  {@link WebLogConfig} #config , {@link AspectBundle} #bundle  </p>
     *
     * @see top.cutexingluo.tools.utils.se.obj.spel.SpELObject
     */
    protected String spEL;
    /**
     * 业务名称
     * <p>keyMap 的 key = bName, 匹配字段 bName , 精确字段为 {@code ${bName} }</p>
     * <p>兼容 {@link XTSystemLog}</p>
     */
    protected String businessName;


    /**
     * 日志种类
     *
     * <p>日志提供者</p>
     */
    protected ILogProvider<?> logProvider;


    public WebLogConfig(WebLogStrategy webLogStrategy) {
        super(webLogStrategy);
    }

    public WebLogConfig(@NotNull WebLog webLog) {
        this(webLog, chooseWebLogStrategy(webLog, null));
    }


    public WebLogConfig(@NotNull WebLog webLog, WebLogStrategy webLogStrategy) {
        this(webLog, webLogStrategy,
                chooseLogProvider(webLog, null)
        );
    }

    public WebLogConfig(@NotNull WebLog webLog, WebLogStrategy webLogStrategy, ILogProvider<?> logProvider) {
        super(webLog.index(), webLog.key(), webLog.logPkg(), webLog.level(), webLog.levelStr(),
                webLogStrategy);
        this.match = webLog.match();
        this.msg = webLog.msg();
        this.spEL = webLog.spEL();
        this.businessName = webLog.businessName();
        this.logProvider = logProvider;
    }

    /**
     * @param webLog      方法上注解
     * @param classWebLog 类上注解
     */
    public WebLogConfig(@NotNull WebLog webLog, @Nullable WebLog classWebLog) {
        this(webLog, classWebLog,
                chooseWebLogStrategy(webLog, classWebLog),
                chooseLogProvider(webLog, classWebLog));
    }

    /**
     * @param webLog      方法上注解
     * @param classWebLog 类上注解
     */
    public WebLogConfig(@NotNull WebLog webLog, @Nullable WebLog classWebLog, WebLogStrategy webLogStrategy, ILogProvider<?> logProvider) {
        super(
                classWebLog != null ? chooseInt(classWebLog.index(), webLog.index()) : webLog.index(),
                classWebLog != null ? chooseStr(classWebLog.key(), webLog.key()) : webLog.key(),
                classWebLog != null ? chooseInt(classWebLog.logPkg(), webLog.logPkg()) : webLog.logPkg(),
                classWebLog != null ? chooseInt(classWebLog.level(), webLog.level()) : webLog.level(),
                classWebLog != null ? chooseStr(classWebLog.levelStr(), webLog.levelStr()) : webLog.levelStr(),
                webLogStrategy);
        this.match = classWebLog != null ? chooseStr(classWebLog.match(), webLog.match()) : webLog.match();
        // 防止 类 msg 覆盖 方法 match
        this.msg = classWebLog != null ? chooseStr(StrUtil.isNotBlank(webLog.match()) ? "" : classWebLog.msg(), webLog.msg()) : webLog.msg();
        this.spEL = classWebLog != null ? chooseStr(StrUtil.isNotBlank(webLog.msg()) ? "" : classWebLog.spEL(), webLog.spEL()) : webLog.spEL();
        this.businessName = classWebLog != null ? chooseStr(classWebLog.businessName(), webLog.businessName()) : webLog.businessName();
        this.logProvider = logProvider;
    }


    /**
     * 推荐使用，可以复用不用新生成新的对象
     * <p>推荐使用</p>
     *
     * @param webLog  方法上注解
     * @param setting 类对象配置
     */
    public WebLogConfig(@NotNull WebLog webLog, @NotNull WebLogSetting setting) {
        super(
                chooseInt(setting.classLogConfig.index, webLog.index()),
                chooseStr(setting.classLogConfig.key, webLog.key()),
                chooseInt(setting.classLogConfig.logPkg, webLog.logPkg()),
                chooseInt(setting.classLogConfig.levelCode, webLog.level()),
                chooseStr(setting.classLogConfig.levelStr, webLog.levelStr()),
                takeWebLogStrategy(webLog, setting.logStrategy));
        this.match = chooseStr(setting.classLogConfig.match, webLog.match());
        // 防止 类 msg 覆盖 方法 match
        this.msg = chooseStr(StrUtil.isNotBlank(webLog.match()) ? "" : setting.classLogConfig.msg, webLog.msg());
        this.spEL = chooseStr(StrUtil.isNotBlank(webLog.msg()) ? "" : setting.classLogConfig.spEL, webLog.spEL());
        this.businessName = chooseStr(setting.classLogConfig.businessName, webLog.businessName());
        this.logProvider = takeLogProvider(webLog, setting.logProvider);
    }

    /**
     * chooseWebLogStrategy
     */
    protected static WebLogStrategy chooseWebLogStrategy(@NotNull WebLog webLog, @Nullable WebLog classWebLog) {
        // unChange
        if (classWebLog != null && StrUtil.isBlank(webLog.referer()) && DefaultWebLogStrategy.class.equals(webLog.strategy())) {
            return WebLogProcessor.getWebLogStrategy(
                    classWebLog.needSpring(), classWebLog.referer(), classWebLog.strategy());
        }
        return WebLogProcessor.getWebLogStrategy(
                webLog.needSpring(), webLog.referer(), webLog.strategy());
    }

    /**
     * takeWebLogStrategy
     */
    protected static WebLogStrategy takeWebLogStrategy(@NotNull WebLog webLog, WebLogStrategy webLogStrategy) {
        // unChange
        if (webLogStrategy != null && StrUtil.isBlank(webLog.referer()) && DefaultWebLogStrategy.class.equals(webLog.strategy())) {
            return webLogStrategy;
        }
        return WebLogProcessor.getWebLogStrategy(
                webLog.needSpring(), webLog.referer(), webLog.strategy());
    }

    /**
     * takeLogProvider
     */
    protected static <T> ILogProvider<T> takeLogProvider(@NotNull WebLog webLog, @Nullable ILogProvider<T> logProvider) {
        // unChange
        if (logProvider != null && webLog.logPkg() == LogPkg.SLF4J && webLog.logProviderClass() == LogSlf4j.class) {
            return logProvider;
        }
        return webLog.needSpring() ?
                XTLogUtil.getLogProviderDefaultOrBean(webLog.logPkg(), webLog.logProviderClass()) :
                XTLogUtil.getLogProviderDefault(webLog.logPkg());

    }

    /**
     * chooseLogProvider
     */
    protected static <T> ILogProvider<T> chooseLogProvider(@NotNull WebLog webLog, @Nullable WebLog classWebLog) {
        // unChange
        if (classWebLog != null && webLog.logPkg() == LogPkg.SLF4J && webLog.logProviderClass() == LogSlf4j.class) {
            return classWebLog.needSpring() ?
                    XTLogUtil.getLogProviderDefaultOrBean(classWebLog.logPkg(), classWebLog.logProviderClass()) :
                    XTLogUtil.getLogProviderDefault(classWebLog.logPkg());
        }
        return webLog.needSpring() ?
                XTLogUtil.getLogProviderDefaultOrBean(webLog.logPkg(), webLog.logProviderClass()) :
                XTLogUtil.getLogProviderDefault(webLog.logPkg());

    }


    protected static int chooseInt(int classInt, int methodInt) {
        return ChooseUtil.checkZeroOverride(methodInt, classInt);
    }

    protected static String chooseStr(String classStr, String methodStr) {
        return ChooseUtil.checkBlankOverride(methodStr, classStr);
    }

    //-----------------change---------------------

    /**
     * 被其他对象覆盖
     * <p>不存在才被覆盖</p>
     *
     * @param webLogConfig 其他对象
     */
    public WebLogConfig isOverrideByOther(@NotNull WebLogConfig webLogConfig) {
        this.index = chooseInt(webLogConfig.index, this.index);
        this.key = chooseStr(webLogConfig.key, this.key);
        this.logPkg = chooseInt(webLogConfig.logPkg, this.logPkg);
        this.levelCode = chooseInt(webLogConfig.levelCode, this.levelCode);
        this.levelStr = chooseStr(webLogConfig.levelStr, this.levelStr);
        this.match = chooseStr(webLogConfig.match, this.match);
        // 防止 类 msg 覆盖 方法 match
        this.msg = chooseStr(StrUtil.isNotBlank(this.match) ? "" : webLogConfig.msg, this.msg);
        this.spEL = chooseStr(StrUtil.isNotBlank(this.msg) ? "" : webLogConfig.spEL, this.spEL);
        this.businessName = chooseStr(webLogConfig.businessName, this.businessName);
        return this;
    }


    /**
     * 切换策略
     */
    public WebLogConfig changeStrategy(boolean needSpring, String referer, Class<? extends WebLogStrategy> strategyClass) {
        WebLogStrategy strategy = WebLogProcessor.getWebLogStrategy(
                needSpring, referer, strategyClass);
        this.setLogStrategy(strategy);
        return this;
    }


    //-----------------default---------------------

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getMsg() {
        return msg;
    }

    public String getSpEL() {
        return spEL;
    }

    public void setSpEL(String spEL) {
        this.spEL = spEL;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public ILogProvider<?> getLogProvider() {
        return logProvider;
    }

    public void setLogProvider(ILogProvider<?> logProvider) {
        this.logProvider = logProvider;
    }

    public WebLogConfig(WebLogStrategy logStrategy, String match, String msg, String businessName, ILogProvider<?> logProvider) {
        super(logStrategy);
        this.match = match;
        this.msg = msg;
        this.businessName = businessName;
        this.logProvider = logProvider;
    }

    public WebLogConfig(int index, String key, int logPkg, int levelCode, String levelStr, WebLogStrategy logStrategy, String match, String msg, String businessName, ILogProvider<?> logProvider) {
        super(index, key, logPkg, levelCode, levelStr, logStrategy);
        this.match = match;
        this.msg = msg;
        this.businessName = businessName;
        this.logProvider = logProvider;
    }

    public WebLogConfig(int logPkg, int levelCode, WebLogStrategy logStrategy, String match, String msg, String businessName, ILogProvider<?> logProvider) {
        super(logPkg, levelCode, logStrategy);
        this.match = match;
        this.msg = msg;
        this.businessName = businessName;
        this.logProvider = logProvider;
    }

    public WebLogConfig(int logPkg, WebLogStrategy logStrategy, String match, String msg, String businessName, ILogProvider<?> logProvider) {
        super(logPkg, logStrategy);
        this.match = match;
        this.msg = msg;
        this.businessName = businessName;
        this.logProvider = logProvider;
    }
}
