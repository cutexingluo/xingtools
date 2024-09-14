package top.cutexingluo.tools.designtools.protocol.jackson.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * 对象转字符串反序列化器
 * <p>使用方式: </p>
 * <pre>
 *     1. 在字段上使用注解 @JsonDeserialize(using = JsonStringDeserializer.class)
 *     2.  或者向 ObjectMapper 添加自定义反序列化器
 *          SimpleModule module = new SimpleModule();
 *         module.addDeserializer(String.class, new JsonStringDeserializer());
 *         objectMapper.registerModule(module);
 * </pre>
 *
 * <p>如果目标是字符串, 则返回原值</p>
 * <p>如果目标是对象, 则返回对象的 JSON 字符串 作为值返回字符串</p>
 * <p>目的为了 Jackson 拥有 FastJson 的对象转字符串的能力</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/14 10:55
 * @since 1.1.4
 */
public class JsonStringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        if (JsonToken.VALUE_STRING == p.currentToken()) { // 如果是字符串直接返回
            return p.getValueAsString();
        } else {
            ObjectCodec codec = p.getCodec();
            // 读取当前JSON节点为树模型
            JsonNode node = codec.readTree(p); // 不是 json 抛出异常

            if (codec instanceof ObjectMapper) {
                ObjectMapper mapper = (ObjectMapper) codec;
                if (node.isTextual()) {
                    return node.textValue();
                } else {
                    // 将树模型转换回JSON字符串
                    return mapper.writeValueAsString(node);
                }
            }
            return node.toString();
        }
    }
}
