package top.cutexingluo.tools.utils.encode.core;

import org.jetbrains.annotations.NotNull;

import java.util.Base64;

/**
 * 密码学处理器
 *
 * <p>Cryptography 密码学算法</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 10:10
 * @since 1.1.6
 */
public interface CryptHandler {

    // Base64

    /**
     * 全局 encoder
     */
    @NotNull
    Base64.Encoder encoder = Base64.getEncoder();
    /**
     * 全局 decoder
     */
    @NotNull
    Base64.Decoder decoder = Base64.getDecoder();

    /**
     * Base64编码
     */
    default String encodeBase64String(byte[] data) {
        return encoder.encodeToString(data);
    }

    /**
     * Base64编码
     */
    default byte[] encodeBase64(byte[] data) {
        return encoder.encode(data);
    }

    /**
     * Base64解码
     */
    default byte[] decodeBase64String(String data) {
        return decoder.decode(data);
    }

    /**
     * Base64解码
     */
    default byte[] decodeBase64(byte[] data) {
        return decoder.decode(data);
    }

}
