package top.cutexingluo.tools.utils.encrypt.symmetric.des;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.utils.encrypt.base.EncType;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * DES  对称加密算法
 *
 * <p>DES算法以64位为分组长度，但实际密钥长度为56位（因为其中有8位是奇偶校验位，不参与DES运算）</p>
 * <p>不安全，建议使用AES</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 10:11
 * @see top.cutexingluo.tools.utils.encrypt.symmetric.aes.AESHandler
 * @since 1.1.6
 */
public class DESHandler {

    public static DESHandler newInstance() {
        return new DESHandler();
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
        /**
         * 模式
         *
         * @see cn.hutool.crypto.Mode
         */
        private Mode mode = Mode.ECB;
        /**
         * 偏移向量，加盐
         */
        private String iv;
    }

    /**
     * DES加密
     *
     * @param data 待加密的数据
     * @param key  密钥，长度必须为8位
     * @param meta 元数据，可以设置模式
     * @return 加密后的数据，使用Base64编码
     */
    public String encode(String data, String key, @Nullable Meta meta) {
        if (meta == null) meta = new Meta();
        DES des = new DES(meta.getMode(), Padding.PKCS5Padding, key.getBytes(), meta.getIv().getBytes());
        return des.encryptBase64(data);
    }

    /**
     * DES解密
     *
     * @param encryptedData 加密后的数据，使用Base64编码
     * @param key           密钥，长度必须为8位
     * @param meta          元数据，可以设置模式
     * @return 解密后的数据
     */
    public String decode(String encryptedData, String key, @Nullable Meta meta) {
        if (meta == null) meta = new Meta();
        DES des = new DES(meta.getMode(), Padding.PKCS5Padding, key.getBytes(), meta.getIv().getBytes());
        return des.decryptStr(encryptedData);
    }

    /**
     * DES加密
     *
     * @param data 待加密的数据
     * @param key  密钥，长度必须为8位
     * @return 加密后的数据，使用Base64编码
     */
    public String encodeBySecurity(@NotNull String data, @NotNull String key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // 根据密钥生成密钥规范
        KeySpec keySpec = new DESKeySpec(key.getBytes());
        // 根据密钥规范生成密钥工厂
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(EncType.DES.getName());
        // 根据密钥工厂和密钥规范生成密钥
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

        // 根据加密算法获取加密器
        Cipher cipher = Cipher.getInstance(EncType.DES.getName());
        // 初始化加密器，设置加密模式和密钥
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        // 加密数据
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        // 对加密后的数据进行Base64编码
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * DES解密
     *
     * @param encryptedData 加密后的数据，使用Base64编码
     * @param key           密钥，长度必须为8位
     * @return 解密后的数据
     */
    public String decodeBySecurity(String encryptedData, @NotNull String key) throws InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        // 根据密钥生成密钥规范
        KeySpec keySpec = new DESKeySpec(key.getBytes());
        // 根据密钥规范生成密钥工厂
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(EncType.DES.getName());
        // 根据密钥工厂和密钥规范生成密钥
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

        // 对加密后的数据进行Base64解码
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        // 根据加密算法获取解密器
        Cipher cipher = Cipher.getInstance(EncType.DES.getName());
        // 初始化解密器，设置解密模式和密钥
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        // 解密数据
        byte[] decryptedData = cipher.doFinal(decodedData);
        // 将解密后的数据转换为字符串
        return new String(decryptedData);
    }
}
