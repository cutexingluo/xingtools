package top.cutexingluo.tools.designtools.json.serializer;

import lombok.NoArgsConstructor;

/**
 * 默认格式化策略
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/1 14:37
 * @since 1.0.4
 */
@NoArgsConstructor
public class DefaultStrJsonStrategy implements StrJsonStrategy {
    @Override
    public String toJsonStr(String originStr) {
        return originStr;
    }
}
