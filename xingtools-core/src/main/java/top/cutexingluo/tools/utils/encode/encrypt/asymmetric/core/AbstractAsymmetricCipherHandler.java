package top.cutexingluo.tools.utils.encode.encrypt.asymmetric.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.cutexingluo.tools.utils.encode.core.CipherExtHandler;

import javax.crypto.Cipher;

/**
 * 非对称算法 加解密处理器 抽象类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/1 11:02
 * @since 1.1.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractAsymmetricCipherHandler<IKey> extends AbstractAsymmetricHandler implements CipherExtHandler<IKey> {

    /**
     * 密钥加解密器
     */
    protected Cipher cipher;

}
