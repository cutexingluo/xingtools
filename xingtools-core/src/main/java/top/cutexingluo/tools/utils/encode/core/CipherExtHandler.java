package top.cutexingluo.tools.utils.encode.core;

import javax.crypto.Cipher;
import java.util.Objects;

/**
 * 扩展 加解密处理器
 *
 * <p>提供实现的方法</p>
 * <p>不支持多继承，所以单独为一个接口</p>
 *
 * @param <IKey> 密钥类型, 或密钥类型的组合类
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 14:04
 * @since 1.1.6
 */
public interface CipherExtHandler<IKey> extends CipherHandler<IKey> {

    /**
     * 获取Cipher 对象
     */
    Cipher getCipher();

    default Cipher checkCipher() {
        return Objects.requireNonNull(getCipher(),
                "cipher should not be null, please call initCipher() first or set it directly");
    }
}
