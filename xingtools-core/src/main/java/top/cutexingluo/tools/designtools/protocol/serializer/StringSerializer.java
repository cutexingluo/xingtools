package top.cutexingluo.tools.designtools.protocol.serializer;

import java.io.IOException;
import java.util.Objects;

/**
 * String 类型序列化接口
 *
 * <p>方法名模仿 js</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/25 20:14
 * @since 1.0.5
 */
public interface StringSerializer {


    /**
     * 序列化
     */
    <T> String stringify(T obj) throws IOException;

    /**
     * 反序列化
     */
    <T> T parse(String data, Class<T> clz) throws IOException;


    /**
     * 序列化
     *
     * @param defaultValue 如果 obj 为 null  等情况 返回的默认值
     * @since 1.1.4
     */
    default <T> String stringify(T obj, String defaultValue) throws IOException {
        if (Objects.isNull(obj)) return defaultValue;
        return stringify(obj);
    }

    /**
     * 反序列化
     *
     * @param defaultValue 如果 data 为 null 等情况 返回的默认值
     * @since 1.1.4
     */
    default <T> T parse(String data, Class<T> clz, T defaultValue) throws IOException {
        if (Objects.isNull(data)) return defaultValue;
        return parse(data, clz);
    }
}
