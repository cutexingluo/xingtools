package top.cutexingluo.tools.designtools.json.serializer;

/**
 * json 字符串序列化策略
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/1 14:27
 * @since 1.0.4
 */
@FunctionalInterface
public interface StrJsonStrategy {
    String toJsonStr(String originStr);
}
