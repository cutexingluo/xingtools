package top.cutexingluo.tools.utils.encrypt.hash.md5;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.encrypt.base.EncType;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Formatter;

/**
 * md5  信息摘要算法
 * <p>密码散列函数</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 9:41
 * @since 1.1.6
 */
public class Md5 {

    @Contract(value = " -> new", pure = true)
    public static @NotNull Md5 newInstance() {
        return new Md5();
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
        /**
         * 盐值加密
         */
        Salt,
        /**
         * 随机20个字符盐加密并拼接
         */
        Random20Salt,
    }


    /**
     * 元数据
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private String salt;
        private int index = 0;
        private int count = 1;
    }

    /**
     * 编码/加密
     *
     * @param data 数据
     * @param algo 算法
     * @param meta 元数据，只有 salt 有用
     */
    public String encode(String data, @NotNull ALGO algo, Meta meta) throws NoSuchAlgorithmException {
        switch (algo) {
            case Security:
                return encodeBySecurity(data);
            case Salt:
                return encodeBySalt(data, meta);
            case Random20Salt:
                return encodeBySaltRandom(data);
            default:
                return encodeHex(data);
        }
    }

    /**
     * 验证是否一致
     *
     * @param data      数据
     * @param encrypted 已加密数据
     * @param algo      算法
     * @param meta      元数据，只有 salt 有用
     * @return boolean 是否一致
     */
    public boolean validate(@NotNull String data, String encrypted, @NotNull ALGO algo, Meta meta) throws NoSuchAlgorithmException {
        switch (algo) {
            case Security:
                return encodeBySecurity(data).equals(encrypted);
            case Salt:
                return encodeBySalt(data, meta).equals(encrypted);
            case Random20Salt:
                return validateBySaltRandom(data, encrypted);
            default:
                return encodeHex(data).equals(encrypted);
        }
    }

    /**
     * 计算32位MD5摘要值，并转为16进制字符串
     *
     * <p>hutool</p>
     *
     * @see DigestUtil#md5Hex(String)
     */
    public String encodeHex(String data) {
        return DigestUtil.md5Hex(data);
    }

    /**
     * 计算32位MD5摘要值，使用UTF-8编码
     *
     * <p>hutool</p>
     *
     * @see DigestUtil#md5(String)
     */
    public byte[] encode(String data) {
        return DigestUtil.md5(data);
    }

    /**
     * 使用Security 加密
     *
     * @param data 数据
     */
    public String encodeBySecurity(@NotNull String data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest;
        byte[] digest = new byte[0];
        try {
            messageDigest = MessageDigest.getInstance(EncType.MD5.getName());
            digest = messageDigest.digest(data.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
        Formatter formatter = new Formatter();
        for (byte b : digest) { // 补齐前导0
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    /**
     * 按SALT编码
     *
     * @param data 数据
     * @param meta 盐
     */
    public String encodeBySalt(String data, Meta meta) {
        if (StrUtil.isBlank(data)) {
            return data;
        }
        if (StrUtil.isBlank(meta.getSalt())) {
            return encodeHex(data);
        }
        MD5 md5 = new MD5(meta.getSalt().getBytes(StandardCharsets.UTF_8),
                meta.getIndex(), meta.getCount());
        // 返回16进制格式
        return md5.digestHex(data);
    }

    /**
     * SALT 长度
     */
    public static int SALT_BYTE_LENGTH = 20;

    /**
     * 按随机SALT编码
     *
     * @param data 数据
     */
    public String encodeBySaltRandom(String data) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_LENGTH];
        secureRandom.nextBytes(salt);
        MD5 md5 = new MD5(salt);
        byte[] digest = md5.digest(data, StandardCharsets.UTF_8);
        //填充前20个字节为盐值，校验密码时候需要取出
        byte[] pwd = ArrayUtil.addAll(salt, digest);
        return HexUtil.encodeHexStr(pwd);
    }

    /**
     * 按随机SALT验证
     *
     * @param data      数据
     * @param encrypted 已加密
     */
    public boolean validateBySaltRandom(String data, String encrypted) {
        byte[] encryptedPwd = HexUtil.decodeHex(encrypted);
        //取出前20个字节盐值
        byte[] salt = ArrayUtil.sub(encryptedPwd, 0, SALT_BYTE_LENGTH);
        //20字节后为真正MD5后密码
        byte[] pwd = ArrayUtil.sub(encryptedPwd, SALT_BYTE_LENGTH, encryptedPwd.length);
        MD5 md5 = new MD5(salt);
        byte[] digest = md5.digest(data, StandardCharsets.UTF_8);
        return Arrays.equals(digest, pwd);
    }
}
