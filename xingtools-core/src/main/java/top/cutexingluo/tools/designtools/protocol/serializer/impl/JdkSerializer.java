package top.cutexingluo.tools.designtools.protocol.serializer.impl;


import top.cutexingluo.tools.designtools.protocol.serializer.SerializationException;
import top.cutexingluo.tools.designtools.protocol.serializer.Serializer;

import java.io.*;

/**
 * JDK 序列化器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/25 14:26
 * @since 1.0.5
 */
public class JdkSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        byte[] bytes;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();
            bytes = outputStream.toByteArray();
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new SerializationException(e);
        }
    }

    public <T> T deserialize(byte[] data) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new SerializationException(e);
        }
    }
}
