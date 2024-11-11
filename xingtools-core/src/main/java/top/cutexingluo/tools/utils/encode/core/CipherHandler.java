package top.cutexingluo.tools.utils.encode.core;

import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;

/**
 * 加解密处理器
 *
 * @param <IKey> 密钥类型, 或密钥类型的组合类
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 11:37
 * @since 1.1.6
 */
public interface CipherHandler<IKey> extends CryptHandler {

    // Cipher

    /**
     * 初始化Cipher
     */
    Cipher initCipher() throws Exception;


    /**
     * 直接加密
     *
     * @param data 待加密的数据
     * @param key  密钥
     * @return 加密后的数据
     */
    byte[] encodeDirect(byte[] data, IKey key) throws Exception;

    /**
     * 直接解密
     *
     * @param encryptedData 加密后的数据
     * @param key           密钥
     * @return 解密后的数据
     */
    byte[] decodeDirect(byte[] encryptedData, IKey key) throws Exception;

    /**
     * 使用公钥或私钥加密数据 (base64)
     *
     * @param data 待加密的数据
     * @param key  密钥
     * @return 加密后的数据
     */
    default String encodeBySecurity(@NotNull String data, IKey key) throws Exception {
        byte[] encryptedData = encodeDirect(data.getBytes(), key);
        return encodeBase64String(encryptedData);
    }


    /**
     * 使用私钥或公钥解密数据(base64)
     *
     * @param encryptedData 加密后的数据
     * @param key           密钥
     * @return 解密后的数据
     */
    default String decodeBySecurity(String encryptedData, IKey key) throws Exception {
        byte[] decodedData = decodeBase64String(encryptedData);
        byte[] decryptedData = decodeDirect(decodedData, key);
        return new String(decryptedData);
    }
}
