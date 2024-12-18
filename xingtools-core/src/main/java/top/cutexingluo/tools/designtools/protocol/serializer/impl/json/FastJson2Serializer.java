package top.cutexingluo.tools.designtools.protocol.serializer.impl.json;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import top.cutexingluo.tools.designtools.protocol.serializer.Serializer;
import top.cutexingluo.tools.designtools.protocol.serializer.StringSerializer;

import java.io.IOException;

/**
 * FastJson2 序列化器
 *
 * <p>需要导入 com.alibaba.fastjson2:fastjson2 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/25 21:22
 * @since 1.0.5
 */
public class FastJson2Serializer implements Serializer, StringSerializer {


    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        return JSON.parseObject(data, clz);
    }

    @Override
    public <T> String stringify(T obj) {
        return JSON.toJSONString(obj, JSONWriter.Feature.WriteClassName);
    }

    @Override
    public <T> T parse(String data, Class<T> clz) throws IOException {
        return JSON.parseObject(data, clz);
    }
}
