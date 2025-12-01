package top.cutexingluo.tools.designtools.json.serializer.pkg.sensitive;

import top.cutexingluo.tools.designtools.json.serializer.StrJsonStrategy;

/**
 * 敏感过滤器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/1 15:37
 * @since 1.0.4
 */
public class SensitiveSerializer implements StrJsonStrategy {

    protected final StrJsonStrategy desensitize;

    public SensitiveSerializer() {
        this("");
    }

    public SensitiveSerializer(String name) {
        desensitize = SensitiveStrategy.getStrategy(name).desensitize();
    }

    @Override
    public String toJsonStr(String originStr) {
        return desensitize.toJsonStr(originStr);
    }
}
