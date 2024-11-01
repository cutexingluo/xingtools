package top.cutexingluo.tools.utils.encrypt.asymmetric.core;

import cn.hutool.crypto.asymmetric.KeyType;
import org.jetbrains.annotations.NotNull;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 非对称算法 公私钥处理器
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
    KeyPair generateKeyPairBySecurity() throws Exception;

    /**
     * 生成RSA密钥对
     *
     * @return RSA密钥对
     */
    default KeyPair generateKeyPairBySecurityWithInit() throws Exception {
        KeyPairGenerator keyPairGenerator = initKeyPairGenerator();
        return keyPairGenerator.generateKeyPair();
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
     * 初始化 KeyFactory
     *
     * @return KeyFactory
     */
    KeyFactory initKeyFactory() throws Exception;

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
