package top.cutexingluo.tools.utils.encode.encrypt.asymmetric.core;


import lombok.Data;
import top.cutexingluo.tools.utils.encode.base.KeyType;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;

/**
 * 非对称算法 公私钥处理器 抽象类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/1 11:04
 * @since 1.1.6
 */
@Data
public abstract class AbstractAsymmetricHandler implements AsymmetricHandler {

    /**
     * 密钥对生成器
     */
    protected KeyPairGenerator keyPairGenerator;
    /**
     * 密钥工厂
     */
    protected KeyFactory keyFactory;


    protected void checkKeyPairGenerator() {
        Objects.requireNonNull(keyPairGenerator,
                "keyPairGenerator should not be null, please call initKeyPairGenerator() first or set it directly");
    }

    protected void checkKeyFactory() {
        Objects.requireNonNull(keyFactory,
                "keyFactory should not be null, please call initKeyFactory() first or set it directly");
    }

    @Override
    public KeyPair generateKeyPairBySecurity() {
        checkKeyPairGenerator();
        return this.keyPairGenerator.generateKeyPair();
    }

    @Override
    public <K extends Key> K getDecodeKey(byte[] key, KeyType keyType) throws InvalidKeySpecException {
        checkKeyFactory();
        KeyFactory factory = this.keyFactory;
        return getDecodeKey(key, keyType, factory);
    }
}
