package top.cutexingluo.tools.designtools.protocol.serializer.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import top.cutexingluo.tools.designtools.protocol.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Kryo 序列化器
 * <p>建议设置为全局唯一</p>
 *
 * <p>需要导入 com.esotericsoftware:kryo 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/25 20:04
 * @since 1.0.5
 */
public class KryoSerializer implements Serializer {

    /**
     * kryo 线程不安全，使用 ThreadLocal 保证每个线程只有一个 Kryo
     */
    protected final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 设置动态动态序列化和反序列化类，不提前注册所有类（可能有安全问题）
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            KRYO_THREAD_LOCAL.get().writeObject(output, obj);
            return byteArrayOutputStream.toByteArray();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> classType) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            T result = KRYO_THREAD_LOCAL.get().readObject(input, classType);
            input.close();
            return result;
        }
    }
}
