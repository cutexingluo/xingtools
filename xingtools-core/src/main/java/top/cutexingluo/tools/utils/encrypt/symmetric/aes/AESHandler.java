package top.cutexingluo.tools.utils.encrypt.symmetric.aes;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.utils.encrypt.base.EncType;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * AES 对称加密算法
 *
 * <p>AES支持128位、192位和256位三种密钥长度，分组长度固定为128位</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 10:16
 * @see top.cutexingluo.tools.utils.encrypt.symmetric.des.DESHandler
 * @since 1.1.6
 */
public class AESHandler {

    @Contract(value = " -> new", pure = true)
    public static @NotNull AESHandler newInstance() {
        return new AESHandler();
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
     * 加密元数据，默认为ECB模式，不设置iv
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private Mode mode = Mode.ECB;
        /**
         * 密钥为16位
         */
        private String key;
        /**
         * 向量为16位
         */
        private String iv;

        public String getTransformation() {
            if (mode == Mode.CBC) {
                return AES_CBC_TRANSFORMATION;
            } else if (mode == Mode.ECB) {
                return AES_ECB_TRANSFORMATION;
            }
            return "AES/" + mode.name() + "/PKCS5Padding";
        }

        // AES加密模式为CBC，填充方式为PKCS5Padding
        private static final String AES_CBC_TRANSFORMATION = "AES/CBC/PKCS5Padding";
        private static final String AES_ECB_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    }

    /**
     * AES加密
     *
     * @param data 待加密的数据
     * @param key  密钥，长度必须为8位
     * @param meta 元数据，可以设置模式
     * @return 加密后的数据，使用Base64编码
     */
    public String encode(String data, String key, @Nullable Meta meta) {
        if (meta == null) meta = new Meta();
        AES aes = new AES(meta.getMode(), Padding.PKCS5Padding, key.getBytes(), meta.getIv().getBytes());
        return aes.encryptBase64(data);
    }

    /**
     * AES解密
     *
     * @param encryptedData 加密后的数据，使用Base64编码
     * @param key           密钥，长度必须为8位
     * @param meta          元数据，可以设置模式
     * @return 解密后的数据
     */
    public String decode(String encryptedData, String key, @Nullable Meta meta) {
        if (meta == null) meta = new Meta();
        AES aes = new AES(meta.getMode(), Padding.PKCS5Padding, key.getBytes(), meta.getIv().getBytes());
        return aes.decryptStr(encryptedData);
    }

    /**
     * AES加密
     *
     * @param data 待加密的数据
     * @return 加密后的数据，使用Base64编码
     */
    public String encodeBySecurity(@NotNull String data, @NotNull Meta meta) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        // 将AES密钥转换为SecretKeySpec对象
        SecretKeySpec secretKeySpec = new SecretKeySpec(meta.getKey().getBytes(), EncType.AES.getName());
        // 将AES初始化向量转换为IvParameterSpec对象
        IvParameterSpec ivParameterSpec = new IvParameterSpec(meta.getIv().getBytes());
        // 根据加密算法获取加密器
        Cipher cipher = Cipher.getInstance(meta.getTransformation());
        // 初始化加密器，设置加密模式、密钥和初始化向量
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        // 加密数据
        byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        // 对加密后的数据使用Base64编码
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * AES解密
     *
     * @param encryptedData 加密后的数据，使用Base64编码
     * @param meta          梅塔
     * @return 解密后的数据
     */
    public String decodeBySecurity(String encryptedData, @NotNull Meta meta) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        // 将AES密钥转换为SecretKeySpec对象
        SecretKeySpec secretKeySpec = new SecretKeySpec(meta.getKey().getBytes(), EncType.AES.getName());
        // 将AES初始化向量转换为IvParameterSpec对象
        IvParameterSpec ivParameterSpec = new IvParameterSpec(meta.getKey().getBytes());
        // 根据加密算法获取解密器
        Cipher cipher = Cipher.getInstance(meta.getTransformation());
        // 初始化解密器，设置解密模式、密钥和初始化向量
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        // 对加密后的数据使用Base64解码
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        // 解密数据
        byte[] decryptedData = cipher.doFinal(decodedData);
        // 返回解密后的数据
        return new String(decryptedData, StandardCharsets.UTF_8);
    }
}
