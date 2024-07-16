package top.cutexingluo.tools.utils.ee.redis.core;

/**
 * Redis 序列化方式
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/29 21:40
 */
public enum RedisSerializerEnum {
    /**
     * Jackson Serializer
     */
    jackson,
    /**
     * ali Fastjson Serializer
     */
    fastjson
}
