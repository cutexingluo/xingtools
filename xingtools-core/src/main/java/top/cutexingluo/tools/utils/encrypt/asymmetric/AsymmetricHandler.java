package top.cutexingluo.tools.utils.encrypt.asymmetric;

import cn.hutool.crypto.asymmetric.KeyType;
import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 非对称加密处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/31 17:24
 */
public interface AsymmetricHandler {

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
    default String encodeToStringBase64(byte[] data) {
        return encoder.encodeToString(data);
    }

    /**
     * Base64解码
     */
    default byte[] decodeBase64(String data) {
        return decoder.decode(data);
    }

    // KeyPairGenerator

    /**
     * 初始化 KeyPairGenerator
     *
     * @return KeyPairGenerator
     */
    KeyPairGenerator initKeyPairGenerator() throws Exception;

    /**
     * 生成RSA密钥对
     *
     * @return RSA密钥对
     */
    default KeyPair generateKeyPairBySecurity() throws Exception {
        KeyPairGenerator keyPairGenerator = initKeyPairGenerator();
        return keyPairGenerator.generateKeyPair();
    }

    // Cipher

    /**
     * 初始化Cipher
     */
    Cipher initCipher() throws Exception;


    /**
     * 直接加密
     */
    default byte[] encodeDirect(byte[] data, Key key) throws Exception {
        Cipher cipher = initCipher();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data);
        return encryptedData;
    }

    /**
     * 直接解密
     */
    default byte[] decodeDirect(byte[] encryptedData, Key key) throws Exception {
        Cipher cipher = initCipher();
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return decryptedData;
    }

    /**
     * 使用公钥或私钥加密数据
     *
     * @param data 待加密的数据
     * @param key  公钥或者私有
     * @return 加密后的数据
     */
    default String encodeBySecurity(@NotNull String data, Key key) throws Exception {
        byte[] encryptedData = encodeDirect(data.getBytes(StandardCharsets.UTF_8), key);
        return encodeToStringBase64(encryptedData);
    }


    /**
     * 使用私钥或公钥解密数据
     *
     * @param encryptedData 加密后的数据
     * @param key           私钥或者公钥
     * @return 解密后的数据
     */
    default String decodeBySecurity(String encryptedData, Key key) throws Exception {
        byte[] decodedData = decodeBase64(encryptedData);
        byte[] decryptedData = decodeDirect(decodedData, key);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }


    // Key

    /**
     * 按Base64编码密钥
     *
     * @param keyPair 密钥对
     * @param keyType 密钥类型
     */
    default String getEncodeKeyByBase64(KeyPair keyPair, KeyType keyType) {
        if (keyType == KeyType.PrivateKey) {
            return encodeToStringBase64(keyPair.getPrivate().getEncoded());
        } else {
            return encodeToStringBase64(keyPair.getPublic().getEncoded());
        }
    }

    /**
     * 使用Base64获取解码密钥
     *
     * @param base64String Base64字符串
     * @param keyType      密钥类型
     */
    default <K extends Key> K getDecodeKeyByBase64(String base64String, KeyType keyType, KeyFactory keyFactory) throws InvalidKeySpecException {
        if (keyType == KeyType.PublicKey) {
            byte[] keyBytes = decodeBase64(base64String);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            PublicKey publicKey;
            try {
                publicKey = keyFactory.generatePublic(keySpec);
            } catch (InvalidKeySpecException e) {
                throw e;
            }
            return (K) publicKey;
        } else if (keyType == KeyType.PrivateKey) {
            byte[] keyBytes = decodeBase64(base64String);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey privateKey;
            try {
                privateKey = keyFactory.generatePrivate(keySpec);
            } catch (InvalidKeySpecException e) {
                throw e;
            }
            return (K) privateKey;
        }
        return null;
    }


    /**
     * 使用Base64获取解码密钥
     *
     * @param base64String Base64字符串
     * @param keyType      密钥类型
     */
    default <K extends Key> K getDecodeKeyByBase64(String base64String, KeyType keyType) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取PrivateKey
     */
    default PrivateKey getPrivateKeyByBase64(String base64String) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        return getDecodeKeyByBase64(base64String, KeyType.PrivateKey);
    }

    /**
     * 获取PublicKey
     */
    default PublicKey getPublicKeyByBase64(String base64String) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        return getDecodeKeyByBase64(base64String, KeyType.PublicKey);
    }


}
