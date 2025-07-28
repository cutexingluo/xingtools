package top.cutexingluo.tools.designtools.protocol.jackson.adapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.cutexingluo.tools.designtools.protocol.adapter.impl.JacksonAdapter;
import top.cutexingluo.tools.designtools.protocol.jackson.deserializer.JsonStringDeserializer;

import java.util.Objects;

/**
 * JacksonToFastJsonModule
 * <p>jackson 对 fastjson 适配器</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/14 11:32
 * @since 1.1.4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class JacksonToFastJsonAdapter extends JacksonAdapter {


    public JacksonToFastJsonAdapter() {
    }

    public JacksonToFastJsonAdapter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public JacksonToFastJsonAdapter setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    /**
     * 初始化自身
     * <p>初始化所有配置</p>
     */
    @Override
    public JacksonToFastJsonAdapter initSelf() {
        Objects.requireNonNull(mapper);
        this.initConfigure().initDeserializers().initSerializers().initModule();
        return this;
    }

    @Override
    public JacksonToFastJsonAdapter initModule() {
        // 时间转化
//        mapper.registerModule(new JavaTimeModule());
        return this;
    }

    @Override
    public JacksonToFastJsonAdapter initDeserializers() {
        SimpleModule module = new SimpleModule();
        // 允许对象转字符串
        module.addDeserializer(String.class, new JsonStringDeserializer());
        mapper.registerModule(module);
        return this;
    }

    @Override
    public JacksonToFastJsonAdapter initSerializers() {
        return this;
    }

    @Override
    public JacksonToFastJsonAdapter initConfigure() {
        // 序列化非空字段
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 允许未知字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许空对象
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 允许单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 允许无引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 禁用时间戳
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return this;
    }
}
