package top.cutexingluo.tools.utils.encrypt.asymmetric.core;

import cn.hutool.crypto.asymmetric.KeyType;
import lombok.Data;

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
    public <K extends Key> K getDecodeKeyByBase64(String base64String, KeyType keyType) throws InvalidKeySpecException {
        checkKeyFactory();
        KeyFactory factory = this.keyFactory;
        return getDecodeKeyByBase64(base64String, keyType, factory);
    }
}
