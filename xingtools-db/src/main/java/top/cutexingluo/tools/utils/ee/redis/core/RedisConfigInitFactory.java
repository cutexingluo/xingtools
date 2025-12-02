package top.cutexingluo.tools.utils.ee.redis.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import top.cutexingluo.core.designtools.protocol.serializer.impl.json.JacksonSerializer;
import top.cutexingluo.tools.utils.ee.fastjson.FastJsonRedisSerializer;

import java.util.Objects;

/**
 * Redis 配置初始化工厂
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 10:59
 */
public class RedisConfigInitFactory {

    /**
     * 加工模板 使用Jackson
     *
     * @param template 模板
     */
    public static <T, V> void initTemplate(@NotNull RedisTemplate<T, V> template, @Nullable ObjectMapper obm) {
        //配置序列化方式
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(Object.class);

        // 1.0.5 封装进入 1.1.4 增加全局判断
        if (Objects.isNull(obm)) {
            obm = new JacksonSerializer().initRedis().getObjectMapper();
        }


        jackson2JsonRedisSerializer.setObjectMapper(obm);
        //String 的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //key 采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        //value 采用 JSON
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //hash 采用String序列化
        template.setHashKeySerializer(stringRedisSerializer);
        // hash value 采用 JSON
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
    }


    /**
     * 返回封装模板
     *
     * @param redisConnectionFactory 连接工厂
     */
    public static @NotNull
    RedisTemplate<String, Object> initTemplate(RedisConnectionFactory redisConnectionFactory, @Nullable ObjectMapper objectMapper) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        initTemplate(template, objectMapper);
        return template;
    }

    /**
     * 加工模板 使用 FastJson 序列化
     *
     * @param template 模板
     */
    @Contract("_ -> param1")
    public static <T, V> @NotNull RedisTemplate<T, V> initTemplateByFastJson(@NotNull RedisTemplate<T, V> template) {

        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 返回封装模板 FastJson 序列化
     *
     * @param redisConnectionFactory 连接工厂
     */
    public static @NotNull
    RedisTemplate<String, Object> initTemplateByFastJson(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        initTemplateByFastJson(template);
        return template;
    }
}
