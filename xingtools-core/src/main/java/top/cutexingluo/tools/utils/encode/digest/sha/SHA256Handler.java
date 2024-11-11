package top.cutexingluo.tools.utils.encode.digest.sha;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.encode.base.EncType;
import top.cutexingluo.tools.utils.encode.core.AbstractDigestHandler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA256 密码学哈希函数
 * <p>属于‌SHA-2（安全哈希算法2系列）中的一种‌</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 15:17
 * @since 1.1.6
 */
public class SHA256Handler extends AbstractDigestHandler {

    @Contract(value = " -> new", pure = true)
    public static @NotNull
    SHA256Handler newInstance() {
        return new SHA256Handler();
    }

    @Override
    public MessageDigest initMessageDigest() throws NoSuchAlgorithmException {
        this.messageDigest = MessageDigest.getInstance(EncType.SHA256.getName());
        return messageDigest;
    }

    /**
     * 使用Security 并格式化
     *
     * @param data 数据
     */
    public String encodeBySecurityFormat(@NotNull String data) {
        byte[] digest = this.encodeBySecurity(data);
        StringBuilder stringBuilder = new StringBuilder();
        //将byte数组转换为16进制字符串
        for (byte b : digest) {
            stringBuilder.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }
        return stringBuilder.toString();
    }

}
