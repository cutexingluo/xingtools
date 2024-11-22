package top.cutexingluo.tools.utils.encode.encrypt.asymmetric.rsa;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.encode.base.EncType;
import top.cutexingluo.tools.utils.encode.encrypt.core.EncMeta;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 15:05
 */
public class RSAHuHandler {


    public static String TRANSFORMATION_PADDING = AsymmetricAlgorithm.RSA_ECB_PKCS1.getValue();


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
    public RSA createRSA(@NotNull KeyPair keyPair, @NotNull EncMeta meta) {
        // 初始化对象
        // 第一个参数为加密算法，不传默认为 RSAOrigin/ECB/PKCS1Padding
        // 第二个参数为私钥（Base64字符串）
        // 第三个参数为公钥（Base64字符串）
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        return new RSA(meta.getTransformation(EncType.RSA.getName(), TRANSFORMATION_PADDING), privateKey, publicKey);
    }

    /**
     * 编码/加密
     *
     * @param data    数据
     * @param rsa     RSA 对象
     * @param keyType 加密密钥类型
     * @return {@link String}
     */
    public String encode(String data, RSA rsa, KeyType keyType) {
        if (keyType == KeyType.PrivateKey) {
            return rsa.encryptBase64(data, KeyType.PrivateKey);
        }
        return rsa.encryptBase64(data, KeyType.PublicKey);
    }

    /**
     * 解码/解密
     *
     * @param encryptedData 加密数据
     * @param rsa           RSA 对象
     * @param keyType       密钥类型
     * @return {@link String}
     */
    public String decode(String encryptedData, RSA rsa, KeyType keyType) {
        if (keyType == KeyType.PublicKey) {
            return rsa.decryptStr(encryptedData, KeyType.PublicKey);
        }
        return rsa.decryptStr(encryptedData, KeyType.PrivateKey);
    }
}
