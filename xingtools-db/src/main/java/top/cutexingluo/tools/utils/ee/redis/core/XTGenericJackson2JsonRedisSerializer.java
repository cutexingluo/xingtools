package top.cutexingluo.tools.utils.ee.redis.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;

/**
 * <p>XTGenericJackson2JsonRedisSerializer 序列化工具</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 11:04
 */
public class XTGenericJackson2JsonRedisSerializer extends GenericJackson2JsonRedisSerializer {

    @Override
    public byte[] serialize(Object object) throws SerializationException {
        if (object == null) return new byte[0];
        if (object instanceof Long || object instanceof Double)
            return object.toString().getBytes(StandardCharsets.UTF_8);
        try {
            return JSON.toJSONBytes(object, SerializerFeature.WriteClassName);
//                return JSONUtil.toJsonStr(object).getBytes(StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new SerializationException("Could not serialize : " + exception.getMessage(), exception);
        }
    }
}
