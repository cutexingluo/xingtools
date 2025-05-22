package top.cutexingluo.tools.designtools.protocol.jackson.adapter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.cutexingluo.tools.designtools.protocol.adapter.impl.JacksonAdapter;

import java.util.Objects;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/14 12:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class JacksonDefaultAdapter extends JacksonAdapter {

    public JacksonDefaultAdapter() {
    }

    public JacksonDefaultAdapter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public JacksonDefaultAdapter setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    @Override
    public JacksonDefaultAdapter initSelf() {
        Objects.requireNonNull(mapper);
        this.initConfigure().initDeserializers().initSerializers().initModule();
        return this;
    }

    @Override
    public JacksonDefaultAdapter initModule() {
        // 时间转化
//        mapper.registerModule(new JavaTimeModule());
        return this;
    }

    @Override
    public JacksonDefaultAdapter initSerializers() {
        return this;
    }

    @Override
    public JacksonDefaultAdapter initDeserializers() {
        return this;
    }

    @Override
    public JacksonDefaultAdapter initConfigure() {
        // 允许出现未知字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许空对象
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        @Deprecated
//                mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        // 时间格式化 @Deprecated
//        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 禁用时间戳
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return this;
    }
}
