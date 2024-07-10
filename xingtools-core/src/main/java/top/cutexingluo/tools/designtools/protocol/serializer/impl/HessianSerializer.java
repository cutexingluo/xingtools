package top.cutexingluo.tools.designtools.protocol.serializer.impl;


import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.HessianSerializerInput;
import top.cutexingluo.tools.designtools.protocol.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Hessian 序列化器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/25 14:36
 * @since 1.0.5
 */
public class HessianSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        Objects.requireNonNull(obj);
        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            HessianOutput ho = new HessianOutput(bos);
            ho.writeObject(obj);
            bytes = bos.toByteArray();
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        Objects.requireNonNull(data);
        T ret;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            HessianSerializerInput hi = new HessianSerializerInput(bis);
            ret = (T) hi.readObject(clz);
        }
        return ret;
    }
}
