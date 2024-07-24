package top.cutexingluo.tools.designtools.convert;

import cn.hutool.core.lang.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * key http 转化器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/12 10:32
 * @since 1.0.4
 */
public class KeyHttpConvertor extends Pair<String, WebHandler> {
    /**
     * 构造
     *
     * @param key   键
     * @param value 值
     */
    public KeyHttpConvertor(String key, WebHandler value) {
        super(key, value);
    }


    /**
     * 构造
     *
     * @param entry 键值对
     */
    public KeyHttpConvertor(@NotNull Map.Entry<String, WebHandler> entry) {
        super(entry.getKey(), entry.getValue());
    }
}
