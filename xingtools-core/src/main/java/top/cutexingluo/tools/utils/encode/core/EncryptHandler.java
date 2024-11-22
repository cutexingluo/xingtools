package top.cutexingluo.tools.utils.encode.core;

import top.cutexingluo.tools.utils.encode.base.KeyType;

import java.security.Key;


/**
 * 加密算法处理器
 *
 * <p>Encryption Algorithm 加密算法处理器</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 10:15
 * @since 1.1.6
 */
public interface EncryptHandler<IKey> extends CryptHandler {

    // Key

    /**
     * 转密钥为字符串
     *
     * @param key     密钥接口
     * @param keyType 密钥类型
     */
    byte[] getEncodeKey(IKey key, KeyType keyType);

    /**
     * 转密钥为 Key
     *
     * @param key     密钥
     * @param keyType 密钥类型
     */
    <K extends Key> K getDecodeKey(byte[] key, KeyType keyType) throws Exception;

    /**
     * 按Base64 编码密钥
     *
     * @param key     密钥接口
     * @param keyType 密钥类型
     * @return base64 编码的密钥
     */
    default String getEncodeKeyByBase64(IKey key, KeyType keyType) {
        return encodeBase64String(getEncodeKey(key, keyType));
    }

    /**
     * 按Base64 解码密钥
     *
     * @param base64Key base64 编码的密钥
     * @param keyType   密钥类型
     */
    default <K extends Key> K getDecodeKeyByBase64(String base64Key, KeyType keyType) throws Exception {
        return getDecodeKey(decodeBase64String(base64Key), keyType);
    }
}
