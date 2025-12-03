package top.cutexingluo.tools.utils.ee.fastjson;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import top.cutexingluo.core.basepackage.struct.ExtInitializable;


/**
 * Redis使用FastJson序列化
 *
 * <p>需要导入 com.alibaba:fastjson 包</p>
 *
 * @author sg and XingTian
 */
public class FastJsonRedisSerializer<T> implements RedisSerializer<T>, ExtInitializable<FastJsonRedisSerializer<T>> {


    protected Class<T> clazz;

    public FastJsonRedisSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONBytes(t, SerializerFeature.WriteClassName);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str = new String(bytes);

        return JSON.parseObject(str, clazz);
    }


    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }

    @Override
    public FastJsonRedisSerializer<T> initSelf() {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        return this;
    }
}