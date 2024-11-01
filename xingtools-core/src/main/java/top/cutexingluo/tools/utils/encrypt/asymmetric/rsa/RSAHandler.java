package top.cutexingluo.tools.utils.encrypt.asymmetric.rsa;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.KeyType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.encrypt.asymmetric.core.AbstractAsymmetricCipherHandler;
import top.cutexingluo.tools.utils.encrypt.base.EncType;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

/**
 * RSA 非对称加密算法
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
public class RSAHandler extends AbstractAsymmetricCipherHandler {

    @Contract(value = " -> new", pure = true)
    public static @NotNull RSAHandler newInstance() {
        return new RSAHandler();
    }

    public enum ALGO {
        /**
         * HuTool 加密，默认模式
         */
        Default,
        /**
         * security 原生加密
         */
        Security,
    }

    /**
     * 加密元数据，默认为ECB模式
     */
    @Data
    public static class Meta {
        /**
         * 密钥
         */
        KeyPair keyPair;
        /**
         * 模式
         */
        Mode mode = Mode.ECB;

        public Meta(KeyPair keyPair) {
            this.keyPair = keyPair;
        }


        /**
         * 获取加密算法名称 (PKCS1Padding)
         */
        public String getTransformation() {
            if (mode == Mode.ECB) {
                return AES_ECB_TRANSFORMATION;
            }
            return "RSA/" + mode.name() + "/PKCS1Padding";
        }

        private static final String AES_ECB_TRANSFORMATION = AsymmetricAlgorithm.RSA_ECB_PKCS1.getValue();
    }

    /**
     * 生成RSA密钥对
     *
     * @return RSA密钥对
     */
    public KeyPair generateKeyPair() {
        return SecureUtil.generateKeyPair(EncType.RSA.getName());
    }


    /**
     * 从元数据获取rsa
     */
    public cn.hutool.crypto.asymmetric.RSA getRSA(@NotNull Meta meta) {
        // 初始化对象
        // 第一个参数为加密算法，不传默认为 RSA/ECB/PKCS1Padding
        // 第二个参数为私钥（Base64字符串）
        // 第三个参数为公钥（Base64字符串）
        PrivateKey privateKey = meta.getKeyPair().getPrivate();
        PublicKey publicKey = meta.getKeyPair().getPublic();
        return new cn.hutool.crypto.asymmetric.RSA(meta.getTransformation(), privateKey, publicKey);
    }

    /**
     * 编码/加密
     *
     * @param data    数据
     * @param meta    元数据（存放公钥密钥）
     * @param keyType 加密密钥类型
     * @return {@link String}
     */
    public String encode(String data, Meta meta, KeyType keyType) {
        cn.hutool.crypto.asymmetric.RSA rsa = getRSA(meta);
        if (keyType == KeyType.PrivateKey) {
            return rsa.encryptBase64(data, KeyType.PrivateKey);
        }
        return rsa.encryptBase64(data, KeyType.PublicKey);
    }

    /**
     * 解码/解密
     *
     * @param encryptedData 加密数据
     * @param meta          梅塔
     * @param keyType       密钥类型
     * @return {@link String}
     */
    public String decode(String encryptedData, Meta meta, KeyType keyType) {
        cn.hutool.crypto.asymmetric.RSA rsa = getRSA(meta);
        if (keyType == KeyType.PublicKey) {
            return rsa.decryptStr(encryptedData, KeyType.PublicKey);
        }
        return rsa.decryptStr(encryptedData, KeyType.PrivateKey);
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
