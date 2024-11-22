package top.cutexingluo.tools.utils.encode.encrypt.symmetric.core;

import lombok.Data;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Objects;

/**
 * 对称算法 公私钥处理器 抽象类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 11:28
 * @since 1.1.6
 */
@Data
public abstract class AbstractSymmetricHandler implements SymmetricHandler {


    /**
     * 密钥生成器
     */
    protected KeyGenerator keyGenerator;

    protected void checkKeyGenerator() {
        Objects.requireNonNull(keyGenerator,
                "keyGenerator should not be null, please call initKeyGenerator() first or set it directly");
    }

    @Override
    public SecretKey generateKeyBySecurity() {
        checkKeyGenerator();
        return keyGenerator.generateKey();
    }


}
