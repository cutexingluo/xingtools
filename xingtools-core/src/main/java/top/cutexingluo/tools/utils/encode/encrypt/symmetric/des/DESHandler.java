package top.cutexingluo.tools.utils.encode.encrypt.symmetric.des;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.utils.encode.base.EncType;
import top.cutexingluo.tools.utils.encode.base.KeyType;
import top.cutexingluo.tools.utils.encode.core.CipherKeyHandler;
import top.cutexingluo.tools.utils.encode.encrypt.core.EncMeta;
import top.cutexingluo.tools.utils.encode.encrypt.symmetric.aes.AESHandler;
import top.cutexingluo.tools.utils.encode.encrypt.symmetric.core.AbstractSymmetricCipherHandler;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * DES  对称加密算法
 *
 * <p>DES算法以64位为分组长度，但实际密钥长度为56位（因为其中有8位是奇偶校验位，不参与DES运算）</p>
 * <p>不安全，建议使用AES</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 10:11
 * @see AESHandler
 * @since 1.1.6
 */
public class DESHandler extends AbstractSymmetricCipherHandler<Key> implements CipherKeyHandler {

    @NotNull
    @Contract("_ -> new")
    public static DESHandler newInstance(@Nullable EncMeta meta) {
        return meta == null ? new DESHandler() : new DESHandler(meta);
    }

    /**
     * 加密算法的元数据
     */
    protected EncMeta meta;

    public DESHandler() {
        this.meta = new EncMeta();
    }

    public DESHandler(@NotNull EncMeta meta) {
        this.meta = meta;
    }

    @Override
    public KeyGenerator initKeyGenerator() throws NoSuchAlgorithmException {
        this.keyGenerator = KeyGenerator.getInstance(EncType.DES.getName());
        return keyGenerator;
    }


    @Override
    public Cipher initCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance(meta.getTransformation(EncType.DES.getName()));
        return cipher;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SecretKeySpec getDecodeKey(byte[] key, KeyType keyType) {
        return new SecretKeySpec(key, EncType.DES.getName());
    }


}
