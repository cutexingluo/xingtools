package top.cutexingluo.tools.utils.encode.encrypt.symmetric.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.cutexingluo.tools.utils.encode.core.CipherExtHandler;

import javax.crypto.Cipher;

/**
 * 对称算法 加解密处理器 抽象类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 11:35
 * @since 1.1.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractSymmetricCipherHandler<IKey> extends AbstractSymmetricHandler implements CipherExtHandler<IKey> {

    /**
     * 密钥加解密器
     */
    protected Cipher cipher;
}
