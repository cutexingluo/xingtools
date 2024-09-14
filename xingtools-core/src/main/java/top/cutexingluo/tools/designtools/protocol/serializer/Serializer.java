package top.cutexingluo.tools.designtools.protocol.serializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 序列化接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/25 19:53
 * @since 1.0.5
 */
public interface Serializer {

    /**
     * 序列化
     */
    <T> byte[] serialize(T obj) throws IOException;

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> clz) throws IOException;


    /**
     * 序列化
     *
     * @param defaultValue 如果obj 为 null  等情况 返回的默认值
     * @since 1.1.4
     */
    default <T> byte[] serialize(T obj, byte[] defaultValue) throws IOException {
        if (Objects.isNull(obj)) return defaultValue;
        return serialize(obj);
    }

    /**
     * 反序列化
     *
     * @param defaultValue data 为 null  等情况 返回的默认值
     * @since 1.1.4
     */
    default <T> T deserialize(byte[] data, Class<T> clz, T defaultValue) throws IOException {
        if (Objects.isNull(data) || data.length == 0) return defaultValue;
        return deserialize(data, clz);
    }
}
