package top.cutexingluo.tools.aop.log.xtlog.pkg;


import top.cutexingluo.tools.aop.log.xtlog.base.WebLogConfig;
import top.cutexingluo.tools.basepackage.bundle.AspectBundle;

import java.util.function.BiFunction;


/**
 * WebLog Function
 * <p>转换器</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/13 11:38
 * @since 1.0.4
 */
@FunctionalInterface
public interface WebLogFunction extends BiFunction<WebLogConfig, AspectBundle, String> {

}
