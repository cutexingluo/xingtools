package top.cutexingluo.tools.designtools.protocol.jackson.adapter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.cutexingluo.tools.designtools.protocol.adapter.impl.JacksonAdapter;

import java.util.Objects;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/14 12:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class JacksonRedisAdapter extends JacksonAdapter {

    public JacksonRedisAdapter() {
    }

    public JacksonRedisAdapter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public JacksonRedisAdapter setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    @Override
    public JacksonRedisAdapter initSelf() {
        Objects.requireNonNull(mapper);
        this.initConfigure().initModule();
        return this;
    }

    @Override
    public JacksonRedisAdapter initModule() {
        // 时间转化
        mapper.registerModule(new JavaTimeModule());
        return this;
    }

    @Override
    public JacksonRedisAdapter initSerializers() {
        return this;
    }

    @Override
    public JacksonRedisAdapter initDeserializers() {
        return this;
    }

    @Override
    public JacksonRedisAdapter initConfigure() {
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会抛出异常
//        obm.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        PolymorphicTypeValidator typeValidator = mapper.getPolymorphicTypeValidator();
        mapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // 禁用时间戳
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return this;
    }
}
