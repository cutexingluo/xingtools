package top.cutexingluo.tools.utils.encode.encrypt.symmetric.core;

import top.cutexingluo.tools.utils.encode.base.KeyType;
import top.cutexingluo.tools.utils.encode.core.EncryptHandler;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 对称算法 公私钥处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 11:07
 * @since 1.1.6
 */
public interface SymmetricHandler extends EncryptHandler<SecretKey> {

    // KeyPairGenerator

    /**
     * 初始化 KeyPairGenerator
     */
    KeyGenerator initKeyGenerator() throws Exception;

    /**
     * 生成 密钥对
     *
     * @return 密钥对
     */
    SecretKey generateKeyBySecurity() throws Exception;

    /**
     * 生成 密钥对
     * <p>自动初始化</p>
     *
     * @return 密钥对
     */
    default SecretKey generateKeyPairBySecurityWithInit() throws Exception {
        KeyGenerator keyGenerator = initKeyGenerator();
        return keyGenerator.generateKey();
    }

    // Key
    @Override
    default byte[] getEncodeKey(SecretKey secretKey, KeyType keyType) {
        return secretKey.getEncoded();
    }

}
