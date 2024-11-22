package top.cutexingluo.tools.utils.encode.core;

import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchProviderException;

/**
 * 扩展 加解密处理器 Key 实现基础接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 11:42
 * @since 1.1.6
 */
public interface CipherKeyHandler extends CipherExtHandler<Key> {


    @Override
    default byte[] encodeDirect(byte[] data, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = checkCipher();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    @Override
    default byte[] decodeDirect(byte[] encryptedData, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = checkCipher();
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedData);
    }

    @Override
    default String encodeBySecurity(@NotNull String data, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchProviderException, InvalidAlgorithmParameterException {
        byte[] encryptedData = encodeDirect(data.getBytes(), key);
        return encodeBase64String(encryptedData);
    }

    @Override
    default String decodeBySecurity(String encryptedData, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchProviderException, InvalidAlgorithmParameterException {
        byte[] decodedData = decodeBase64String(encryptedData);
        byte[] decryptedData = decodeDirect(decodedData, key);
        return new String(decryptedData);
    }
}
