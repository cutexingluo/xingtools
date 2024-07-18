package top.cutexingluo.tools.designtools.protocol.serializer.impl.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import top.cutexingluo.tools.common.base.IData;
import top.cutexingluo.tools.designtools.protocol.serializer.Serializer;
import top.cutexingluo.tools.designtools.protocol.serializer.StringSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Jackson 序列化器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/25 20:01
 * @since 1.0.5
 */
@Data
public class JacksonSerializer implements Serializer, StringSerializer, IData<ObjectMapper> {

    protected ObjectMapper objectMapper;

    public JacksonSerializer() {
        this.objectMapper = new ObjectMapper();
    }

    public JacksonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ObjectMapper data() {
        return objectMapper;
    }

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        return objectMapper.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        return objectMapper.readValue(data, clz);
    }

    @Override
    public <T> String stringify(T obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }

    @Override
    public <T> T parse(String data, Class<T> clz) throws IOException {
        return objectMapper.readValue(data, clz);
    }


    //-----默认初始化方法列表----------

    public JacksonSerializer initDefault(JsonInclude.Include include) {
        objectMapper.setSerializationInclusion(include);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return this;
    }

    public JacksonSerializer initDefault() {
        return initDefault(JsonInclude.Include.ALWAYS);
    }


    /**
     * Redis 序列化常用配置
     */
    public JacksonSerializer initRedis() {
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会抛出异常
//        obm.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // 解决jackson无法反序列化LocalDateTime的问题
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        return this;
    }


}
