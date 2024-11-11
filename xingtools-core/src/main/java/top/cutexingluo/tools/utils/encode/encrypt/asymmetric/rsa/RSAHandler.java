package top.cutexingluo.tools.utils.encode.encrypt.asymmetric.rsa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.encode.base.EncType;
import top.cutexingluo.tools.utils.encode.core.CipherKeyHandler;
import top.cutexingluo.tools.utils.encode.encrypt.asymmetric.core.AbstractAsymmetricCipherHandler;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * RSAOrigin 非对称加密算法
 *
 * <p>RSA算法基于大数分解问题的难解性，使用公钥进行加密，私钥进行解密。公钥和私钥是一对数学上相关的密钥</p>
 * <p>RSA密钥长度通常较长，常见的有1024位、2048位等，以确保安全性</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 10:32
 * @since 1.1.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RSAHandler extends AbstractAsymmetricCipherHandler<Key> implements CipherKeyHandler {

    @Contract(value = " -> new", pure = true)
    public static @NotNull
    RSAHandler newInstance() {
        return new RSAHandler();
    }


    //------interface------

    @Override
    public KeyPairGenerator initKeyPairGenerator() throws NoSuchAlgorithmException {
        keyPairGenerator = KeyPairGenerator.getInstance(EncType.RSA.getName());
        keyPairGenerator.initialize(2048); // 密钥大小为2048位
        return keyPairGenerator;
    }

    @Override
    public KeyFactory initKeyFactory() throws Exception {
        this.keyFactory = KeyFactory.getInstance(EncType.RSA.getName());
        return keyFactory;
    }

    @Override
    public Cipher initCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance(EncType.RSA.getName());
        return cipher;
    }

}
