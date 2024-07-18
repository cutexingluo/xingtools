package top.cutexingluo.tools.aop.log.xtlog.base;

import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.aop.log.xtlog.strategy.WebLogFactory;
import top.cutexingluo.tools.aop.log.xtlog.strategy.WebLogStrategy;
import top.cutexingluo.tools.designtools.method.ClassMaker;
import top.cutexingluo.tools.utils.spring.SpringUtils;

import java.util.Map;

/**
 * WebLog 执行器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/8 15:46
 * @since 1.0.4
 */
public class WebLogProcessor {


    /**
     * <b>获得WebLogStrategy 实例 </b>
     * <p>执行顺序如下</p>
     * <ul>
     *     <li>1. 若 needSpring = true  且 referer 不为空, 从 Spring  中取出 {@link WebLogFactory} 得到 获得WebLogStrategy 实例 </li>
     *     <li>2. 若 needSpring = true  且 referer 不为空, 且1 不存在，则根据 referer名称 和 class 获取 spring bean</li>
     *     <li>3. 若 needSpring = true  且 referer 不为空, 且2 不存在，则根据 referer名称 获取 WebLogStrategy  bean <b>不推荐, 最好referer 和 class 保持一致</b></li>
     *     <li>4. 若 needSpring = true, 或 3 不存在，则根据 class  获取 spring bean</li>
     *     <li>5. 若  4 不存在，则根据 class  反射无参构造获取实例</li>
     * </ul>
     *
     * @param referer 引用的 strategy 名称
     * @param clazz   Strategy 类型
     * @return 返回策略实例
     */
    public static WebLogStrategy getWebLogStrategy(boolean needSpring, @Nullable String referer, @NotNull Class<? extends WebLogStrategy> clazz) {
        WebLogStrategy webLogStrategy = null;
        if (needSpring) {
            if (StrUtil.isNotBlank(referer)) {
                webLogStrategy = getStrategyInFactory(referer);
                if (webLogStrategy == null) { // not find
                    webLogStrategy = SpringUtils.getBeanNoExc(referer, clazz);
                }
                if (webLogStrategy == null) { // not find
                    webLogStrategy = SpringUtils.getBeanNoExc(referer, WebLogStrategy.class);
                }
            }
            if (webLogStrategy == null) {// not find
                webLogStrategy = SpringUtils.getBeanNoExc(clazz);
            }
            if (webLogStrategy != null) { // find return
                return webLogStrategy;
            }
        }
        ClassMaker<? extends WebLogStrategy> maker = new ClassMaker<>(clazz);
        webLogStrategy = maker.newInstanceNoExc();
        return webLogStrategy;
    }

    /**
     * find in WebLogFactory
     */
    @Nullable
    protected static WebLogStrategy getStrategyInFactory(String referer) {
        WebLogFactory webLogFactory = SpringUtils.getBeanNoExc(WebLogFactory.class);
        if (webLogFactory != null) {
            Map<String, WebLogStrategy> strategyMap = webLogFactory.webLogStrategyConfig();
            if (strategyMap != null) {
                return strategyMap.get(referer);
            }
        }
        return null;
    }


}
