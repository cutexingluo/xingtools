package top.cutexingluo.tools.utils.encrypt.asymmetric.ec;

import cn.hutool.crypto.asymmetric.KeyType;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.encrypt.asymmetric.AsymmetricHandler;
import top.cutexingluo.tools.utils.encrypt.base.EncType;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;

/**
 * ECC 非对称加密算法
 * <p>椭圆曲线密码算法（Elliptic Curve Cryptography，ECC）是一种基于椭圆曲线数学原理的加解密算法，具有较高的安全性和较小的计算量。在Java中，我们可以使用Bouncy Castle库来实现ECC加密算法。</p>
 * <p>需导入 org.bouncycastle:bcprov-jdk15on </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/31 16:44
 * @since 1.1.6
 */
public class ECCHandler implements AsymmetricHandler {


    /**
     * 标准曲线名称
     */
    public static final String STD_CURVE_NAME = "secp256r1";

    /**
     * 标准转换
     */
    public static final String TRANSFORMATION = "ECIES";

    /**
     * 初始化
     * <p>使用前 必须调用</p>
     */
    public static void init() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    @Override
    public KeyPairGenerator initKeyPairGenerator() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException {
        // 初始化密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(EncType.EC.getName(), EncType.BC.getName());
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(STD_CURVE_NAME);
        keyPairGenerator.initialize(ecSpec);
        return keyPairGenerator;
    }

    @Override
    public Cipher initCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        return Cipher.getInstance(TRANSFORMATION, EncType.BC.getName());
    }

    @Override
    public byte[] encodeDirect(byte[] data, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchProviderException {
        Cipher cipher = initCipher();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data);
        return encryptedData;
    }

    @Override
    public byte[] decodeDirect(byte[] encryptedData, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchProviderException {
        Cipher cipher = initCipher();
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return decryptedData;
    }

    @Override
    public String encodeBySecurity(@NotNull String data, Key key) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, NoSuchProviderException {
        byte[] encryptedData = encodeDirect(data.getBytes(StandardCharsets.UTF_8), key);
        return encodeToStringBase64(encryptedData);
    }

    @Override
    public String decodeBySecurity(String encryptedData, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchProviderException {
        byte[] decodedData = decodeBase64(encryptedData);
        byte[] decryptedData = decodeDirect(decodedData, key);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    @Override
    public <K extends Key> K getDecodeKeyByBase64(String base64String, KeyType keyType) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        KeyFactory factory = KeyFactory.getInstance(EncType.EC.getName(), EncType.BC.getName());
        return getDecodeKeyByBase64(base64String, keyType, factory);
    }
}
