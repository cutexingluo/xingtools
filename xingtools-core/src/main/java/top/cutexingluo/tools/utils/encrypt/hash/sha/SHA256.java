package top.cutexingluo.tools.utils.encrypt.hash.sha;

import cn.hutool.crypto.digest.DigestUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.encrypt.base.EncType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA256 密码学哈希函数
 * <p>属于‌SHA-2（安全哈希算法2系列）中的一种‌</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 9:52
 * @since 1.1.6
 */
public class SHA256 {

    @Contract(value = " -> new", pure = true)
    public static @NotNull SHA256 newInstance() {
        return new SHA256();
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
     * 编码/加密
     *
     * @param data 数据
     * @param algo 算法
     */
    public String encode(String data, ALGO algo) throws NoSuchAlgorithmException {
        if (algo == ALGO.Security) {
            return encodeBySecurity(data);
        }
        return encodeHex(data);
    }

    /**
     * 验证是否一致
     *
     * @param data      数据
     * @param encrypted 已加密数据
     * @param algo      算法
     */
    public boolean validate(String data, String encrypted, ALGO algo) throws NoSuchAlgorithmException {
        if (algo == ALGO.Security) {
            return encodeBySecurity(data).equals(encrypted);
        }
        return encodeHex(data).equals(encrypted);
    }

    /**
     * 计算SHA-256摘要值，并转为16进制字符串
     *
     * @see DigestUtil#sha256Hex(String)
     */
    public String encodeHex(String data) {
        return DigestUtil.sha256Hex(data);
    }

    /**
     * 使用Security 加密
     *
     * @param data 数据
     */
    public String encodeBySecurity(@NotNull String data) throws NoSuchAlgorithmException {
        //获取SHA-256算法实例
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];
        try {
            messageDigest = MessageDigest.getInstance(EncType.SHA256.getName());
            //计算散列值
            digest = messageDigest.digest(data.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
        StringBuilder stringBuilder = new StringBuilder();
        //将byte数组转换为16进制字符串
        for (byte b : digest) {
            stringBuilder.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }
        return stringBuilder.toString();
    }

}
