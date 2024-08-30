package top.cutexingluo.tools.aop.log.xtlog.strategy.impl;

import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.aop.log.xtlog.base.WebLogConfig;
import top.cutexingluo.tools.aop.log.xtlog.pkg.WebLogKeyMap;
import top.cutexingluo.tools.aop.log.xtlog.strategy.WebLogStrategy;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;
import top.cutexingluo.tools.utils.log.strategy.impl.DefaultLogStrategy;
import top.cutexingluo.tools.utils.se.character.symbol.SymbolPairEnum;
import top.cutexingluo.tools.utils.se.string.XTString;

/**
 * 常规获取方式
 * <p>从 msgMap 中转化 msg </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/15 16:26
 */

public class CommonWebLogStrategy extends DefaultLogStrategy<WebLogConfig> implements WebLogStrategy {

    /**
     * 前驱
     */
    public static String prefixBraces = SymbolPairEnum.INTERPOLATION_BRACES.getPrefix();

    /**
     * 后驱
     */
    public static String suffixBraces = SymbolPairEnum.INTERPOLATION_BRACES.getSuffix();

    @Override
    public String getMsg(WebLogConfig config, @NotNull WebLogKeyMap msgMap, AspectBundle bundle) {
        if (config == null) return "";
        if (StrUtil.isNotBlank(config.getMsg())) {
            String msg = config.getMsg(); // not trim
            return new XTString(msg)
                    .replaceAllBetweenPatterns(prefixBraces, suffixBraces, (s) -> {
                        String value = msgMap.getValue(s, 0, config, bundle);
                        if (value == null) value = "";
                        return value;
                    });
        }
        return config.getMsg(); // msg
    }
}
