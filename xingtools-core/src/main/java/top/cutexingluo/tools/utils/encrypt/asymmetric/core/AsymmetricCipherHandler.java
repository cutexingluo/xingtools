package top.cutexingluo.tools.utils.encrypt.asymmetric.core;

import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import java.security.Key;

/**
 * 非对称算法 加解密处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/1 10:30
 * @since 1.1.6
 */
public interface AsymmetricCipherHandler extends AsymmetricHandler {

    // Cipher

    /**
     * 初始化Cipher
     */
    Cipher initCipher() throws Exception;


    /**
     * 直接加密
     *
     * @param data 待加密的数据
     * @param key  公钥或者私钥
     * @return 加密后的数据
     */
    byte[] encodeDirect(byte[] data, Key key) throws Exception;

    /**
     * 直接解密
     *
     * @param encryptedData 加密后的数据
     * @param key           私钥或者公钥
     * @return 解密后的数据
     */
    byte[] decodeDirect(byte[] encryptedData, Key key) throws Exception;

    /**
     * 使用公钥或私钥加密数据 (base64)
     *
     * @param data 待加密的数据
     * @param key  公钥或者私钥
     * @return 加密后的数据
     */
    default String encodeBySecurity(@NotNull String data, Key key) throws Exception {
        byte[] encryptedData = encodeDirect(data.getBytes(), key);
        return encodeToStringBase64(encryptedData);
    }


    /**
     * 使用私钥或公钥解密数据(base64)
     *
     * @param encryptedData 加密后的数据
     * @param key           私钥或者公钥
     * @return 解密后的数据
     */
    default String decodeBySecurity(String encryptedData, Key key) throws Exception {
        byte[] decodedData = decodeBase64(encryptedData);
        byte[] decryptedData = decodeDirect(decodedData, key);
        return new String(decryptedData);
    }

}
