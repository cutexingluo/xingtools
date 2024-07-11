package top.cutexingluo.tools.designtools.json.serializer;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.springframework.core.annotation.AnnotationUtils;
import top.cutexingluo.tools.designtools.method.ClassMaker;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * 序列化注解自定义实现
 * JsonSerializer<String>：指定String 类型，serialize()方法用于将修改后的数据载入
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/1 14:25
 * @since 1.0.4
 */
public class StringJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {

    protected StrJsonStrategy strategy;
    protected StrJson strJson;

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (Objects.nonNull(strJson)) {
            if (strJson.filterExists() && s == null) {
                jsonGenerator.writeString((String) null);
            } else {
                jsonGenerator.writeString(strategy.toJsonStr(s));
            }
        } else {
            jsonGenerator.writeString(s);
        }
    }

    /**
     * 获取属性上的注解属性
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        StrJson annotation = beanProperty.getAnnotation(StrJson.class);
        strJson = AnnotationUtils.getAnnotation(annotation, StrJson.class);
        if (Objects.nonNull(strJson) && Objects.equals(String.class, beanProperty.getType().getRawClass())) {
            ClassMaker<?> classMaker = new ClassMaker<>(strJson.value());
            StrJsonStrategy strategy = null;
            if (StrUtil.isBlank(strJson.name())) {
                strategy = (StrJsonStrategy) classMaker.newInstanceNoExc(); // new instance
                if (Objects.isNull(strategy)) {
                    throw JsonMappingException.from(serializerProvider, "The class must implement the StrJsonStrategy class and must provide either a no-parameter or a constructor of a String type parameter.  now name is '', you need  a no-parameter " +
                            "  该类必须为 StrJsonStrategy 实现类，并且必须提供无参或者一个String类型参数的构造方法。现在需要无参构造");
                }
            } else {
                Constructor<?> constructor = classMaker.getConstructor(String.class);
                try {
                    strategy = (StrJsonStrategy) constructor.newInstance(strJson.name());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw JsonMappingException.from(serializerProvider, "The class must implement the StrJsonStrategy class and must provide either a no-parameter or a constructor of a String type parameter. now name is not '', you need   a String type parameter " +
                            "该类必须为 StrJsonStrategy 实现类，并且必须提供无参或者一个String类型参数的构造方法。现在需要String有参构造");
                }
            }
            this.strategy = strategy;
            return this;
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
