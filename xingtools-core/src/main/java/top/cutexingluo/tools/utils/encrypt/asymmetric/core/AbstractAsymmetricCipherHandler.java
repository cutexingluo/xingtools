package top.cutexingluo.tools.utils.encrypt.asymmetric.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchProviderException;
import java.util.Objects;

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
public abstract class AbstractAsymmetricCipherHandler extends AbstractAsymmetricHandler implements AsymmetricCipherHandler {

    /**
     * 密钥加解密器
     */
    protected Cipher cipher;

    protected void checkCipher() {
        Objects.requireNonNull(cipher,
                "cipher should not be null, please call initCipher() first or set it directly");
    }

    @Override
    public byte[] encodeDirect(byte[] data, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        checkCipher();
        Cipher cipher = this.cipher;
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data);
        return encryptedData;
    }

    @Override
    public byte[] decodeDirect(byte[] encryptedData, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        checkCipher();
        Cipher cipher = this.cipher;
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return decryptedData;
    }

    @Override
    public String encodeBySecurity(@NotNull String data, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchProviderException {
        byte[] encryptedData = encodeDirect(data.getBytes(), key);
        return encodeToStringBase64(encryptedData);
    }

    @Override
    public String decodeBySecurity(String encryptedData, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchProviderException {
        byte[] decodedData = decodeBase64(encryptedData);
        byte[] decryptedData = decodeDirect(decodedData, key);
        return new String(decryptedData);
    }
}
