package top.cutexingluo.tools.designtools.protocol.serializer;

import java.io.IOException;

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


}
