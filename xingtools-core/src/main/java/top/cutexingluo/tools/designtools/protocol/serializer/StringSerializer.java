package top.cutexingluo.tools.designtools.protocol.serializer;

import java.io.IOException;

/**
 * String 类型序列化接口
 *
 * <p>方法名模仿 js</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/25 20:14
 * @since 1.0.5
 */
public interface StringSerializer {


    /**
     * 序列化
     */
    <T> String stringify(T obj) throws IOException;

    /**
     * 反序列化
     */
    <T> T parse(String data, Class<T> clz) throws IOException;

}
