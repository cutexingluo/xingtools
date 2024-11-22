package top.cutexingluo.tools.utils.encode.digest.sha;

import cn.hutool.crypto.digest.DigestUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
    public static @NotNull
    SHA256 newInstance() {
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
            return SHA256Handler.newInstance().encodeBySecurityFormat(data);
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
            return SHA256Handler.newInstance().encodeBySecurityFormat(data).equals(encrypted);
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


}
