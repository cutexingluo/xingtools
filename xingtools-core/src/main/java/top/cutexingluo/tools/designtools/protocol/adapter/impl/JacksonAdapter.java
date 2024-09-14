package top.cutexingluo.tools.designtools.protocol.adapter.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import top.cutexingluo.tools.designtools.protocol.adapter.ModuleAdapter;

/**
 * Jackson 适配器
 *
 * <p>继承该类实现方法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/14 13:55
 */
public class JacksonAdapter implements ModuleAdapter<ObjectMapper> {

    protected ObjectMapper mapper;

    public JacksonAdapter() {
    }

    public JacksonAdapter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public JacksonAdapter setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
