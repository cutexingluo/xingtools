package top.cutexingluo.tools.designtools.protocol.serializer;

/**
 * 序列化异常
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/25 14:46
 * @since 1.0.5
 */
public class SerializationException extends RuntimeException {

    public SerializationException() {
        super();
    }

    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}
