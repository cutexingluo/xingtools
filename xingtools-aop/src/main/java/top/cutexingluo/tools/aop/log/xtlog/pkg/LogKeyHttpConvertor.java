package top.cutexingluo.tools.aop.log.xtlog.pkg;

import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.data.Entry;

import java.util.Map;

/**
 * WebLogFunction 二元组
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/13 13:27
 * @since 1.0.4
 */
public class LogKeyHttpConvertor extends Entry<String, WebLogFunction> {
    public LogKeyHttpConvertor(String key, WebLogFunction value) {
        super(key, value);
    }

    public LogKeyHttpConvertor(@NotNull Map.Entry<String, WebLogFunction> entry) {
        super(entry.getKey(), entry.getValue());
    }
}
