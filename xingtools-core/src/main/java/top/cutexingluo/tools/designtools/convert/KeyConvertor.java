package top.cutexingluo.tools.designtools.convert;


import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.data.Entry;

import java.util.Map;
import java.util.function.Supplier;

/**
 * key  转化器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/12 10:06
 * @since 1.0.4
 */

public class KeyConvertor extends Entry<String, Supplier<String>> {

    /**
     * 构造
     *
     * @param key   键
     * @param value 值
     */
    public KeyConvertor(String key, Supplier<String> value) {
        super(key, value);
    }


    /**
     * 构造
     *
     * @param entry 键值对
     */
    public KeyConvertor(@NotNull Map.Entry<String, Supplier<String>> entry) {
        super(entry.getKey(), entry.getValue());
    }
}
