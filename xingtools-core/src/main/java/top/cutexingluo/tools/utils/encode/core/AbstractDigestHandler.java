package top.cutexingluo.tools.utils.encode.core;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

/**
 * 摘要算法处理器 抽象类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 10:48
 * @since 1.1.6
 */
@Data
public abstract class AbstractDigestHandler implements DigestHandler {

    /**
     * 摘要算法处理器
     */
    protected MessageDigest messageDigest;


    @Override
    public byte[] encodeBySecurity(byte[] data) {
        return messageDigest.digest(data);
    }

    @Override
    public byte[] encodeBySecurity(@NotNull String data) {
        return encodeBySecurity(data.getBytes());
    }

    @Override
    public boolean validateBySecurity(byte[] data, CharSequence encoded) {
        byte[] bytes = encodeBySecurity(data);
        return new String(bytes).contentEquals(encoded);
    }

    @Override
    public boolean validateBySecurity(@NotNull String data, CharSequence encoded) {
        return validateBySecurity(data.getBytes(), encoded);
    }
}
