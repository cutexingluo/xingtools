package top.cutexingluo.tools.utils.encode.encrypt.symmetric.aes;

import top.cutexingluo.tools.utils.encode.base.EncType;
import top.cutexingluo.tools.utils.encode.base.KeyType;
import top.cutexingluo.tools.utils.encode.core.CipherKeyHandler;
import top.cutexingluo.tools.utils.encode.encrypt.core.EncMeta;
import top.cutexingluo.tools.utils.encode.encrypt.symmetric.core.AbstractSymmetricCipherHandler;
import top.cutexingluo.tools.utils.encode.encrypt.symmetric.des.DESHandler;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * AES 对称加密算法
 *
 * <p>AES支持128位、192位和256位三种密钥长度，分组长度固定为128位</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 10:16
 * @see DESHandler
 * @since 1.1.6
 */

public class AESHandler extends AbstractSymmetricCipherHandler<Key> implements CipherKeyHandler {


    /**
     * 加密算法的元数据
     */
    protected EncMeta meta;

    public AESHandler() {
        this.meta = new EncMeta();
    }

    public AESHandler(EncMeta meta) {
        this.meta = meta;
    }

    @Override
    public KeyGenerator initKeyGenerator() throws NoSuchAlgorithmException {
        this.keyGenerator = KeyGenerator.getInstance(EncType.AES.getName());
        return keyGenerator;
    }

    @Override
    public Cipher initCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance(meta.getTransformation(EncType.AES.getName()));
        return cipher;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SecretKeySpec getDecodeKey(byte[] key, KeyType keyType) {
        // 将AES密钥转换为SecretKeySpec对象
        return new SecretKeySpec(key, EncType.AES.getName());
    }

    @Override
    public byte[] encodeDirect(byte[] data, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        if (meta.getIv() != null) {
            Cipher cipher = checkCipher();
            // 将AES初始化向量转换为IvParameterSpec对象
            IvParameterSpec ivParameterSpec = new IvParameterSpec(meta.getIv());
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            return cipher.doFinal(data);
        }
        return CipherKeyHandler.super.encodeDirect(data, key);
    }

    @Override
    public byte[] decodeDirect(byte[] encryptedData, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        if (meta.getIv() != null) {
            Cipher cipher = checkCipher();
            // 将AES初始化向量转换为IvParameterSpec对象
            IvParameterSpec ivParameterSpec = new IvParameterSpec(meta.getIv());
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            return cipher.doFinal(encryptedData);
        }
        return CipherKeyHandler.super.decodeDirect(encryptedData, key);
    }

}
