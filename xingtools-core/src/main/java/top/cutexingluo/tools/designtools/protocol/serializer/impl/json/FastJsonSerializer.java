package top.cutexingluo.tools.designtools.protocol.serializer.impl.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import top.cutexingluo.tools.designtools.protocol.serializer.Serializer;
import top.cutexingluo.tools.designtools.protocol.serializer.StringSerializer;

import java.io.IOException;

/**
 * FastJson 序列化器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/25 20:03
 * @since 1.0.5
 */
public class FastJsonSerializer implements Serializer, StringSerializer {

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        if (obj == null) {
            return new byte[0];
        }
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        if (data == null || data.length == 0) {
            return null;
        }
        return JSON.parseObject(data, clz);
    }

    @Override
    public <T> String stringify(T obj) throws IOException {
        if (obj == null) {
            return "";
        }
        return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
    }

    @Override
    public <T> T parse(String data, Class<T> clz) throws IOException {
        if (data == null) {
            return null;
        }
        return JSON.parseObject(data, clz);
    }
}
