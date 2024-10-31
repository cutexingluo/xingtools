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
 * 普通 EC 加密工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/31 16:44
 */
public class ECHandler implements AsymmetricHandler {

    /**
     * 标准曲线名称
     */
    public static final String STD_CURVE_NAME = "secp256r1";

    /**
     * 标准转换
     */
    public static final String TRANSFORMATION = "ECIES";


    @Override
    public KeyPairGenerator initKeyPairGenerator() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        // 初始化密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(EncType.EC.getName());
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(STD_CURVE_NAME);
        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        return keyPairGenerator;
    }

    @Override
    public Cipher initCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance(TRANSFORMATION);
    }

    @Override
    public byte[] encodeDirect(byte[] data, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = initCipher();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data);
        return encryptedData;
    }

    @Override
    public byte[] decodeDirect(byte[] encryptedData, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
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
    public <K extends Key> K getDecodeKeyByBase64(String base64String, KeyType keyType) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory factory = KeyFactory.getInstance(EncType.EC.getName());
        return getDecodeKeyByBase64(base64String, keyType, factory);
    }
}
