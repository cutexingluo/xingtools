package top.cutexingluo.tools.designtools.protocol.serializer.impl.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.base.IData;
import top.cutexingluo.tools.designtools.protocol.adapter.ModuleAdapter;
import top.cutexingluo.tools.designtools.protocol.adapter.impl.JacksonAdapter;
import top.cutexingluo.tools.designtools.protocol.jackson.adapter.JacksonDefaultAdapter;
import top.cutexingluo.tools.designtools.protocol.jackson.adapter.JacksonRedisAdapter;
import top.cutexingluo.tools.designtools.protocol.jackson.adapter.JacksonToFastJsonAdapter;
import top.cutexingluo.tools.designtools.protocol.serializer.Serializer;
import top.cutexingluo.tools.designtools.protocol.serializer.StringSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.function.Consumer;

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

    //------------ other ------------


    //------------ builder ------------

    /**
     * 自定义初始化
     *
     * @since 1.1.4
     */
    public JacksonSerializer initMapper(@NotNull Consumer<ObjectMapper> consumer) {
        consumer.accept(objectMapper);
        return this;
    }

    /**
     * 自定义适配器注册
     *
     * @since 1.1.4
     */
    public JacksonSerializer registerAdapter(@NotNull ModuleAdapter<ObjectMapper> adapter) {
        adapter.setMapper(objectMapper).initSelf();
        return this;
    }

    /**
     * 自定义适配器注册
     *
     * @since 1.1.4
     */
    public JacksonSerializer registerAdapter(@NotNull JacksonAdapter adapter) {
        adapter.setMapper(objectMapper).initSelf();
        return this;
    }

    //-----默认初始化方法列表----------

    /**
     * 使用默认配置
     */
    public JacksonSerializer initDefault() {
        registerAdapter(new JacksonDefaultAdapter());
        return this;
    }

    /**
     * Redis 序列化常用配置
     *
     * @since 1.1.4
     */
    public JacksonSerializer initRedis() {
        registerAdapter(new JacksonRedisAdapter());
        return this;
    }

    /**
     * 模拟 fastjson 配置
     *
     * @since 1.1.4
     */
    public JacksonSerializer initToFactJson() {
        registerAdapter(new JacksonToFastJsonAdapter());
        return this;
    }

    //------------ include ------------

    public JacksonSerializer setSerializationInclusion(JsonInclude.Include include) {
        objectMapper.setSerializationInclusion(include);
        return this;
    }

    /**
     * 仅序列化非 null 字段
     * <p>忽略 null 字段</p>
     *
     * @since 1.1.4
     */
    public JacksonSerializer includeNonNull() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return this;
    }

    //------------ ext ------------

    /**
     * 使用默认配置
     */
    public JacksonSerializer initDefault(JsonInclude.Include include) {
        objectMapper.setSerializationInclusion(include);
        initDefault();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return this;
    }


    /**
     * 注册 时间类型序列化
     */
    public JacksonSerializer registerJavaTimeModule() {
        // 解决jackson无法反序列化LocalDateTime的问题
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        return this;
    }


}
