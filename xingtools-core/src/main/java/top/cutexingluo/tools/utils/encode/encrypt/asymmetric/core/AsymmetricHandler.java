package top.cutexingluo.tools.utils.encode.encrypt.asymmetric.core;


import top.cutexingluo.tools.utils.encode.base.KeyType;
import top.cutexingluo.tools.utils.encode.core.EncryptHandler;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 非对称算法 公私钥处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/31 17:24
 * @since 1.1.6
 */
public interface AsymmetricHandler extends EncryptHandler<KeyPair> {


    // KeyPairGenerator

    /**
     * 初始化 KeyPairGenerator
     *
     * @return KeyPairGenerator
     */
    KeyPairGenerator initKeyPairGenerator() throws Exception;

    /**
     * 生成 密钥对
     *
     * @return 密钥对
     */
    KeyPair generateKeyPairBySecurity() throws Exception;

    /**
     * 生成 密钥对
     * <p>自动初始化</p>
     *
     * @return 密钥对
     */
    default KeyPair generateKeyPairBySecurityWithInit() throws Exception {
        KeyPairGenerator keyPairGenerator = initKeyPairGenerator();
        return keyPairGenerator.generateKeyPair();
    }


    // Key


    /**
     * 初始化 KeyFactory
     *
     * @return KeyFactory
     */
    KeyFactory initKeyFactory() throws Exception;


    /**
     * 获取解码 私钥
     *
     * @param privateKey 私钥
     */
    default PrivateKey getPrivateKey(byte[] privateKey, KeyFactory keyFactory) throws InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        return key;
    }

    /**
     * 获取解码 公钥
     *
     * @param publicKey 公钥
     */
    default PublicKey getPublicKey(byte[] publicKey, KeyFactory keyFactory) throws InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        PublicKey key = keyFactory.generatePublic(keySpec);
        return key;
    }

    /**
     * 获取解码密钥
     *
     * @param key     密钥
     * @param keyType 密钥类型
     */
    default <K extends Key> K getDecodeKey(byte[] key, KeyType keyType, KeyFactory keyFactory) throws InvalidKeySpecException {
        if (keyType == KeyType.PublicKey) {
            return (K) getPublicKey(key, keyFactory);
        } else if (keyType == KeyType.PrivateKey) {
            return (K) getPrivateKey(key, keyFactory);
        }
        return null;
    }

    /**
     * 获取密钥
     *
     * @param keyPair 密钥对
     * @param keyType 密钥类型
     */
    @Override
    default byte[] getEncodeKey(KeyPair keyPair, KeyType keyType) {
        if (keyType == KeyType.PrivateKey) {
            return keyPair.getPrivate().getEncoded();
        } else {
            return keyPair.getPublic().getEncoded();
        }
    }


    /**
     * 获取PrivateKey
     */
    default PrivateKey getPrivateKeyByBase64(String base64Key) throws Exception {
        return getDecodeKeyByBase64(base64Key, KeyType.PrivateKey);
    }

    /**
     * 获取PublicKey
     */
    default PublicKey getPublicKeyByBase64(String base64Key) throws Exception {
        return getDecodeKeyByBase64(base64Key, KeyType.PublicKey);
    }


}
